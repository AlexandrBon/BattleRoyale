package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.client.Starter;
import hse.java.cr.events.NewCharacterEvent;

public class CardInterface extends Group {
    private final Mana mana;
    private final Stage gameStage;

    public CardInterface(Assets assets, Stage gameStage) {
        this.gameStage = gameStage;
        mana = new Mana();
        addActor(new Card(assets, "brownGolem"));
        addActor(new Card(assets, "grayGolem"));
        addActor(new Card(assets, "greenGoblin"));
        addActor(new Card(assets, "greenGolem"));

        float width = getChild(0).getWidth();

        for (int i = 0; i < 4; i++) {
            getChild(i).setPosition(0 + i * width, 0);
        }
    }

    public Mana getMana() {
        return mana;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float width = getChild(0).getWidth();
        for (int i = 0; i < 4; i++) {
            Card curCard = (Card) getChild(i);
            switch (curCard.getState()) {
                case TOUCH_DOWN: {
                    curCard.setCenterPosition(Gdx.input.getX(),
                            Gdx.graphics.getHeight() - Gdx.input.getY());
                    curCard.draw(batch, 0.5f);
                    break;
                }
                case TOUCH_UP: {
                    if (mana.decreaseMana(curCard.getCost())) {
                        gameStage.addActor(new Character(curCard.getCharacterAtlas(),
                                curCard.getX(), curCard.getY(), true));

                        final NewCharacterEvent characterEvent = new NewCharacterEvent();
                        characterEvent.characterName = curCard.getName();
                        characterEvent.x = curCard.getX();
                        characterEvent.y = curCard.getY();
                        Starter.getClient().sendTCP(characterEvent);
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
}

