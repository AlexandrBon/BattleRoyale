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
    private Vector2 position;
    private final Array<Animation<TextureRegion>> golemAnimations;
    private TextureRegion curFrame;
    private float frameDelta = 0f;

    private Character currentOpponennt;
    private float timer;

    private int attack = 1;
    private int maxHealth;
    private int health;
    private boolean myTeam;
    private boolean isRun = true;

    public Character(TextureAtlas golemAtlas, boolean team) {
        golemAnimations = new Array<>(State.values().length);
        if (team) {
            position = new Vector2(100, 100);
            maxHealth = 10;
        } else {
            position = new Vector2(1100, 100);
            maxHealth = 1;
        }
        health = maxHealth;
        curFrame = new Sprite();
        myTeam = team;
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

        setBounds(0, 0,
                textureWidth * getScaleX(),
                textureHeight * getScaleY());

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
        if (health == 0) {
            return;
        }
        curFrame = golemAnimations.get(state.ordinal()).getKeyFrame(frameDelta);
        batch.draw(curFrame, position.x,  position.y,
                getWidth() * getScaleX(), getHeight() * getScaleY());
        frameDelta += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void act(float delta) {
        if (health == 0) {
            return;
        }
        if (isRun) {
            for(Actor actor : this.getStage().getActors()) {
                if (actor instanceof Character) {
                    Character character = (Character) actor;
                    if (character != this && character.getHealth() > 0 && !character.myTeam && character.position.y == position.y && Math.abs(position.x - character.position.x) <= 100) {
                        isRun = false;
                        currentOpponennt = character;
                        timer = 0;
                    }
                }
            }
            if (myTeam) {
                position.x++;
            } else {
                position.x--;
            }
        } else {
            timer += delta;
            if (timer >= 1) {
                int opponentHealth = Math.max(0, currentOpponennt.getHealth() - attack);
                currentOpponennt.setHealth(opponentHealth);
                if (opponentHealth == 0) {
                    isRun = true;
                    currentOpponennt = null;
                }
                timer = 0;
            }
        }
    }

    public void dispose() {
    }
}
