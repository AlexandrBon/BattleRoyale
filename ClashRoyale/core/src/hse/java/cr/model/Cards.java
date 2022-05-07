package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.Assets;

class Card extends Actor {
    enum State {
        NORMAL,
        TOUCH_DOWN,
        TOUCH_UP
    }

    private final TextureRegion cardTexture;
    private final TextureAtlas characterAtlas;
    private State state = State.NORMAL;

    public Card(TextureRegion cardTexture, TextureAtlas characterAtlas) {
        this.cardTexture = cardTexture;
        this.characterAtlas = characterAtlas;

        float scale = Gdx.graphics.getHeight() / 5f / cardTexture.getRegionHeight();

        setBounds(0, 0,
                cardTexture.getRegionWidth() * scale,
                cardTexture.getRegionHeight() * scale);
    }

    public TextureAtlas getCharacterAtlas() {
        return characterAtlas;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, parentAlpha);
        batch.draw(cardTexture, getX(), getY(), getWidth(), getHeight());
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }


    public void setCenterPosition(float x, float y) {
        this.setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }
}

public class Cards {
    private final Stage cardStage;
    private final Array<Card> cardConveyor;
    private final Stage gameStage;
    private Card curCard;

    public Cards(Assets assets, Stage gameStage) {
        cardStage = new Stage();
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

        float width = cardConveyor.get(0).getWidth();
        for (int i = 0; i < 4; i++) {
            cardConveyor.get(i).setPosition(0 + i * width, 0);
            cardStage.addActor(cardConveyor.get(i));
        }
        cardStage.addListener(new InputListener() {
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
        Gdx.input.setInputProcessor(cardStage);
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
                    gameStage.addActor(new Character(curCard.getCharacterAtlas(),
                        curCard.getX(), curCard.getY()));
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
