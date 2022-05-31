package hse.java.cr.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hse.java.cr.events.NewCharacterEvent;
import hse.java.cr.server.ServerCharacter;
import hse.java.cr.server.ServerFoundation;
import hse.java.cr.server.ServerGame;
import hse.java.cr.server.ServerPlayer;

public class NewObjectListener extends Listener {
    @Override
    public void received (Connection connection, Object object) {
        if (object instanceof NewCharacterEvent) {
            NewCharacterEvent character = (NewCharacterEvent) object;
            ServerGame game = ServerFoundation.INSTANCE
                    .getServerGame(character.gameIndex);
            ServerPlayer player1 = game.getPlayerByConnection(connection);
            ServerPlayer player2 = game.getEnemy(player1);

            player1.getServerCharacters().add(new ServerCharacter(
                    player1.getServerCharacters().size,
                    character.x,
                    character.y,
                    true
            ));

            player2.getServerCharacters().add(new ServerCharacter(
                    player2.getServerCharacters().size,
                    800 - character.x,
                    character.y,
                    false
            ));

            ServerFoundation.INSTANCE.getServer().sendToTCP(
                    player2.getConnection().getID(),
                    character
            );
        }
    }
}
