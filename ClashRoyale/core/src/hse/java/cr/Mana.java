package hse.java.cr;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Mana {
    public final ShapeRenderer manaBar;
    public static final int w = 400;
    public static final int h = 100;
    public int count = 0;

    public Mana() {
        manaBar = new ShapeRenderer();
    }
}