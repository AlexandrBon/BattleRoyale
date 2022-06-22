package hse.java.cr.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.model.Character;
import hse.java.cr.model.GameField;
import hse.java.cr.model.GameInterface;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.wrappers.FontSizeHandler;

import java.security.PublicKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class ObjectManager {
    public final Map<Integer, Vector2> serverCharacterPositions = new HashMap<>();
}

public class Player {
    public static String enemyUsername = "noname";
    public static boolean isLeft = true;
    public GameInterface gameInterface;
    public final Stage gameStage;
    public final GameField gameField;
    public final BitmapFont font;
    public final ObjectManager objectManager;
    public static int gameIndex;
    public static int charactersCount;
    public float pastTime = 0f;

    public Player(Assets assets) {
        charactersCount = 0;
        objectManager = new ObjectManager();
        gameStage = new Stage();
        gameInterface = new GameInterface(assets, gameStage);
        gameField = new GameField();
        font = FontSizeHandler.INSTANCE.getFont(
                Gdx.graphics.getHeight() / 20,
                Color.FIREBRICK
        );
    }

    public void update(float delta) {
        Actor[] actors = gameStage.getActors().items;
        Vector2 characterPos = new Vector2();
        for (Actor actor : actors) {
            Character character;
            if (actor instanceof Character) {
                character = (Character) actor;
                pastTime += delta;
                characterPos.set(character.getX(), character.getY());
                int index = character.getIndex();
                if (objectManager.serverCharacterPositions.containsKey(index)) {
                    characterPos.interpolate(
                            objectManager.serverCharacterPositions.get(index),
                            0.1F,
                            Interpolation.linear
                    );
                    character.setPosition(characterPos.x, characterPos.y);
                }
            }
        }
    }

    public void addCharacter(String characterName, float x, float y) {
        gameStage.addActor(new Character(characterName, x, y, false, charactersCount++));
    }

    public void act(float delta) {
        update(delta);
        gameStage.act();
    }

    public void draw(Batch batch, float delta) {
        gameStage.draw();
        gameInterface.getCardInterface().getMana().draw(batch.getProjectionMatrix(), delta);
        {
            batch.begin();
            font.draw(batch, "username",
                    Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight());
            batch.end();
        }
    }
}
