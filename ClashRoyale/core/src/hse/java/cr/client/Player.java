package hse.java.cr.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import hse.java.cr.model.Character;
import hse.java.cr.model.GameField;
import hse.java.cr.model.GameInterface;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.wrappers.FontSizeHandler;

public class Player {
    public static String enemyUsername = "noname";
    public static boolean isLeft = true;
    public GameInterface gameInterface;
    public final Stage gameStage;
    public final GameField gameField;
    public final BitmapFont font;

    public Player(Assets assets) {
        gameStage = new Stage();
        gameInterface = new GameInterface(assets, gameStage);
        gameField = new GameField();
        font = FontSizeHandler.INSTANCE.getFont(Gdx.graphics.getHeight() / 15, Color.FIREBRICK);
    }

    public void addCharacter(String characterName, float x, float y) {
        gameStage.addActor(new Character(characterName, x, y, false));
    }

    public void draw(Batch batch, float delta) {
        gameStage.act();
        gameStage.draw();
        gameInterface.getCardInterface().getMana().draw(batch.getProjectionMatrix(), delta);
        batch.begin();
        font.draw(batch, enemyUsername,
                Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight());
        batch.end();
    }
}
