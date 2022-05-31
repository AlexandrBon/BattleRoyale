package hse.java.cr.server;

import com.esotericsoftware.kryonet.Server;

import hse.java.cr.network.Network;
import hse.java.cr.server.listeners.JoinListener;
import hse.java.cr.server.listeners.NewObjectListener;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServerFoundation {
    public static ServerFoundation INSTANCE;
    private final Server server;
    private static final Map<Integer, ServerGame> serverGames = new HashMap<>();

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

    public static void main(String[] args) {
        ServerFoundation.INSTANCE = new ServerFoundation();
    }
}
