package hse.java.cr.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.client.Player;
import hse.java.cr.client.Starter;
import hse.java.cr.events.RemoveObjectEvent;
import hse.java.cr.wrappers.Assets;

public class Character extends Actor {
    public enum State {
        WALKING,
        ATTACKING
    }

    private static final int maxHealth = 100;

    private int characterIndex;
    private State state;

    private final Array<Animation<TextureRegion>> golemAnimations;
    private TextureRegion curFrame;
    private float frameDelta = 0f;
    private Character currentOpponent;
    private float timer;

    private final boolean mySide;
    private final int attack;

    private final float deltaX;
    private int health;
    private boolean isRun = true;

    public Character(String characterName, float x, float y, boolean side, int index) {
        characterIndex = index;
        attack = 50;
        health = 100;

        golemAnimations = new Array<>(true, State.values().length, Animation.class);

        curFrame = new Sprite();
        state = State.WALKING;
        mySide = side;

        TextureAtlas characterWalkingAtlas = Assets.getTextureAtlas(characterName + "Walking");
        TextureAtlas characterAttackingAtlas = Assets.getTextureAtlas(characterName + "Attacking");

        golemAnimations.add(new Animation<>(0.06f, characterWalkingAtlas.getRegions()));
        golemAnimations.get(0).setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        golemAnimations.add(new Animation<>(0.06f, characterAttackingAtlas.getRegions()));
        golemAnimations.get(1).setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        float textureWidth = golemAnimations.get(0).getKeyFrame(0).getRegionWidth();
        float textureHeight = golemAnimations.get(0).getKeyFrame(0).getRegionHeight();

        float scale = Gdx.graphics.getHeight() / 8f / textureHeight;

        setScale(scale, scale);
        setBounds(x, y, textureWidth * getScaleX(),
                textureHeight * getScaleY());

        deltaX = getWidth() * 0.01f;
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


    public void setIndex(int index) {
        characterIndex = index;
    }

    public int getIndex() {
        return characterIndex;
    }

    @Override
    public void act(float delta) {
        float eps = Gdx.graphics.getWidth();
        if (health <= 0 || getX() < -eps || getX() > Gdx.graphics.getWidth() + eps) {
            remove();
            return;
        }
        if (isRun) {
            Actor[] actors = getStage().getActors().toArray();
            for (Actor actor : actors) {
                if (actor instanceof Character) {
                    Character character = (Character) actor;
                    if (character != this && character.getHealth() > 0
                            && getY() == character.getY()
                            && (character.mySide != mySide)
                            && Math.abs(getX() - character.getX()) <= 160) {
                        state = State.ATTACKING;
                        character.state = State.ATTACKING;
                        isRun = false;
                        currentOpponent = character;
                        timer = 0;
                    }
                }
            }
            if (!isRun) {
                if (mySide) {
                    moveBy(deltaX, 0);
                } else {
                    moveBy(-deltaX, 0);
                }
            }
        } else {
            timer += delta;
            if (timer >= 1) {
                int opponentHealth = Math.max(0, currentOpponent.getHealth() - attack);
                currentOpponent.setHealth(opponentHealth);
                if (opponentHealth == 0) {
                    isRun = true;
                    currentOpponent.remove();
                    {
                        RemoveObjectEvent removeObjectEvent = new RemoveObjectEvent();
                        removeObjectEvent.gameIndex = Player.gameIndex;
                        removeObjectEvent.characterIndex = currentOpponent.characterIndex;
                        Starter.getClient().sendTCP(removeObjectEvent);
                    }
                    state = State.WALKING;
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
