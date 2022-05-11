package hse.java.cr.server;

import com.esotericsoftware.kryonet.Server;
import hse.java.cr.network.Network;

import java.io.IOException;

public class GameServer {
    public static GameServer instance;
    Server server;

    public GameServer() {
        server = new Server();
        Network.register(server);
        // TODO: add listeners
        bindServer();
    }

    private void bindServer() {
        server.start();

        try {
            server.bind(6334, 6334);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameServer.instance = new GameServer();
    }
}
