package hse.java.cr.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import hse.java.cr.wrappers.Assets;

import java.util.Objects;

public class Card extends Actor {
    enum State {
        NORMAL,
        TOUCH_DOWN,
        TOUCH_UP
    }

    private final TextureRegion card;
    private State state = State.NORMAL;
    private int cost;

    private final TextureRegion border;
    private static final String borderName = "border";
    private static float borderWidth;
    private static float borderHeight;
    private float borderX;
    private static float borderY;

    public Card(Assets assets, String characterName) {
        card = assets.get(Assets.cardsAtlas).findRegion(characterName);
        border = assets.get(Assets.cardsAtlas).findRegion(borderName);
        setName(characterName);
        cost = 3;

        borderWidth = (float) Gdx.graphics.getWidth() / 8.5f;
        borderHeight = (float) Gdx.graphics.getHeight() / 4.5f;

        float scale = (float) Math.min(
                borderWidth * 0.9 / card.getRegionWidth(),
                borderHeight * 0.9 / card.getRegionHeight()
        );

        setBounds(0, 0, card.getRegionWidth() * scale,
                card.getRegionHeight() * scale);
    }

    public void setBorderX(float x) {
        borderX = x;
    }

    public void setBorderY(float y) {
        borderY = y;
    }

    public float getBorderX() {
        return borderX;
    }

    public static float getBorderY() {
        return borderY;
    }

    public static float getBorderWidth() {
        return borderWidth;
    }

    public static float getBorderHeight() {
        return borderHeight;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, parentAlpha);
        batch.draw(card, getX(), getY(), getWidth(), getHeight());
        batch.draw(border, borderX, borderY, borderWidth, borderHeight);
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Card) {
            return ((Card) o).getName().equals(this.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

}
