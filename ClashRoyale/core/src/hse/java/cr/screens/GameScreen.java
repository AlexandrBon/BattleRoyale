package hse.java.cr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import hse.java.cr.client.EventListener;
import hse.java.cr.client.Player;
import hse.java.cr.client.Starter;
import hse.java.cr.wrappers.Assets;
import org.jetbrains.annotations.NotNull;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Assets assets;
    private final Starter starter;
    private OrthographicCamera camera;
    private Sprite gameBackground;
    private Player player;

    public GameScreen(@NotNull Starter starter) {
        this.starter = starter;
    }

    @Override
    public void show() {
        assets = starter.getAssets();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        gameBackground = new Sprite(assets.get(Assets.gameBackground));
        gameBackground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player = new Player(assets);
        Starter.getClient().addListener(new EventListener(player));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        {
            batch.begin();
            batch.draw(gameBackground,
                    !Player.isLeft ?
                            gameBackground.getX() + gameBackground.getWidth()
                            : gameBackground.getX(),
                    gameBackground.getY(),
                    !Player.isLeft ? -gameBackground.getWidth() : gameBackground.getWidth(),
                    gameBackground.getHeight()
                    );
            batch.end();
        }
        player.act(delta);
        player.draw(batch, delta);
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
