package hse.java.cr.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hse.java.cr.buttons.UIButton;
import hse.java.cr.client.Player;
import hse.java.cr.client.screens.MainScreen;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.wrappers.FontSizeHandler;

public class GameInterface {
    private final CardInterface cardInterface;
    private final Stage gameStage;
    private final Table table;
    private final UIButton quitButton;
    private final Stage interfaceStage;
    private final Assets assets;
    private final String enemyNickname;

    public GameInterface(Assets assets, Stage gameStage,
                         MainScreen mainScreen, String enemyNickname) {
        this.enemyNickname = enemyNickname;
        this.assets = assets;
        this.gameStage = gameStage;
        cardInterface = new CardInterface(assets, gameStage);
        setupGameStage();

        table = new Table();
        setupTable();
        gameStage.addActor(table);

        quitButton = new UIButton("Quit", assets);

        interfaceStage = new Stage();
        setupInterfaceStage(mainScreen);
    }

    private void setupTable() {
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Skin skin = assets.get(Assets.skin);
        Label.LabelStyle style = new Label.LabelStyle();

        style.font = FontSizeHandler.INSTANCE.getFont(Gdx.graphics.getHeight() / 12, Color.BLACK);
        style.fontColor = Color.WHITE;
        style.background = skin.getDrawable("button-color");
        Label enemyNicknameLabel = new Label(enemyNickname, style);

        table
                .add(enemyNicknameLabel)
                .width(enemyNicknameLabel.getWidth())
                .height(enemyNicknameLabel.getHeight())
                .row();
        table.top().right();
    }

    private void setupInterfaceStage(MainScreen mainScreen) {
        interfaceStage.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (quitButton.getState().equals(UIButton.State.HOVERED)) {
                    quitButton.setState(UIButton.State.PRESSED);
                    quitButton.playSound();
                    mainScreen.setToCurrentScreen();
                }
            }
        });
        interfaceStage.addActor(quitButton);
    }

    private void setupGameStage() {
        gameStage.addActor(cardInterface);
        gameStage.addListener(cardInterface.getInputListener());
    }

    public void updateGameState(Player.Status status) {
        Player.status = status;
        switch (status) {
            case EMPTY: {
                break;
            }
            case WIN: {
                quitButton.setPosition(
                        quitButton.getWidth() / 3,
                        (Gdx.graphics.getHeight()  - quitButton.getHeight()) / 2
                );
                Gdx.input.setInputProcessor(interfaceStage);
                break;
            }
            case LOSE: {
                quitButton.setPosition(
                        (Gdx.graphics.getWidth() - quitButton.getWidth()) / 2,
                        (Gdx.graphics.getHeight()  - quitButton.getHeight()) / 2
                );
                Gdx.input.setInputProcessor(interfaceStage);
                break;
            }
        }
    }

    public void act(float delta) {
        gameStage.act(delta);
        if (!Player.status.equals(Player.Status.EMPTY)) {
            interfaceStage.act();
        }
    }

    public void draw(Batch batch, float delta) {
        gameStage.draw();
        cardInterface.getMana().draw(batch.getProjectionMatrix(), delta);
        if (!Player.status.equals(Player.Status.EMPTY)) {
            interfaceStage.draw();
        }
    }
}
