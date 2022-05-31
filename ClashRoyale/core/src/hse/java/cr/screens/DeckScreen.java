package hse.java.cr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import hse.java.cr.Assets;
import hse.java.cr.Starter;
import hse.java.cr.buttons.UIButton;
import hse.java.cr.model.CardInfo;

public class DeckScreen implements Screen {
    int index = 0;
    private TextureRegion curFrame;
    private MainScreen mainScreen;
    private SpriteBatch batch;
    private Assets assets;
    private final Starter game;
    private OrthographicCamera camera;
    private Sprite gameBackground;
    private ArrayList<CardInfo> cardInfos;
    private UIButton backButton;
    private UIButton leftButton;
    private UIButton rightButton;
    private UIButton addButton;
    private Stage stage;

    public DeckScreen(@NotNull Starter game, MainScreen screen) {
        this.game = game;
        mainScreen = screen;
    }

    @Override
    public void show() {
        stage = new Stage();
        curFrame = new Sprite();
        assets = game.getAssets();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        cardInfos = new ArrayList<>();
        cardInfos.add(new CardInfo(assets.get(Assets.fire), "Fire", "Spell", 0, 0, 0));
        cardInfos.add(new CardInfo(assets.get(Assets.grayGolem), "Fire", "Character", 0, 0, 0));
        cardInfos.add(new CardInfo(assets.get(Assets.greenGoblin), "Fire", "Character", 0, 0, 0));
        cardInfos.add(new CardInfo(assets.get(Assets.greenGolem), "Fire", "Character", 0, 0, 0));
        gameBackground = new Sprite(assets.get(Assets.gameBackground));
        gameBackground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backButton = new UIButton("Play", assets);
        leftButton = new UIButton("Play", assets);
        rightButton = new UIButton("Play", assets);
        leftButton.moveBy(100, 100);
        rightButton.moveBy(-100, 100);

        stage.addActor(backButton);
        stage.addActor(leftButton);
        stage.addActor(rightButton);
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (backButton.getState().equals(UIButton.State.HOVERED)) {
                    backButton.setState(UIButton.State.PRESSED);
                    backButton.playSound();
                }
                if (leftButton.getState().equals(UIButton.State.HOVERED)) {
                    leftButton.setState(UIButton.State.PRESSED);
                    leftButton.playSound();
                }
                if (rightButton.getState().equals(UIButton.State.HOVERED)) {
                    rightButton.setState(UIButton.State.PRESSED);
                    rightButton.playSound();
                }
            }
        });
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

        curFrame = cardInfos.get(index).getAnimation().getKeyFrame(0);

        batch.draw(curFrame, 200,  200,
                100 , 100);

        batch.end();
        stage.draw();

        if (backButton.getState().equals(UIButton.State.PRESSED)) {
            game.setScreen(mainScreen);
        }
        if (leftButton.getState().equals(UIButton.State.PRESSED)) {
            leftButton.setState(UIButton.State.NORMAL);
            index = (index - 1 + cardInfos.size()) % cardInfos.size();
            curFrame = null;
        }
        if (rightButton.getState().equals(UIButton.State.PRESSED)) {
            rightButton.setState(UIButton.State.NORMAL);
            index = (index + 1) % cardInfos.size();
            curFrame = null;
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

    }
}
