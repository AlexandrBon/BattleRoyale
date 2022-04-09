package hse.java.cr.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import hse.java.cr.Data;
import hse.java.cr.Starter;
import org.jetbrains.annotations.NotNull;

public class LoadingScreen implements Screen {
    private final Data assets;
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private static final int LOAD_LINE_WIDTH = 1280;
    private static final int LOAD_LINE_HEIGHT = 70;
    private final ShapeRenderer loadLine;
    private final Texture loadingBackground;
    private final Starter game;

    public LoadingScreen(@NotNull Starter game) {
        this.game = game;
        loadLine = new ShapeRenderer();
        assets = game.getAssets();
        loadingBackground = ScreenAssistant.
                getBackground("backgrounds/game_background_1.png");

        camera = new OrthographicCamera(1280, 720);
        camera.setToOrtho(false);
        batch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        loadLine.setProjectionMatrix(camera.combined);

        boolean isLoaded = assets.updateManager(0);

        batch.begin();

        batch.draw(loadingBackground, 0, 0);

        batch.end();

        float progress = assets.getManager().getProgress();
        loadLine.begin(ShapeRenderer.ShapeType.Filled);
        loadLine.setColor(Color.RED);
        loadLine.rect(0, 0, progress * LOAD_LINE_WIDTH, LOAD_LINE_HEIGHT);
        loadLine.end();
        if (isLoaded) {
            game.setScreen(new MainScreen(game));
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
        loadLine.dispose();
        loadingBackground.dispose();
    }
}
