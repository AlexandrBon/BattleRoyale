package hse.java.cr.server;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.kryonet.Connection;
import hse.java.cr.events.EnemyInfo;
import hse.java.cr.events.GameStartsEvent;
import hse.java.cr.events.PlayerUpdateEvent;
import hse.java.cr.events.PlayerUpdateEvent.ObjectState;
import hse.java.cr.events.StatusEvent;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static hse.java.cr.server.ServerPlayer.PlayerStatus;

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
    private long pastTime = System.nanoTime();
    private double delta = 0;

    public ServerGame(int playersCount) {
        gameStatus = GameStatus.EMPTY;
        players = new Array<>(true, playersCount, ServerPlayer.class);
        currentBattles = new Array<>(true, playersCount / 2, SimpleEntry.class);
        System.out.println("ServerGameConstructor");
    }

    public synchronized void start() {
        gameStatus = GameStatus.RUNNING;

        splitIntoPairs();

        // inform all players about the start of the game
        GameStartsEvent gameStartsEvent = new GameStartsEvent();
        gameStartsEvent.gameIndex = serverGameIndex;
        for (ServerPlayer player : players.items) {
            player.getConnection().sendTCP(gameStartsEvent);
        }

        ServerFoundation.executorService
                .scheduleAtFixedRate(this, 0, 100, TimeUnit.MILLISECONDS);

        ServerFoundation.executorService
                .scheduleAtFixedRate(this::checkAndUpdateGameState, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void splitIntoPairs() {
        currentBattles.clear();
        ServerPlayer first = null, second = null;
        for (ServerPlayer player : players.items) {
            boolean flag = player.getStatus().equals(PlayerStatus.EMPTY)
                    || player.getStatus().equals(PlayerStatus.WIN);
            if (first != null && flag) {
                second = player;
            } else if (flag) {
                first = player;
            }
            if (first != null && second != null) {
                currentBattles.add(new SimpleEntry<>(first, second));
                first = null;
                second = null;
            }
        }
        sendOpponentInfos();
    }

    private void sendOpponentInfos() {
        ServerPlayer player1, player2;
        EnemyInfo enemyInfo = new EnemyInfo();
        for (SimpleEntry<ServerPlayer, ServerPlayer>
                pair : currentBattles.items) {
            player1 = pair.getKey();
            player2 = pair.getValue();

            enemyInfo.enemyUsername = String.valueOf(player1.getName());
            enemyInfo.isLeft = true;
            assert player2 != null;
            player2.getConnection().sendTCP(enemyInfo);

            enemyInfo.enemyUsername = String.valueOf(player2.getName());
            enemyInfo.isLeft = false;
            player1.getConnection().sendTCP(enemyInfo);
        }
    }

    public void addPlayer(ServerPlayer player) {
        players.add(player);
        gameStatus = GameStatus.WAITING;
    }

    public ServerPlayer getPlayerByConnection(final Connection connection) {
        return Arrays.stream(players.items)
                .filter(player -> player != null && player.getConnection() == connection)
                .findAny()
                .orElse(null);
    }

    public ServerPlayer getEnemy(final ServerPlayer player) {
        for (SimpleEntry<ServerPlayer, ServerPlayer> currentBattle : currentBattles.items) {
            if (player == null) {
                continue;
            }
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
        for (SimpleEntry<ServerPlayer, ServerPlayer>
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

            player1.getConnection().sendTCP(player1UpdateEvent);
            player2.getConnection().sendTCP(player2UpdateEvent);
        }
    }

    private void checkAndUpdateGameState() {
        int empty = 0, win = 0, lose = 0;
        SnapshotArray<ServerPlayer> snapshotArray = new SnapshotArray<>(players.items);
        StatusEvent statusEvent = new StatusEvent();
        for (ServerPlayer player : snapshotArray) {
            switch (player.getStatus()) {
                case WIN: {
                    statusEvent.win = true;
                    player.getConnection().sendTCP(statusEvent);
                    player.getConnection().close();
                    win++;
                    break;
                }
                case LOSE: {
                    lose++;
                    statusEvent.win = false;
                    player.getConnection().sendTCP(statusEvent);
                    player.getConnection().close();
                    snapshotArray.removeValue(player, true);
                    break;
                }
                case EMPTY: {
                    empty++;
                    break;
                }
            }
        }
        if (win == 1 && lose == 1) {
            System.out.println("1 win and 1 lose");
            ServerFoundation.lock.lock();
            try {
                gameStatus = GameStatus.END;
                players.clear();
                System.out.println("signal");
                ServerFoundation.gameIsEnd.signal();
            } finally {
                ServerFoundation.lock.unlock();
            }
        }
        players.items = snapshotArray.items;
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
        double amountOfTicks = 60;
        long now = System.nanoTime();
        double nanoSeconds = 1e9 / amountOfTicks;
        delta += (now - pastTime) / nanoSeconds;
        pastTime = now;

        while (delta > 0) {
            tick();
            delta--;
        }
    }

    public int increasePlayersCountAndGet() {
        return ++currentPlayersCount;
    }
}
