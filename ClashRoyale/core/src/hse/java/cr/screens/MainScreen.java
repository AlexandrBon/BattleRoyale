package hse.java.cr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import hse.java.cr.Assets;
import hse.java.cr.Starter;
import hse.java.cr.buttons.SimpleButton;
import org.jetbrains.annotations.NotNull;

public class MainScreen implements Screen {
    private SpriteBatch batch;
    private Assets assets;
    private Starter game;
    private OrthographicCamera camera;
    private Sprite gameBackground;
    private SimpleButton playButton;
    private Stage stage;

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
        playButton = new SimpleButton("Play", assets);

        stage.addActor(playButton);
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (playButton.getState().equals(SimpleButton.State.HOVERED)) {
                    playButton.setState(SimpleButton.State.PRESSED);
                    playButton.playClickSound();
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.enableBlending(); // Enable alpha
        batch.begin();

        gameBackground.setAlpha(0.3f);
        gameBackground.draw(batch);

        batch.end();
        stage.draw();

        if (playButton.getState().equals(SimpleButton.State.PRESSED)) {
            game.setScreen(new GameScreen(game));
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
    }
}