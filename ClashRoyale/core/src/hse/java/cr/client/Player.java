package hse.java.cr.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hse.java.cr.buttons.UIButton;
import hse.java.cr.client.model.Character;
import hse.java.cr.client.model.GameInterface;
import hse.java.cr.client.screens.MainScreen;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.wrappers.FontSizeHandler;

import java.util.HashMap;
import java.util.Map;

public class Player {
    public enum Status {
        WIN,
        LOSE,
        WAITING_NEW_GAME,
        EMPTY
    }

    public static String enemyNickname;
    public static boolean isLeft = true;
    public GameInterface gameInterface;
    private final Stage gameStage;
    public static int gameIndex;
    public static int charactersCount;
    public final Map<Integer, Vector2> serverCharacterPositions;
    public static Status status;

    public Player(Assets assets, MainScreen mainScreen) {
        status = Status.EMPTY;
        charactersCount = 0;
        serverCharacterPositions = new HashMap<>();
        gameStage = new Stage();
        gameInterface = new GameInterface(assets, gameStage, mainScreen, enemyNickname);

        Gdx.input.setInputProcessor(gameStage);
    }

    public void setStatus(Status status) {
        Player.status = status;
        gameInterface.updateGameState(status);
    }

    public void updateCharacters(float delta) {
        Actor[] actors = gameStage.getActors().items;
        Vector2 characterPos = new Vector2();
        for (Actor actor : actors) {
            Character character;
            if (actor instanceof Character) {
                character = (Character) actor;
                characterPos.set(character.getX(), character.getY());
                int index = character.getIndex();
                if (serverCharacterPositions.containsKey(index)) {
                    characterPos.interpolate(
                            serverCharacterPositions.get(index),
                            0.05F,
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
        updateCharacters(delta);
        gameInterface.act(delta);
    }

    public void draw(Batch batch, float delta) {
        gameInterface.draw(batch, delta);
    }
}
