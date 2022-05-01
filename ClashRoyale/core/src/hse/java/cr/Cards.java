package hse.java.cr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.model.Character;

class Card extends Actor {
    enum State {
        NORMAL,
        PRESSED
    }

    private final TextureRegion cardTexture;
    private TextureAtlas characterAtlas;
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
        if (state.equals(State.PRESSED)) {
            setPosition(Gdx.input.getX(), Gdx.input.getY());
        }
        batch.draw(cardTexture, getX(), getY(), getWidth(), getHeight());
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setCenterPosition(float x, float y) {
        this.setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }
}

public class Cards {
    private Stage cardStage;
    private Array<Card> cardConveyor;
    private Stage gameStage;
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
        cardStage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 0; i < 4; i++) {
                    curCard = cardConveyor.get(i);
                    if (curCard.getX() + curCard.getWidth() >= x
                        && curCard.getY() + curCard.getHeight() >= y) {
                        curCard.setCenterPosition(x, y);
                        //gameStage.addActor(new Character(curCard.getCharacterAtlas()));
                        break;
                    }
                }
            }
        });

        Gdx.input.setInputProcessor(cardStage);
    }

    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < 4; i++) {
            cardConveyor.get(i).draw(batch, parentAlpha);
        }
    }
}
