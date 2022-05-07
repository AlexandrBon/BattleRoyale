package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Character extends Actor {
    public enum State {
        HIT,
        RUN,
        JUMP
    }

    private State state;
    private int health;
    private final Array<Animation<TextureRegion>> golemAnimations;
    private TextureRegion curFrame;
    private float frameDelta = 0f;

    public Character(TextureAtlas golemAtlas, float x, float y) {
        health = 100;
        golemAnimations = new Array<>(State.values().length);
        //setPosition((float)Math.random() * 500 + 100, (float)Math.random() * 500 + 100);
        curFrame = new Sprite();
        state = State.HIT;

        int n = golemAnimations.size;
        n = 1; // TODO: add RUN and JUMP animation

        for (int i = 0; i < n; i++) {
            golemAnimations.add(new Animation<>(0.07f, golemAtlas.getRegions()));
            golemAnimations.get(i).setPlayMode(Animation.PlayMode.LOOP);
            // TODO: JUMP PlayMode is .NORMAL
        }

        float textureWidth = golemAnimations.get(0).getKeyFrame(0).getRegionWidth();
        float textureHeight = golemAnimations.get(0).getKeyFrame(0).getRegionHeight();

        float height = Gdx.graphics.getHeight() / 3f;
        float width = height * textureWidth / textureHeight;

        setScale(width / textureWidth, height / textureHeight);

        setBounds(x, y, textureWidth * getScaleX(),
                textureHeight * getScaleY());

    }
    public Character(TextureAtlas golemAtlas) {
       this(golemAtlas, 0, 0);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void decreaseHealth(int damage) {
        this.health -= damage;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //moveBy(2, 0);
        curFrame = golemAnimations.get(state.ordinal()).getKeyFrame(frameDelta);

        batch.draw(curFrame, getX(),  getY(),
                getWidth() * getScaleX(), getHeight() * getScaleY());
        frameDelta += Gdx.graphics.getDeltaTime();

        decreaseHealth(1);
        if (getHealth() < 0) {
            remove();
        }
    }
    public void dispose() {
    }
}
