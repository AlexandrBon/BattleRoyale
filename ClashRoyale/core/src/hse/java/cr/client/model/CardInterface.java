package hse.java.cr.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import hse.java.cr.client.Player;
import hse.java.cr.client.Starter;
import hse.java.cr.events.NewCharacterEvent;
import hse.java.cr.wrappers.Assets;

import java.util.Arrays;

public class CardInterface extends Group {
    private final Mana mana;
    private final Stage gameStage;
    private final GameField gameField;
    private float padInBorderX;
    private float padInBorderY;
    private float pad;
    private final Array<Card> tmpCardDeck;
    private final InputListener inputListener;

    private static final int CARD_INTERFACE_SIZE = 4;
    private static final int DECK_SIZE = 6;

    public CardInterface(Assets assets, Stage gameStage) {
        this.gameStage = gameStage;
        gameField = new GameField();
        Array<Card> cardDeck = new Array<>(true, DECK_SIZE, Card.class);
        tmpCardDeck = new Array<>(true, DECK_SIZE, Card.class);

        // tempCode
        // TODO: get cardDeck from user's choice
        cardDeck.add(new Card(assets, "brownGolem"));
        cardDeck.add(new Card(assets, "grayGolem"));
        cardDeck.add(new Card(assets, "greenOrc"));
        cardDeck.add(new Card(assets, "iceGolem"));
        cardDeck.add(new Card(assets, "lavaGolem"));
        cardDeck.add(new Card(assets, "greenOgre"));

        ///////////

        mana = new Mana(Card.getBorderWidth() * 4);
        if (cardDeck.size!= DECK_SIZE) {
            throw new RuntimeException("not enough count of cards: " + cardDeck.size);
        }

        cardDeck.forEach(this::addActor);
        centering();


        inputListener = new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                for (int i = 0; i < CARD_INTERFACE_SIZE; i++) {
                    Card curCard = (Card) getChild(i);
                    if (curCard.getRight() >= x && curCard.getTop() >= y) {
                        curCard.setState(Card.State.TOUCH_DOWN);
                        break;
                    }
                }
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                for (int i = 0; i < CARD_INTERFACE_SIZE; i++) {
                    Card curCard = (Card) getChild(i);
                    if (curCard.getRight() >= x && curCard.getTop() >= y
                            && curCard.getState().equals(Card.State.TOUCH_DOWN)) {
                        curCard.setState(Card.State.TOUCH_UP);
                        break;
                    }
                }
            }
        };
    }

    public InputListener getInputListener() {
        return inputListener;
    }

    private void centering() {
        float cardInterfaceWidth = Card.getBorderWidth() * CARD_INTERFACE_SIZE;
        pad = (Gdx.graphics.getWidth() - cardInterfaceWidth) / 2;
        Actor[] actors = getChildren().items;
        int id = 0;

        float borderWidth = Card.getBorderWidth();
        float borderHeight = Card.getBorderHeight();
        float scale = 0.1f;
        padInBorderX = scale * borderWidth / 2;
        padInBorderY = scale * borderHeight / 2;
        for (Actor actor : actors) {
            if (actor instanceof Card) {
                Card card = (Card) actor;
                float x = pad + id++ * borderWidth;
                float y = mana.getHeight();
                card.setBorderX(x);
                card.setBorderY(y);
                actor.setPosition(x + padInBorderX, y + padInBorderY);
            }
        }
        mana.setX(pad);
        mana.setY(0);
    }

    public Mana getMana() {
        return mana;
    }

    @Override
    public void act(float delta) {
        for (int i = 0; i < CARD_INTERFACE_SIZE; i++) {
            Card curCard = (Card) getChild(i);
            switch (curCard.getState()) {
                case TOUCH_DOWN: {
                    curCard.setCenterPosition(Gdx.input.getX(),
                            Gdx.graphics.getHeight() - Gdx.input.getY());
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

                        tmpCardDeck.clear();
                        tmpCardDeck.add((Card) getChild(DECK_SIZE - 1));
                        for (int j = 0; j < CARD_INTERFACE_SIZE; j++) {
                            if (j != i) {
                                tmpCardDeck.add((Card) getChild(j));
                            }
                        }
                        tmpCardDeck.add((Card) getChild(i));
                        tmpCardDeck.add((Card) getChild(DECK_SIZE - 2));

                        clear();
                        tmpCardDeck.forEach(this::addActor);

                        for (int j = 0; j < CARD_INTERFACE_SIZE; j++) {
                            getChild(j).setPosition(
                                    pad + j * Card.getBorderWidth() + padInBorderX,
                                    Card.getBorderY() + padInBorderY
                            );
                            ((Card) getChild(j)).setBorderX(pad + j * Card.getBorderWidth());
                        }
                    } else {
                        curCard.setPosition(curCard.getBorderX() + padInBorderX, Card.getBorderY() + padInBorderY);
                    }
                    curCard.setState(Card.State.NORMAL);
                    break;
                }
                case NORMAL: {
                    break;
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (Arrays.stream(getChildren().items).anyMatch(
                actor -> {
                    Card card = (Card) actor;
                    return card != null && card.getState().equals(Card.State.TOUCH_DOWN);
                }))
        {
            batch.end();
            gameField.draw();
            batch.begin();
        }

        for (int i = 0; i < CARD_INTERFACE_SIZE; i++) {
            Card curCard = (Card) getChild(i);
            switch (curCard.getState()) {
                case TOUCH_DOWN: {
                    curCard.draw(batch, 0.5f);
                    break;
                }
                case TOUCH_UP: {
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

