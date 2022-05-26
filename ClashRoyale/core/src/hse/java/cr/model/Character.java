package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import hse.java.cr.client.Player;
import hse.java.cr.wrappers.Assets;

public class Character extends Actor {
    public enum State {
        HIT,
        RUN,
        JUMP
    }

    private State state;
    private final Array<Animation<TextureRegion>> golemAnimations;
    private TextureRegion curFrame;
    private float frameDelta = 0f;
    private Character currentOpponennt;
    private float timer;
    private Rectangle hpLine;

    private final boolean mySide;
    private int attack = 1;
    private int maxHealth;
    private int health;
    private boolean isRun = true;
    
    public Character(String characterName, float x, float y, boolean side) {
        TextureAtlas characterAtlas = Assets.stringToTextureAtlas(characterName);
        golemAnimations = new Array<>(State.values().length);
        setPosition(x, y);
        health = 100;
        curFrame = new Sprite();
        state = State.HIT;
        mySide = side;
        int n = golemAnimations.size;
        n = 1; // TODO: add RUN and JUMP animation

        for (int i = 0; i < n; i++) {
            golemAnimations.add(new Animation<>(0.06f, characterAtlas.getRegions()));
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
    public void act(float delta) {
        if (health == 0) {
            return;
        }
        if (isRun) {
            Array.ArrayIterator<Actor> actorIterator =
                    new Array.ArrayIterator<>(getStage().getActors());
            for(Actor actor : actorIterator) {
                if (actor instanceof Character) {
                    Character character = (Character) actor;
                    if (character != this && character.getHealth() > 0
                            && (character.mySide != mySide) && character.getY() == getY()
                            && Math.abs(getX() - character.getX()) <= 100) {
                        isRun = false;
                        currentOpponennt = character;
                        timer = 0;
                    }
                }
            }
            if (mySide) {
                moveBy(100 * delta, 0);
            } else {
                moveBy(-100 * delta, 0);
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        System.out.println("Draw: " + getX());
        if (health <= 0) {
            remove();
        }
        frameDelta += Gdx.graphics.getDeltaTime();
        curFrame = golemAnimations.get(state.ordinal()).getKeyFrame(frameDelta);

        batch.draw(curFrame, getX(),  getY(),
                (Player.isLeft ? 1 : -1) * getWidth() * getScaleX(), getHeight() * getScaleY());
    }

    public void dispose() {
    }
}
