package hse.java.cr.server;

import com.esotericsoftware.kryonet.Server;

import hse.java.cr.network.Network;
import hse.java.cr.server.listeners.JoinListener;
import hse.java.cr.server.listeners.NewObjectListener;

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
            Executors.newScheduledThreadPool(8);

    public Server getServer() {
        return server;
    }

    public Collection<ServerGame> getServerGames() {
        return serverGames.values();
    }

    public void addGame(ServerGame game) {
        serverGames.put(serverGames.size(), game);
    }

    public ServerGame getServerGame(int index) {
        return serverGames.get(index);
    }

    private ServerFoundation() {
        server = new Server();

        Network.register(server);

        server.addListener(new NewObjectListener());
        server.addListener(new JoinListener());

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

    public static void main(String[] args) throws InterruptedException {
        ServerFoundation.INSTANCE = new ServerFoundation();
        /*while (true) {
            Thread.currentThread().wait();

        }*/
    }
}
