package hse.java.cr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private final int LOAD_LINE_WIDTH;
    private final int LOAD_LINE_HEIGHT;
    private final ShapeRenderer loadLine;
    private final Texture loadingBackground;
    private final Starter game;
    private final BitmapFont font;

    public LoadingScreen(@NotNull Starter game) {
        LOAD_LINE_WIDTH = Gdx.graphics.getWidth();
        LOAD_LINE_HEIGHT = Gdx.graphics.getHeight() / 15;
        this.game = game;
        loadLine = new ShapeRenderer();
        assets = game.getAssets();
        loadingBackground = ScreenAssistant.
                getBackground("backgrounds/game_background_1.png");

        camera = new OrthographicCamera();//new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
        camera.setToOrtho(false);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().scale(5);
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
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

        boolean isLoaded = assets.updateManager(1);
        float progress = assets.getManager().getProgress();

        batch.begin();

        batch.draw(loadingBackground, 0, 0);
        font.draw(batch, Integer.valueOf((int)(progress * 100)).toString() + "%",
                (float)LOAD_LINE_WIDTH / 2 - 50, LOAD_LINE_HEIGHT + 100);

        batch.end();

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
