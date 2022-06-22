package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import hse.java.cr.client.Player;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.client.Starter;
import hse.java.cr.events.NewCharacterEvent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CardInterface extends Group {
    private final Mana mana;
    private final Stage gameStage;
    private final GameField gameField;

    public CardInterface(Assets assets, Stage gameStage) {
        this.gameStage = gameStage;
        mana = new Mana();
        gameField = new GameField();
        addActor(new Card(assets, "brownGolem"));
        addActor(new Card(assets, "grayGolem"));
        addActor(new Card(assets, "greenGoblin"));
        addActor(new Card(assets, "greenGolem"));

        float width = Arrays.stream(getChildren().items).max(Comparator.comparing(
                Actor::getWidth)).orElse(getChild(0)).getWidth();
        for (int i = 0; i < 4; i++) {
            getChild(i).setPosition(0 + i * width, 0);
        }
    }

    public Mana getMana() {
        return mana;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (Arrays.stream(getChildren().items).anyMatch(
                actor -> {
                    Card card = (Card) actor;
                    return card.getState().equals(Card.State.TOUCH_DOWN);
                }))
        {
            batch.end();
            gameField.draw();
            batch.begin();
        }
        float width = Arrays.stream(getChildren().items).max(Comparator.comparing(
                Actor::getWidth)).orElse(getChild(0)).getWidth();

        final int groupSize = getChildren().size;
        for (int i = 0; i < groupSize; i++) {
            Card curCard = (Card) getChild(i);
            switch (curCard.getState()) {
                case TOUCH_DOWN: {
                    curCard.setCenterPosition(Gdx.input.getX(),
                            Gdx.graphics.getHeight() - Gdx.input.getY());
                    curCard.draw(batch, 0.5f);
                    break;
                }
                case TOUCH_UP: {
                    if (gameField.overlaps(Gdx.input.getX(),
                            Gdx.graphics.getHeight() - Gdx.input.getY())
                        && mana.decreaseMana(curCard.getCost())) {

                        final NewCharacterEvent characterEvent = new NewCharacterEvent();
                        characterEvent.characterName = curCard.getName();
                        characterEvent.x = curCard.getX();
                        characterEvent.y = curCard.getY();
                        characterEvent.gameIndex = Player.gameIndex;
                        if (Starter.getClient() != null) {
                            Starter.getClient().sendTCP(characterEvent);
                        }

                        gameStage.addActor(new Character(
                                curCard.getName(),
                                curCard.getX(),
                                curCard.getY(),
                                true,
                                Player.charactersCount++
                        ));


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

