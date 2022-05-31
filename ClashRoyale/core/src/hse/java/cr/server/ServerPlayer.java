package hse.java.cr.server;

import com.badlogic.gdx.utils.Array;
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
    private PlayerStatus playerStatus;
    private int screenWidth, screenHeight;
    private int score;

    public ServerPlayer(Connection connection) {
        score = 0;
        playerStatus = PlayerStatus.EMPTY;
        serverCharacters = new Array<>(true, 16, ServerCharacter.class);
        this.connection = connection;
    }

    public Array<ServerCharacter> getServerCharacters() {
        return serverCharacters;
    }

    public void update() {
        ServerCharacter[] characters = serverCharacters.toArray(ServerCharacter.class);
        boolean isLeft;
        float speed;
        for (ServerCharacter character : characters) {
            isLeft = character.isLeft();
            speed = character.getSpeed();
            character.addToX(isLeft ? speed : -speed);
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
}
