package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Spell extends Actor {
    private TextureRegion curFrame;
    private float frameDelta = 0f;
    private boolean myTeam;
    private float lifiTime = 1000;
    private Vector2 position;
    private Animation<TextureRegion> spellAnimation;

    public Spell(TextureAtlas atlas, float x, float y, boolean team) {
        myTeam = team;
        position = new Vector2();
        position.x = x;
        position.y = y;

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
        lifiTime--;
        if (lifiTime <= 0) {
            remove();
        }
        curFrame = spellAnimation.getKeyFrame(frameDelta);

        batch.draw(curFrame, getX(),  getY(),
                (myTeam ? 1 : -1) * getWidth() * getScaleX(), getHeight() * getScaleY());
        frameDelta += Gdx.graphics.getDeltaTime();
    }
}
