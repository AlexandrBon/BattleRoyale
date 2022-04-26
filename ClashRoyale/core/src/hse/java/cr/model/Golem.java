package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.Assets;

import java.util.Iterator;
import java.util.Random;

public class Golem extends Actor {
    public enum State {
        HIT,
        RUN,
        JUMP
    }

    private State state;
    private Vector2 position;
    private int health;
    private final Array<Animation<TextureRegion>> golemAnimations;
    private TextureRegion curFrame;
    private float frameDelta = 0f;

    public Golem(TextureAtlas golemAtlas) {
        golemAnimations = new Array<>(State.values().length);
        position = new Vector2(0, 0);
        curFrame = new Sprite();
        state = State.HIT;

        int n = golemAnimations.size;
        n = 1; // TODO: add RUN and JUMP animation

        Array<Animation<TextureRegion>> golemAnimationsTemp = new Array<>();
        for (int i = 0; i < n; i++) {
            golemAnimations.add(new Animation<>(0.07f, golemAtlas.getRegions()));
            golemAnimations.get(i).setPlayMode(Animation.PlayMode.LOOP);
            // TODO: JUMP PlayMode is .NORMAL
        }


    }

    public Vector2 getPosition() {
        return position;
    }

    public int getHealth() {
        return health;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        curFrame = golemAnimations.get(state.ordinal()).getKeyFrame(frameDelta);
        batch.draw(curFrame, position.x,  position.y, 100, 100);
        frameDelta += Gdx.graphics.getDeltaTime();
    }
    public void dispose() {
    }
}
