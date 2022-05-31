package hse.java.cr.server;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import hse.java.cr.events.EnemyInfo;
import hse.java.cr.events.GameStartsEvent;
import hse.java.cr.events.PlayerUpdateEvent;
import hse.java.cr.events.PlayerUpdateEvent.ObjectState;

import static hse.java.cr.server.ServerPlayer.PlayerStatus;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;

public class ServerGame implements Runnable {
    public enum GameStatus {
        EMPTY,
        WAITING,
        RUNNING,
        END
    }
    public GameStatus gameStatus;
    public int serverGameIndex;
    private int currentPlayersCount = 0;
    public Array<ServerPlayer> players;
    private final Array<SimpleEntry<ServerPlayer, ServerPlayer>> currentBattles;

    public ServerGame(int playersCount) {
        gameStatus = GameStatus.EMPTY;
        players = new Array<>(true, playersCount, ServerPlayer.class);
        currentBattles = new Array<>(true, playersCount / 2, SimpleEntry.class);
    }

    public synchronized void start() {
        gameStatus = GameStatus.RUNNING;

        splitIntoPairs();

        // inform all players about the start of the game
        GameStartsEvent gameStartsEvent = new GameStartsEvent();
        gameStartsEvent.gameIndex = serverGameIndex;
        for (ServerPlayer player : players.items) {
            sendToPlayer(player.getConnection(), gameStartsEvent);
        }
        final Thread thread = new Thread(this);
        thread.setName("UpdateThread");
        thread.start();
    }

    public void splitIntoPairs() {
        currentBattles.clear();
        ServerPlayer first = null, second = null;
        for (ServerPlayer player : players.items) {
            if (first != null && second != null) {
                currentBattles.add(new SimpleEntry<>(first, second));
                first = null;
                second = null;
                continue;
            }
            boolean flag = player.getStatus().equals(PlayerStatus.EMPTY)
                    || player.getStatus().equals(PlayerStatus.WIN);
            if (first != null && flag) {
                second = player;
            } else if (flag) {
                first = player;
            }
        }
        assert first != null && second != null;
        currentBattles.add(new SimpleEntry<>(first, second));
        sendOpponentInfos();
    }

    private void sendOpponentInfos() {
        ServerPlayer player1, player2;
        EnemyInfo enemyInfo = new EnemyInfo();
        for (SimpleEntry<ServerPlayer, ServerPlayer>
                pair : currentBattles.items) {
            player1 = pair.getKey();
            player2 = pair.getValue();

            enemyInfo.enemyUsername = player1.getName();
            enemyInfo.isLeft = true;
            sendToPlayer(player2.getConnection(), enemyInfo);

            enemyInfo.enemyUsername = player2.getName();
            enemyInfo.isLeft = false;
            sendToPlayer(player1.getConnection(), enemyInfo);
        }
    }

    public void addPlayer(ServerPlayer player) {
        players.add(player);
        gameStatus = GameStatus.WAITING;
    }

    private void sendToPlayer(Connection connection,
                                  Object object) {
        ServerFoundation.INSTANCE.getServer()
                .sendToTCP(connection.getID(), object);
    }

    public ServerPlayer getPlayerByConnection(final Connection connection) {
        return Arrays.stream(players.items)
                .filter(player -> player.getConnection() == connection)
                .findAny()
                .orElse(null);
    }

    public ServerPlayer getEnemy(final ServerPlayer player) {
        for (SimpleEntry<ServerPlayer, ServerPlayer> currentBattle : currentBattles.items) {
            if (player.equals(currentBattle.getKey())) {
                return currentBattle.getValue();
            } else if (player.equals(currentBattle.getValue())) {
                return currentBattle.getKey();
            }
        }
        return null;
    }

    public void tick() {
        ServerPlayer player1, player2;
        for(SimpleEntry<ServerPlayer, ServerPlayer>
                pair : currentBattles.items) {
            if (pair == null) {
                break;
            }
            player1 = pair.getKey();
            player2 = pair.getValue();
            // Update server player
            player1.update();
            player2.update();

            // Send update to all battles
            final PlayerUpdateEvent player1UpdateEvent = getPlayerUpdateEvent(player1);
            final PlayerUpdateEvent player2UpdateEvent = getPlayerUpdateEvent(player2);

            sendToPlayer(player1.getConnection(), player1UpdateEvent);
            sendToPlayer(player2.getConnection(), player2UpdateEvent);
        }
    }

    private PlayerUpdateEvent getPlayerUpdateEvent(ServerPlayer player) {
        final PlayerUpdateEvent updateEvent = new PlayerUpdateEvent();
        updateEvent.objectStates
                = new ObjectState[player.getServerCharacters().size];

        int i = 0;
        for (ServerCharacter character : player.getServerCharacters().items) {
            if (character == null) {
                break;
            }
            updateEvent.objectStates[i++] = character.getState();
        }
        return updateEvent;
    }

    @Override
    public void run() {
        long pastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (gameStatus.equals(GameStatus.RUNNING)) {
            try {
                Thread.sleep((long) (60F / amountOfTicks));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long now = System.nanoTime();
            delta += (now - pastTime) / ns;
            pastTime = now;

            while (delta > 0) {
                tick();
                delta--;
            }
        }


    }

    public int getCurrentPlayersCount() {
        return currentPlayersCount;
    }

    public int increasePlayersCount() {
        return ++currentPlayersCount;
    }
}
