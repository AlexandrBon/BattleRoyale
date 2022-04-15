package hse.java.cr.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import hse.java.cr.Data;
import hse.java.cr.Golem;
import hse.java.cr.Starter;
import org.jetbrains.annotations.NotNull;

public class GameScreen implements Screen {
    private final SpriteBatch batch;
    private final Data assets;
    private final Starter game;
    private final OrthographicCamera camera;
    private final Texture gameBackground;
    private final Golem golem = new Golem("golemAnimation2/packed.atlas");

    public GameScreen(@NotNull Starter game) {
        this.game = game;
        assets = game.getAssets();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        gameBackground = ScreenAssistant.
                getBackground("backgrounds/game_background_2.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(gameBackground, 0, 0);
        golem.draw(batch, 1);

        batch.end();

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

    }
}
