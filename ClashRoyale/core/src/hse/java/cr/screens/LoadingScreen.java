package hse.java.cr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.client.Starter;
import org.jetbrains.annotations.NotNull;

public class LoadingScreen implements Screen {
    private Assets assets;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private int LOAD_LINE_WIDTH;
    private int LOAD_LINE_HEIGHT;
    private ShapeRenderer loadLine;
    private Sprite loadingBackground;
    private final Starter game;
    private BitmapFont font;

    public LoadingScreen(@NotNull Starter game) {
        this.game = game;
    }

    @Override
    public void show() {
        LOAD_LINE_WIDTH = Gdx.graphics.getWidth();
        LOAD_LINE_HEIGHT = Gdx.graphics.getHeight() / 15;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        loadLine = new ShapeRenderer();
        assets = game.getAssets();

        loadingBackground = new Sprite(new Texture("backgrounds/game_background_1.png"));
        loadingBackground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().scale(5);
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        loadLine.setProjectionMatrix(camera.combined);

        boolean isLoaded = assets.updateManager(30);
        float progress = assets.getManager().getProgress();

        batch.begin();

        loadingBackground.draw(batch);
        font.draw(batch, Integer.valueOf((int)(progress * 100f)).toString() + "%",
                (float)LOAD_LINE_WIDTH / 2 - 70, LOAD_LINE_HEIGHT + 100);

        batch.end();

        loadLine.begin(ShapeRenderer.ShapeType.Filled);
        loadLine.setColor(Color.GOLDENROD);
        loadLine.rect(0, 0, progress * LOAD_LINE_WIDTH, LOAD_LINE_HEIGHT);
        loadLine.end();
        if (isLoaded) {
            try {
                assets.fillStringToTextureAtlasMap();
            } catch (ReflectionException e) {
                System.out.println("FUCK");
            }
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
    }
}
