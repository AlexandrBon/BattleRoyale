package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import org.jetbrains.annotations.NotNull;

public class Mana {
    private final ShapeRenderer manaBar;
    private final int width;
    private final int height;
    private int count = 0;
    private float timer;

    public Mana() {
        width = (int) (Gdx.graphics.getWidth() / 3f);
        height = (int) (Gdx.graphics.getHeight() / 15f);
        manaBar = new ShapeRenderer();
    }

    public void draw(Matrix4 combined, float delta) {
        manaBar.setProjectionMatrix(combined);
        manaBar.begin(ShapeRenderer.ShapeType.Filled);
        manaBar.setColor(Color.BLUE);
        manaBar.rect(0, 0, width * ((float)count / 10), height);
        manaBar.end();
        timer += delta;
        if (timer >= 1) {
            if (count < 10) {
                count++;
            }
            timer -= 1;
        }
    }

    /**
     * returns true if there is enough mana
     */
    public boolean decreaseMana(int count) {
        if (this.count  >= count) {
            this.count -= count;
            return true;
        } else {
            return false;
        }
    }

    public void dispose() {
        manaBar.dispose();
    }
}