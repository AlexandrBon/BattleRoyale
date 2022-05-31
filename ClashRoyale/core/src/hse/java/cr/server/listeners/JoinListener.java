package hse.java.cr.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import hse.java.cr.events.JoinRequestEvent;
import hse.java.cr.server.ServerFoundation;
import hse.java.cr.server.ServerGame;
import hse.java.cr.server.ServerPlayer;

import java.util.Arrays;
import java.util.Collection;

public class JoinListener extends Listener {
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof JoinRequestEvent) {
            final JoinRequestEvent joinRequestEvent = (JoinRequestEvent) object;
            Server server = ServerFoundation.INSTANCE.getServer();

            int playersCount = joinRequestEvent.playersCount;

            // get or create a game for `playersCount` gamers
            Collection<ServerGame> games = ServerFoundation.INSTANCE.getServerGames();
            ServerGame[] serverGames = new ServerGame[games.size()];
            serverGames = games.toArray(serverGames);

            ServerGame serverGame =
                    Arrays.stream(serverGames)
                            .parallel()
                            .filter(game -> game.players.items.length == playersCount)
                            .findAny()
                            .orElse(new ServerGame(playersCount));

            if (serverGame.gameStatus.equals(ServerGame.GameStatus.EMPTY)) {
                ServerFoundation.INSTANCE.addGame(serverGame);
            }
            serverGame.addPlayer(new ServerPlayer(connection));

            if (serverGame.increasePlayersCount() == playersCount) {
                serverGame.start();
            }
        }
    }
}
