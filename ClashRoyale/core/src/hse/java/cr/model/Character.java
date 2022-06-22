package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import hse.java.cr.client.Player;
import hse.java.cr.client.Starter;
import hse.java.cr.wrappers.Assets;
import jdk.swing.interop.SwingInterOpUtils;

public class Character extends Actor {
    public enum State {
        HIT,
        RUN,
        JUMP
    }

    private int characterIndex;
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
    
    public Character(String characterName, float x, float y, boolean side, int index) {
        characterIndex = index;
        TextureAtlas characterAtlas = Assets.stringToTextureAtlas(characterName);
        golemAnimations = new Array<>(State.values().length);

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
        System.out.println(textureWidth + " " + textureHeight);
        float scale = Gdx.graphics.getHeight() / 8f / textureHeight;
        setScale(scale, scale);
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

    public void setIndex(int index) {
        characterIndex = index;
    }

    public int getIndex() {
        return characterIndex;
    }

    @Override
    public void act(float delta) {
        if (health == 0) {
            return;
        }
        if (isRun) {
            Actor[] actors = getStage().getActors().items;
            for(Actor actor : actors) {
                if (actor instanceof Character) {
                    Character character = (Character) actor;
                    if (character != this && character.getHealth() > 0
                            && (character.mySide != mySide) && Math.abs(character.getY() - getY()) < 20
                            && Math.abs(getX() - character.getX()) <= 100) {
                        isRun = false;
                        currentOpponennt = character;
                        timer = 0;
                    }
                }
            }
            if (Starter.getClient() == null) {
                if (mySide) {
                    moveBy(1, 0);
                } else {
                    moveBy(-1, 0);
                }
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
        if (health <= 0) {
            remove();
        }
        frameDelta += Gdx.graphics.getDeltaTime();
        curFrame = golemAnimations.get(state.ordinal()).getKeyFrame(frameDelta);
        batch.draw(curFrame, getX(), getY(),
                (mySide ? 1 : -1) * curFrame.getRegionWidth() * getScaleX(),
                curFrame.getRegionHeight() * getScaleY());
    }

    public void dispose() {
    }
}
