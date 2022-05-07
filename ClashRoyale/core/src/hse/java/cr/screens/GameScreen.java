package hse.java.cr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import hse.java.cr.Assets;
import hse.java.cr.model.GameInterface;
import hse.java.cr.Starter;
import org.jetbrains.annotations.NotNull;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Assets assets;
    private final Starter game;
    private OrthographicCamera camera;
    private Sprite gameBackground;
    private GameInterface gameInterface;
    private Stage stage;

    public GameScreen(@NotNull Starter game) {
        this.game = game;
    }

    @Override
    public void show() {
        assets = game.getAssets();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        gameBackground = new Sprite(assets.get(Assets.gameBackground));
        gameBackground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage();
        gameInterface = new GameInterface(assets, stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        {
            batch.begin();
            gameBackground.draw(batch);
            gameInterface.draw(batch, 1f);
            batch.end();
        }
        stage.draw();
        gameInterface.getMana().draw(camera.combined, delta);
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
