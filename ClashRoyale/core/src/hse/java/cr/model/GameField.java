package hse.java.cr.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class GameField extends InputListener {
    private final int rectHeight = Gdx.graphics.getHeight() / 7;
    private final int rectWidth = Gdx.graphics.getWidth() / 2;
    private final int offsetY = Gdx.graphics.getHeight() / 6;
    private final int offsetX = 0;
    final int LINES_NUMBER = 3;
    private final ShapeRenderer shapeRenderer;

    public GameField() {
        shapeRenderer = new ShapeRenderer();
    }

    private int getTop() {
        return offsetY + rectHeight * LINES_NUMBER;
    }

    private int getRight() {
        return rectWidth + offsetX;
    }

    public void draw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.3f, 1f, 0.3f, 0.15f);
        for (int i = 0; i < LINES_NUMBER; i++) {
            shapeRenderer.rect(offsetX, offsetY + i * rectHeight, rectWidth, rectHeight);
        }
        drawFragmentLines();
    }

    private void drawFragmentLines() {
        shapeRenderer.setColor(0.3f, 1f, 0.3f, 0.3f);
        final int LINE_DIST_Y = rectHeight / 2;
        final int TOP_GAME_FIELD = offsetY + rectHeight * LINES_NUMBER;
        for (int i = offsetY; i <= TOP_GAME_FIELD; i += LINE_DIST_Y) {
            shapeRenderer.rectLine(offsetX, i, rectWidth, i, 2f);
        }

        final int LINE_DIST_X = rectWidth / 8;
        for (int i = offsetX; i <= getRight(); i += LINE_DIST_X) {
            shapeRenderer.rectLine(i, offsetY, i, TOP_GAME_FIELD, 2f);
        }
        shapeRenderer.end();
    }

    public boolean overlaps(float x, float y) {
        return x >= offsetX && x <= getRight()
                && y >= offsetY && y <= getTop();
    }
}
