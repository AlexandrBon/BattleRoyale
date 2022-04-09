package hse.java.cr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import hse.java.cr.Data;
import hse.java.cr.Starter;
import org.jetbrains.annotations.NotNull;

public class MainScreen implements Screen {
    private float frameDelta = 0f;
    private Texture curFrame;
    private final SpriteBatch batch;
    private final Data assets;
    private final Starter game;
    private final Animation<Texture> hitAnimation;
    private final OrthographicCamera camera;
    private final Texture gameBackground;

    public MainScreen(@NotNull Starter game) {
        this.game = game;
        assets = game.getAssets();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        gameBackground = ScreenAssistant.
                getBackground("backgrounds/game_background_2.png");

        Texture[] hitFrames = new Texture[12];
        for (int i = 0; i < 12; i++) {
            String path = "golemAnimation1/0_Golem_Run Slashing_0"
                    + (i < 10 ? "0" + i : i) + ".png";
            hitFrames[i] = assets.getManager().get(path);
        }
        hitAnimation = new Animation<>(0.07f, hitFrames);
        hitAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        curFrame = hitAnimation.getKeyFrame(frameDelta);
        batch.begin();

        batch.draw(gameBackground, 0, 0);
        batch.draw(curFrame, 0, 0);

        batch.end();
        frameDelta += Gdx.graphics.getDeltaTime();
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
        curFrame.dispose();
        batch.dispose();
    }
}