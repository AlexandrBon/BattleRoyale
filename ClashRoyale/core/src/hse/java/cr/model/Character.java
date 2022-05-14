package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
    private ShapeRenderer hpLine;

    private int attack = 1;
    private int maxHealth;
    private int health;
    private boolean myTeam;
    private boolean isRun = true;
    
    public Character(TextureAtlas golemAtlas, float x, float y, boolean team) {
        golemAnimations = new Array<>(State.values().length);
        if (team) {
            setPosition(100, 100);
            maxHealth = 100;
        } else {
            setPosition(1100, 100);
            maxHealth = 100;
        }
        //hpLine = new ShapeRenderer();
        health = maxHealth;
        curFrame = new Sprite();
        myTeam = team;
        state = State.HIT;

        int n = golemAnimations.size;
        n = 1; // TODO: add RUN and JUMP animation

        for (int i = 0; i < n; i++) {
            golemAnimations.add(new Animation<>(0.06f, golemAtlas.getRegions()));
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

    public Character(TextureAtlas golemAtlas, boolean team) {
       this(golemAtlas, 0, 0, team);
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
        moveBy(2, 0);
        if (health == 0) {
            remove();
        }
        curFrame = golemAnimations.get(state.ordinal()).getKeyFrame(frameDelta);

        batch.draw(curFrame, getX(),  getY(),
                getWidth() * getScaleX(), getHeight() * getScaleY());
        frameDelta += Gdx.graphics.getDeltaTime();

        decreaseHealth(1);
        if (getHealth() < 0) {
            remove();
        }
    }

    public void act(float delta) {
        if (health == 0) {
            return;
        }
        if (isRun) {
            for(Actor actor : getStage().getActors()) {
                if (actor instanceof Character) {
                    Character character = (Character) actor;
                    if (character != this && character.getHealth() > 0
                            && !character.myTeam && character.getY() == getY()
                            && Math.abs(getX() - character.getX()) <= 100) {
                        isRun = false;
                        currentOpponennt = character;
                        timer = 0;
                    }
                }
            }
            if (myTeam) {
                moveBy(1, 0);
            } else {
                moveBy(-1, 0);
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
        //hpLine.dispose();
    }
}
