package hse.java.cr.server;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Server;
import hse.java.cr.network.Network;
import hse.java.cr.server.listeners.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerFoundation {
    public static ServerFoundation INSTANCE;
    private final Server server;
    private static final Map<Integer, ServerGame> serverGames = new HashMap<>();
    public static final ScheduledExecutorService executorService =
            Executors.newScheduledThreadPool(10);
    public static final Lock lock = new ReentrantLock();
    public static Condition gameIsEnd = lock.newCondition();
    public static int newGameId = 0;

    public Server getServer() {
        return server;
    }

    public Collection<ServerGame> getServerGames() {
        return serverGames.values();
    }

    public void addGame(ServerGame game) {
        serverGames.put(newGameId++, game);
    }

    public ServerGame getServerGame(int index) {
        return serverGames.get(index);
    }

    private ServerFoundation() {
        server = new Server();

        Network.register(server);

        server.addListener(new NewObjectListener());
        server.addListener(new JoinListener());
        server.addListener(new EventListener());
        server.addListener(new LeaveListener());
        server.addListener(new RemoveObjectListener());

        bindServer();
    }

    private void bindServer() {
        server.start();
        try {
            server.bind(54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Array<Integer> getEndGamesIndexes() {
        Array<Integer> endGamesIndexes = new Array<>(true, 16, Integer.class);
        for (ServerGame serverGame : serverGames.values()) {
            if (serverGame.gameStatus.equals(ServerGame.GameStatus.END)) {
                endGamesIndexes.add(serverGame.serverGameIndex);
            }
        }
        return endGamesIndexes;
    }

    private void cleanServerGames() throws InterruptedException {
        Array<Integer> endGamesIndexes;
        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            try {
                while ((endGamesIndexes = INSTANCE.getEndGamesIndexes()).isEmpty()) {
                    gameIsEnd.await();
                }
                for (Integer id : endGamesIndexes.items) {
                    if (id == null)
                        break;
                    serverGames.remove(id);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ServerFoundation.INSTANCE = new ServerFoundation();
        INSTANCE.cleanServerGames();
    }
}
