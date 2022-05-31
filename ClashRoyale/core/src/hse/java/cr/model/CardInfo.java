package hse.java.cr.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CardInfo {
    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public String getTypeOfCard() {
        return typeOfCard;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getRadious() {
        return radious;
    }

    private final Animation<TextureRegion> animation;
    private final String typeOfCard;
    private final String name;
    private final int hp;
    private final int attack;
    private final int radious;

    public CardInfo(TextureAtlas atlas, String name, String typeOfCard, int hp, int attack, int radious) {
        animation = new Animation<>(0.06f, atlas.getRegions());
        animation.setPlayMode(Animation.PlayMode.LOOP);
        this.typeOfCard = typeOfCard;
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.radious = radious;
    }
}
