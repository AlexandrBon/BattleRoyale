package hse.java.cr.client;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import hse.java.cr.model.Character;
import hse.java.cr.model.GameInterface;
import hse.java.cr.wrappers.Assets;

public class Player {
    public static String username;
    public static boolean isLeft;
    public GameInterface gameInterface;
    public final Stage gameStage;

    public Player(Assets assets) {
        gameStage = new Stage();
        gameInterface = new GameInterface(assets, gameStage);
    }

    public void addCharacter(String characterName, float x, float y) {
        addCharacter(Assets.stringToTextureAtlas(characterName), x, y);
    }

    public void addCharacter(TextureAtlas characterAtlas, float x, float y) {
        gameStage.addActor(new Character(characterAtlas, x, y, isLeft));
    }

    public void draw(Matrix4 combined, float delta) {
        gameStage.act();
        gameStage.draw();
        gameInterface.getCardInterface().getMana().draw(combined, delta);
    }
}
