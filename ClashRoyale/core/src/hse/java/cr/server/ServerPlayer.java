package hse.java.cr.server;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;

public class ServerPlayer {
    enum PlayerStatus {
        EMPTY,
        WIN,
        LOSE
    }

    private final String name;
    private final Connection connection;
    private final Array<ServerCharacter> serverCharacters;
    private int newCharacterIndex;
    private PlayerStatus playerStatus;
    private int screenWidth, screenHeight;
    private int health;
    private int score;

    private static final int MAX_SCORE = 1;
    private static final int MIN_HEALTH = 0;

    public ServerPlayer(Connection connection, String name, int screenWidth, int screenHeight) {
        health = 1;
        score = 0;
        playerStatus = PlayerStatus.EMPTY;
        serverCharacters = new Array<>(true, 16, ServerCharacter.class);
        this.name = name;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.connection = connection;
    }

    public Array<ServerCharacter> getServerCharacters() {
        return serverCharacters;
    }

    public void update() {
        if (health <= MIN_HEALTH) {
            playerStatus = PlayerStatus.LOSE;
            return;
        } else if (score >= MAX_SCORE) {
            playerStatus = PlayerStatus.WIN;
            return;
        }
        ServerCharacter[] characters = serverCharacters.toArray(ServerCharacter.class);
        boolean isLeft;
        float speed;
        for (int i = 0; i < characters.length; i++) {
            ServerCharacter character = characters[i];
            isLeft = character.isMine();
            speed = character.getSpeed();
            character.addToX(isLeft ? speed : -speed);
            int eps = (int) (5 * speed);
            if (character.getX() < -eps || character.getX() > screenWidth + eps) {
                if (!character.isMine()) {
                    health--;
                } else {
                    score++;
                }
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
