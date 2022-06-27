package hse.java.cr.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Client;
import hse.java.cr.buttons.UIButton;
import hse.java.cr.client.JoinResponseListener;
import hse.java.cr.client.Starter;
import hse.java.cr.events.JoinRequestEvent;
import hse.java.cr.network.Network;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.wrappers.FontSizeHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MainScreen implements Screen {
    private final SpriteBatch batch;
    private final Assets assets;
    private final Starter starter;
    private final OrthographicCamera camera;
    private final Sprite gameBackground;
    private final UIButton playButton;
    private final UIButton exitButton;
    private final Stage stage;
    public static boolean isGameRunning = false;
    private boolean zoomingOut;
    private final Table table;
    private TextField nicknameTextField;
    private final Viewport viewport;


    public MainScreen(@NotNull Starter starter) {
        this.starter = starter;
        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        assets = starter.getAssets();
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        gameBackground = new Sprite(assets.get(Assets.mainMenuBackground));
        gameBackground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        playButton = new UIButton("Play", assets);
        exitButton = new UIButton("Exit", assets);

        exitButton.setPosition(
                playButton.getX(),
                playButton.getY() - playButton.getHeight()
        );

        stage = new Stage();
        setupStage();
        setupTable();
        stage.addActor(table);
    }

    @Override
    public void show() {
        // TODO: show current score number: read it from 'rewards' file
        Gdx.input.setInputProcessor(stage);
    }

    private void setupStage() {
        stage.addActor(exitButton);
        stage.addActor(playButton);
        stage.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (playButton.getState().equals(UIButton.State.HOVERED)) {
                    setupClient();
                    playButton.setState(UIButton.State.PRESSED);
                    playButton.playSound();
                }
                if (exitButton.getState().equals(UIButton.State.HOVERED)) {
                    exitButton.setState(UIButton.State.PRESSED);
                    exitButton.playSound();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.exit(0);
                }
            }
        });
    }

    private void setupTable() {
        table.clear();

        float width = playButton.getWidth();
        float height = 2 * playButton.getHeight() / 3;

        Skin skin = assets.get(Assets.skin);
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();

        style.font = FontSizeHandler.INSTANCE.getFont((int) (height / 3), Color.BLACK);
        style.fontColor = Color.WHITE;
        style.background = skin.getDrawable("black");
        style.cursor = skin.getDrawable("white");
        style.selection = skin.getDrawable("pressed");
        final String defaultText = "enter your nickname";
        nicknameTextField = new TextField(defaultText, style);

        table
                .add(nicknameTextField)
                .width(width)
                .height(height)
                .padTop(Gdx.graphics.getHeight() - playButton.getTop() - height)
                .row();
        table.top();

        nicknameTextField.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (nicknameTextField.getText().equals(defaultText)) {
                    nicknameTextField.setText("");
                }
            }
        });
        nicknameTextField.addListener(new InputListener() {
            @Override
            public boolean keyUp (InputEvent event, int keycode) {
                if (Input.Keys.ENTER == keycode) {
                    nicknameTextField.clearSelection();
                }
                return true;
            }
        });
    }

    private void setupClient() {
        final Client client = new Client();

        Network.register(client);

        try {
            client.start();
            client.connect(10000, "localhost", 54555, 54777);
        } catch (IOException e) {
            System.out.println("client couldn't connect");
            return;
        }

        // Success
        Starter.setClient(client);
        Starter.getClient().addListener(new JoinResponseListener());
        JoinRequestEvent joinRequestEvent = new JoinRequestEvent();
        final String nickname = nicknameTextField.getText();
        joinRequestEvent.username =
                (nickname.equals("enter your nickname") || nickname.isEmpty() ? "noname" : nickname);
        joinRequestEvent.playersCount = 2;
        joinRequestEvent.screenHeight = Gdx.graphics.getHeight();
        joinRequestEvent.screenWidth = Gdx.graphics.getWidth();
        Starter.getClient().sendTCP(joinRequestEvent);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.8f, 0.8f, 0.8f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.enableBlending(); // Enable alpha
        {
            batch.begin();
            gameBackground.setAlpha(0.6f);
            gameBackground.draw(batch);
            batch.end();
        }
        stage.act();
        stage.draw();

        if (playButton.getState().equals(UIButton.State.PRESSED)) {
            if (isGameRunning/* || Starter.getClient() == null*/) {
                starter.setScreen(new GameScreen(this, assets));
                playButton.setState(UIButton.State.NORMAL);
                isGameRunning = false;
            } else {
                zooming();
            }
        }
    }

    public void setToCurrentScreen() {
        starter.setScreen(this);
    }

    private void zooming() {
        if (camera.zoom <= 0.8) {
            zoomingOut = true;
        } else if (camera.zoom >= 0.99) {
            zoomingOut = false;
        }
        float zoomSpeed = 0.002f;
        camera.zoom += zoomingOut ? zoomSpeed : -zoomSpeed;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        playButton.dispose();
        stage.dispose();
    }
}