package hse.java.cr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

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

    public Golem(String pathToTextureAtlas) {
        golemAnimations = new Array<>(State.values().length);
        position = new Vector2(0, 0);
        curFrame = new TextureRegion();
        state = State.HIT;
        TextureAtlas atlas = new TextureAtlas(pathToTextureAtlas);

        int n = golemAnimations.size;
        n = 1; // TODO: add RUN and JUMP animation

        for (int i = 0; i < n; i++) {
            golemAnimations.add(new Animation<>(0.07f,
                    atlas.findRegions("0_Golem_Run Slashing")));
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        curFrame = golemAnimations.get(state.ordinal()).getKeyFrame(frameDelta);
        batch.draw(curFrame, position.x, position.y);
        frameDelta += Gdx.graphics.getDeltaTime();
    }

    public void dispose() {
    }
}
