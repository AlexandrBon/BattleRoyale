package hse.java.cr.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.wrappers.Assets;

public class Character extends Actor {
    public enum State {
        HIT,
        RUN,
        JUMP
    }

    private static final int maxHealth = 100;

    private int characterIndex;
    private final State state;

    private final Array<Animation<TextureRegion>> golemAnimations;
    private TextureRegion curFrame;
    private float frameDelta = 0f;
    private Character currentOpponennt;
    private float timer;

    private final boolean mySide;
    private int attack = 1;

    float deltaY = getHeight() * 0.01f;
    float deltaX = getWidth() * 0.1f;
    private int health;
    private boolean isRun = true;
    
    public Character(String characterName, float x, float y, boolean side, int index) {
        characterIndex = index;
        TextureAtlas characterAtlas = Assets.getTextureAtlas(characterName);
        golemAnimations = new Array<>(State.values().length);
        attack = 1;
        health = 100;
        curFrame = new Sprite();
        state = State.HIT;
        mySide = side;
        int n = golemAnimations.size;
        n = 1; // TODO: add RUN and JUMP animation

        for (int i = 0; i < n; i++) {
            golemAnimations.add(new Animation<>(0.06f, characterAtlas.getRegions()));
            golemAnimations.get(i).setPlayMode(Animation.PlayMode.LOOP_REVERSED);
            // TODO: JUMP PlayMode is .NORMAL
        }

        float textureWidth = golemAnimations.get(0).getKeyFrame(0).getRegionWidth();
        float textureHeight = golemAnimations.get(0).getKeyFrame(0).getRegionHeight();

        float scale = Gdx.graphics.getHeight() / 8f / textureHeight;
        setScale(scale, scale);
        setBounds(x, y, textureWidth * getScaleX(),
                textureHeight * getScaleY());
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean getMySide() {
        return mySide;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void decreaseHealth(int damage) {
        this.health -= damage;
    }

    public void setIndex(int index) {
        characterIndex = index;
    }

    public int getIndex() {
        return characterIndex;
    }

    @Override
    public void act(float delta) {
        if (health <= 0 || getX() < 0 || getX() > Gdx.graphics.getWidth()) {
            remove();
            return;
        }
        if (isRun) {
            //getStage().setDebugAll(true);
            Actor[] actors = getStage().getActors().toArray();
            for(Actor actor : actors) {
                if (actor instanceof Character) {
                    Character character = (Character) actor;
                    if (character != this && character.getHealth() > 0
                            && (character.mySide != mySide) && Math.abs(character.getY() - getY()) < deltaY
                            && Math.abs(getX() - character.getX()) <= 100) {
                        isRun = false;
                        currentOpponennt = character;
                        timer = 0;
                    }
                }
            }
            if (mySide) {
                moveBy(deltaX, 0);
            } else {
                moveBy(-deltaX, 0);
            }
        } else {
            timer += delta;
            if (timer >= 1) {
                int opponentHealth = Math.max(0, currentOpponennt.getHealth() - attack);
                currentOpponennt.setHealth(opponentHealth);
                if (opponentHealth == 0) {
                    isRun = true;
                    currentOpponennt.remove();
                }
                timer = 0;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        frameDelta += Gdx.graphics.getDeltaTime();
        curFrame = golemAnimations.get(state.ordinal()).getKeyFrame(frameDelta);
        batch.draw(curFrame, getX(), getY(),
                (mySide ? 1 : -1) * curFrame.getRegionWidth() * getScaleX(),
                curFrame.getRegionHeight() * getScaleY());
    }

    public void dispose() {
    }
}
