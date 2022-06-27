package hse.java.cr.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Map;

public class Spell extends Actor {
    private TextureRegion curFrame;
    private float frameDelta = 0f;
    private final float radius;
    private final boolean myTeam;
    private float lifeTime;
    private final String typeOfSpell;
    private final int attack;
    Map<Character, Float> times;
    private final Animation<TextureRegion> spellAnimation;

    public Spell(TextureAtlas atlas, float x, float y, boolean team) {
        typeOfSpell = "burst";
        myTeam = team;
        Vector2 position = new Vector2();
        position.x = x;
        position.y = y;
        attack = 1;

        //TODO: normal radius and lifeTime;
        radius = 100;
        lifeTime = 1000;

        curFrame = new Sprite();
        spellAnimation = new Animation<>(0.06f, atlas.getRegions());
        spellAnimation.setPlayMode(Animation.PlayMode.LOOP);

        float textureWidth = spellAnimation.getKeyFrame(0).getRegionWidth();
        float textureHeight = spellAnimation.getKeyFrame(0).getRegionHeight();

        float height = Gdx.graphics.getHeight() / 3f;
        float width = height * textureWidth / textureHeight;

        setScale(width / textureWidth, height / textureHeight);

        setBounds(x, y, textureWidth * getScaleX(),
                textureHeight * getScaleY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        lifeTime--;
        if (lifeTime <= 0) {
            remove();
        }
        curFrame = spellAnimation.getKeyFrame(frameDelta);

        batch.draw(curFrame, getX(),  getY(),
                (myTeam ? 1 : -1) * getWidth() * getScaleX(), getHeight() * getScaleY());
        frameDelta += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Actor[] actors = getStage().getActors().items;
        for (Actor actor : actors) {
            if (actor instanceof Character) {
                Character character = (Character) actor;
                String typeOfSpellBurst = "burst";
                String typeOfSpellHeal = "heal";
                if ((character.getMySide() != myTeam && typeOfSpell.equals(typeOfSpellBurst))
                        || (character.getMySide() == myTeam && typeOfSpell.equals(typeOfSpellHeal))
                        && (Math.abs(character.getX() - getX()) <= radius)) {
                    if (!times.containsKey(character)) {
                        times.put(character, 1.5f);
                    }
                    if (times.get(character) >= 1.0f) {
                        times.put(character, 0f);
                        if (typeOfSpell.equals(typeOfSpellBurst)) {
                            character.setHealth(Math.min(character.getHealth() - attack, 0));
                        }
                        if (typeOfSpell.equals(typeOfSpellHeal)) {
                            character.setHealth(Math.max(character.getHealth() + attack, character.getMaxHealth()));
                        }
                    }
                    times.put(character, times.get(character) + delta);
                }
            }
        }
    }
}
