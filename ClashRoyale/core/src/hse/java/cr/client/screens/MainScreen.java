package hse.java.cr.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final Sprite gameBackground;

    private final UIButton playButton;
    private final UIButton exitButton;
    private final UIButton deckButton;

    private final Stage stage;
    private final Table table;

    public static boolean isGameRunning = false;
    private boolean zoomingOut;

    private TextField nicknameTextField;

    private TextButton mode2;
    private TextButton mode4;
    private TextButton mode8;

    private DeckScreen deckScreen = null;

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

        deckButton = new UIButton("Settings2", assets);
        deckButton.setPosition(
                playButton.getX() - deckButton.getWidth(),
                playButton.getY()
        );
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
        Gdx.input.setInputProcessor(stage);
    }

    private void setupStage() {
        stage.addActor(exitButton);
        stage.addActor(playButton);
        stage.addActor(deckButton);

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
                if (deckButton.getState().equals(UIButton.State.HOVERED)) {
                    deckButton.setState(UIButton.State.PRESSED);
                    deckButton.playSound();
                }
            }
        });
    }

    private void setupTable() {
        table.clear();

        // text field

        float textFieldWidth = playButton.getWidth();
        float textFieldHeight = 2 * playButton.getHeight() / 3;

        Skin skin = assets.get(Assets.skin);
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();

        BitmapFont font = FontSizeHandler.INSTANCE.getFont((int) (textFieldHeight / 2.3f), Color.BLACK);

        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = skin.getDrawable("black");
        textFieldStyle.cursor = skin.getDrawable("white");
        textFieldStyle.selection = skin.getDrawable("pressed");

        final String defaultText = "enter your nickname";
        nicknameTextField = new TextField(defaultText, textFieldStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.down = skin.getDrawable("black");
        textButtonStyle.up = skin.getDrawable("black");
        textButtonStyle.over = skin.getDrawable("black");
        textButtonStyle.checked = skin.getDrawable("button");
        textButtonStyle.checkedOver = skin.getDrawable("button");

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


        // mode buttons
        mode2 = new TextButton("2", textButtonStyle);
        mode4 = new TextButton("4", textButtonStyle);
        mode8 = new TextButton("8", textButtonStyle);

        ClickListener clickListener = new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                mode2.setChecked(false);
                mode4.setChecked(false);
                mode8.setChecked(false);
                if (mode2.getRight() >= x) {
                    mode2.setChecked(true);
                } else if (mode4.getRight() >= x) {
                    mode4.setChecked(true);
                } else {
                    mode8.setChecked(true);
                }
            }
        };

        float textButtonWidth = playButton.getWidth() / 3;
        float textButtonHeight = playButton.getHeight() * 0.66f;

        table.add(mode2).width(textButtonWidth).height(textButtonHeight).center();
        table.add(mode4).width(textButtonWidth).height(textButtonHeight).center();
        table.add(mode8).width(textButtonWidth).height(textButtonHeight).center();
        table.row();

        table.add(nicknameTextField).colspan(3).width(textFieldWidth).center();
        table.row();
        table.add(deckButton, playButton).row();

        table.add(exitButton).colspan(3).row();
    }

    private void setupClient() {
        final Client client = new Client();

        Network.register(client);

        try {
            client.start();
            client.connect(10000, "localhost", 54555, 54777);
        } catch (IOException e) {
            return;
        }

        // Success
        Starter.setClient(client);
        Starter.getClient().addListener(new JoinResponseListener());
        JoinRequestEvent joinRequestEvent = new JoinRequestEvent();
        final String nickname = nicknameTextField.getText();
        joinRequestEvent.username =
                (nickname.equals("enter your nickname") || nickname.isEmpty() ? "noname" : nickname);

        if (mode4.isChecked()) {
            joinRequestEvent.playersCount = 4;
        } else if (mode8.isChecked()) {
            joinRequestEvent.playersCount = 8;
        } else {
            joinRequestEvent.playersCount = 2;
        }

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
            if (isGameRunning || Starter.getClient() == null) {
                starter.setScreen(new GameScreen(this, assets));
                playButton.setState(UIButton.State.NORMAL);
                isGameRunning = false;
            } else {
                zooming();
            }
        }

        if (deckButton.getState().equals(UIButton.State.PRESSED)) {
            if (deckScreen == null) {
                deckScreen = new DeckScreen(starter, this);
            }
            deckButton.setState(UIButton.State.NORMAL);
            starter.setScreen(deckScreen);
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