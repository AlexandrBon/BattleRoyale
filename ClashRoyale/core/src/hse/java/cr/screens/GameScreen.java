package hse.java.cr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import hse.java.cr.client.EventListener;
import hse.java.cr.client.JoinResponseListener;
import hse.java.cr.client.Player;
import hse.java.cr.events.JoinRequestEvent;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.model.GameInterface;
import hse.java.cr.client.Starter;
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
        Starter.getClient().addListener(new JoinResponseListener());
        Starter.getClient().sendTCP(new JoinRequestEvent());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        {
            batch.begin();
            gameBackground.draw(batch);
            batch.end();
        }
        player.draw(camera.combined, delta);
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
