package hse.java.cr.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hse.java.cr.events.JoinRequestEvent;
import hse.java.cr.server.ServerFoundation;
import hse.java.cr.server.ServerGame;
import hse.java.cr.server.ServerGame.GameStatus;
import hse.java.cr.server.ServerPlayer;

import java.util.Arrays;
import java.util.Collection;

public class JoinListener extends Listener {
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof JoinRequestEvent) {
            final JoinRequestEvent joinRequestEvent = (JoinRequestEvent) object;
            ServerFoundation serverFoundation = ServerFoundation.INSTANCE;

            int playersCount = joinRequestEvent.playersCount;

            // get or create a game for `playersCount` gamers
            Collection<ServerGame> games = serverFoundation.getServerGames();

            ServerGame[] serverGames = new ServerGame[games.size()];
            serverGames = games.toArray(serverGames);

            ServerGame serverGame =
                    Arrays.stream(serverGames)
                            .parallel()
                            .filter(game -> game.players.items.length == playersCount
                                            && (game.gameStatus.equals(GameStatus.EMPTY)
                                            || game.gameStatus.equals(GameStatus.WAITING)))
                            .findAny()
                            .orElse(new ServerGame(playersCount));

            if (serverGame.gameStatus.equals(GameStatus.EMPTY)) {
                serverFoundation.addGame(serverGame);
            }

            serverGame.addPlayer(new ServerPlayer(
                    connection,
                    joinRequestEvent.username,
                    joinRequestEvent.screenWidth,
                    joinRequestEvent.screenHeight
            ));

            if (serverGame.increasePlayersCountAndGet() == playersCount) {
                serverGame.start();
            }
        }
    }
}
