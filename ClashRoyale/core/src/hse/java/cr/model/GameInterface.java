package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.Assets;


public class GameInterface {
    private final Stage gameInterfaceStage;
    private final Array<Card> cardConveyor;
    private final Stage gameStage;
    private Card curCard;
    private final Mana mana;

    public GameInterface(Assets assets, Stage gameStage) {
        mana = new Mana();
        this.gameStage = gameStage;
        cardConveyor = new Array<>(4);
        // TODO need to init from User's Card Deck(or randomly generate every new game)
        cardConveyor.add(new Card(
                assets.get(Assets.cardsAtlas).findRegion("brownGolem"),
                assets.get(Assets.brownGolem)));
        cardConveyor.add(new Card(
                assets.get(Assets.cardsAtlas).findRegion("grayGolem"),
                assets.get(Assets.grayGolem)));
        cardConveyor.add(new Card(
                assets.get(Assets.cardsAtlas).findRegion("greenGoblin"),
                assets.get(Assets.greenGoblin)));
        cardConveyor.add(new Card(
                assets.get(Assets.cardsAtlas).findRegion("greenGolem"),
                assets.get(Assets.greenGolem)));
        gameInterfaceStage = new Stage();
        setupGameInterfaceStage();
    }

    public Mana getMana() {
        return mana;
    }

    private void setupGameInterfaceStage() {
        float width = cardConveyor.get(0).getWidth();

        for (int i = 0; i < 4; i++) {
            cardConveyor.get(i).setPosition(0 + i * width, 0);
            gameInterfaceStage.addActor(cardConveyor.get(i));
        }

        gameInterfaceStage.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                for (int i = 0; i < 4; i++) {
                    curCard = cardConveyor.get(i);
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
                    curCard = cardConveyor.get(i);
                    if (curCard.getRight() >= x && curCard.getTop() >= y) {
                        curCard.setState(Card.State.TOUCH_UP);
                        break;
                    }
                }
            }
        });
        Gdx.input.setInputProcessor(gameInterfaceStage);
    }

    public void draw(Batch batch, float parentAlpha) {
        float width = cardConveyor.get(0).getWidth();
        for (int i = 0; i < 4; i++) {
            curCard = cardConveyor.get(i);
            switch (curCard.getState()) {
                case TOUCH_DOWN: {
                    curCard.setCenterPosition(Gdx.input.getX(),
                            Gdx.graphics.getHeight() - Gdx.input.getY());
                    curCard.draw(batch,  0.5f);
                    break;
                }
                case TOUCH_UP: {
                    if (mana.decreaseMana(curCard.getCost())) {
                        gameStage.addActor(new Character(curCard.getCharacterAtlas(),
                                curCard.getX(), curCard.getY(), true));
                    }
                    curCard.setPosition(0 + i * width, 0);
                    curCard.setState(Card.State.NORMAL);
                    break;
                }
                case NORMAL: {
                    curCard.draw(batch, parentAlpha);
                    break;
                }
            }
        }
    }

    public void dispose() {
        gameInterfaceStage.dispose();
    }
}
