package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import hse.java.cr.wrappers.Assets;
import org.jetbrains.annotations.NotNull;

public class Card extends Actor {
    enum State {
        NORMAL,
        TOUCH_DOWN,
        TOUCH_UP
    }

    private final TextureRegion cardTexture;
    private final TextureAtlas characterAtlas;
    private State state = State.NORMAL;
    private int cost;

    public Card(Assets assets, String characterName) {
        cardTexture = assets.get(Assets.cardsAtlas).findRegion(characterName);
        setName(characterName);
        this.characterAtlas = Assets.stringToTextureAtlas(characterName);
        cost = 1;

        float scale = Gdx.graphics.getHeight() / 5f / cardTexture.getRegionHeight();
        setBounds(0, 0, cardTexture.getRegionWidth() * scale,
                cardTexture.getRegionHeight() * scale);
    }

    public TextureAtlas getCharacterAtlas() {
        return characterAtlas;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, parentAlpha);
        batch.draw(cardTexture, getX(), getY(), getWidth(), getHeight());
        //batch.setColor(getColor());
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }


    public void setCenterPosition(float x, float y) {
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }
}
