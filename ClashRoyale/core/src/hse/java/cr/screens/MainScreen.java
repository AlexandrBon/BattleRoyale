package hse.java.cr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Client;
import hse.java.cr.buttons.UIButton;
import hse.java.cr.client.JoinResponseListener;
import hse.java.cr.client.Starter;
import hse.java.cr.events.JoinRequestEvent;
import hse.java.cr.network.Network;
import hse.java.cr.wrappers.Assets;
import org.jetbrains.annotations.NotNull;

public class MainScreen implements Screen {
    private SpriteBatch batch;
    private Assets assets;
    private final Starter game;
    private OrthographicCamera camera;
    private Sprite gameBackground;
    private UIButton playButton;
    private UIButton quitButton;
    private Stage stage;
    public static boolean isGameRunning = false;
    private boolean zoomingOut;

    public MainScreen(@NotNull Starter game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        assets = game.getAssets();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        gameBackground = new Sprite(assets.get(Assets.mainMenuBackground));
        gameBackground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        playButton = new UIButton("Play", assets);
        quitButton = new UIButton("Quit", assets);
        quitButton.setPosition(
                playButton.getX(),
                playButton.getY() - playButton.getHeight()
        );
        stage.addActor(quitButton);
        stage.addActor(playButton);

        Gdx.input.setInputProcessor(stage);

        stage.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (playButton.getState().equals(UIButton.State.HOVERED)) {
                    setupClient();
                    playButton.setState(UIButton.State.PRESSED);
                    playButton.playSound();
                }
                if (quitButton.getState().equals(UIButton.State.HOVERED)) {
                    quitButton.setState(UIButton.State.PRESSED);
                    quitButton.playSound();
                    System.exit(0);
                }
            }
        });
    }

    private void setupClient() {
        final Client client = new Client();

        Network.register(client);

        try {
            client.start();
            client.connect(10000, "localhost", 54555, 54777);
        } catch (Exception e) {
            System.out.println("client couldn't connect");
            return;
        }

        // Success
        Starter.setClient(client);
        Starter.getClient().addListener(new JoinResponseListener());
        JoinRequestEvent joinRequestEvent = new JoinRequestEvent();
        joinRequestEvent.username = "USERNAME";
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
        batch.begin();

        gameBackground.setAlpha(0.6f);
        gameBackground.draw(batch);

        batch.end();
        stage.draw();

        if (playButton.getState().equals(UIButton.State.PRESSED)) {
            if (isGameRunning || Starter.getClient() == null) {
                game.setScreen(new GameScreen(game));
            } else {
                if (camera.zoom <= 0.8) {
                    zoomingOut = true;
                } else if (camera.zoom >= 0.99) {
                    zoomingOut = false;
                }
                float zoomSpeed = 0.002f;
                camera.zoom += zoomingOut ? zoomSpeed : -zoomSpeed;
            }
        }
    }

    @Override
    public void resize(int width, int height) {

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