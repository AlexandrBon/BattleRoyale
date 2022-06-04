package hse.java.cr.server;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.kryonet.Connection;

public class ServerPlayer {
    enum PlayerStatus {
        EMPTY,
        WIN,
        LOSE
    }

    private String name;
    private final Connection connection;
    private final Array<ServerCharacter> serverCharacters;
    private int newCharacterIndex;
    private PlayerStatus playerStatus;
    private int screenWidth, screenHeight;
    private int health;

    public ServerPlayer(Connection connection, int screenWidth, int screenHeight) {
        health = 3;
        playerStatus = PlayerStatus.EMPTY;
        serverCharacters = new Array<>(true, 16, ServerCharacter.class);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.connection = connection;
    }

    public Array<ServerCharacter> getServerCharacters() {
        return serverCharacters;
    }

    public void update() {
        if (health <= 0) {
            playerStatus = PlayerStatus.LOSE;
            return;
        }
        ServerCharacter[] characters = serverCharacters.toArray(ServerCharacter.class);
        boolean isLeft;
        float speed;
        for (int i = 0; i < characters.length; i++) {
            ServerCharacter character = characters[i];
            isLeft = character.isLeft();
            speed = character.getSpeed();
            character.addToX(isLeft ? speed : -speed);
            if (character.getX() >= screenWidth && character.isLeft()) {
                serverCharacters.removeIndex(i);
            } else if (character.getX() <= 0 && !character.isLeft()) {
                health--;
                serverCharacters.removeIndex(i);
            }
        }
    }

    public void resizeScreen(int screenWidth, int screenHeight) {
        float scaleX = (float) screenWidth / (float) this.screenWidth;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        changeCharactersSpeed(scaleX);
    }

    private void changeCharactersSpeed(float scaleX) {
        for (ServerCharacter character : serverCharacters.items) {
            character.setSpeed(character.getSpeed() * scaleX);
        }
    }

    public void addCharacter(ServerCharacter character) {
        serverCharacters.add(character);
    }

    public String getName() {
        return name;
    }

    public Connection getConnection() {
        return connection;
    }

    public PlayerStatus getStatus() {
        return playerStatus;
    }

    public void setStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getNewCharacterIndex() {
        return newCharacterIndex++;
    }
}
