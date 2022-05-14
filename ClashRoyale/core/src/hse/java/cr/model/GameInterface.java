package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import hse.java.cr.wrappers.Assets;

public class GameInterface {
    private final CardInterface cardInterface;
    public final Stage gameStage;
    private Card curCard;

    public GameInterface(Assets assets, Stage gameStage) {
        this.gameStage = gameStage;
        cardInterface = new CardInterface(assets, gameStage);
        setupGameStage();
    }

    public CardInterface getCardInterface() {
        return cardInterface;
    }

    private void setupGameStage() {
        gameStage.addActor(cardInterface);
        gameStage.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                for (int i = 0; i < 4; i++) {
                    curCard = (Card) cardInterface.getChild(i);
                    if (curCard.getRight() >= x && curCard.getTop() >= y) {
                        curCard.setState(Card.State.TOUCH_DOWN);
                        break;
                    }
                }
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                for (int i = 0; i < 4; i++) {
                    curCard = (Card) cardInterface.getChild(i);
                    if (curCard.getRight() >= x && curCard.getTop() >= y) {
                        curCard.setState(Card.State.TOUCH_UP);
                        break;
                    }
                }
            }
        });
        Gdx.input.setInputProcessor(gameStage);
    }

    public void dispose() {
    }
}
