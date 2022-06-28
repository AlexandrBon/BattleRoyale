package hse.java.cr.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hse.java.cr.events.NewCharacterEvent;
import hse.java.cr.events.RemoveObjectEvent;
import hse.java.cr.server.ServerCharacter;
import hse.java.cr.server.ServerFoundation;
import hse.java.cr.server.ServerGame;
import hse.java.cr.server.ServerPlayer;

public class RemoveObjectListener extends Listener {
    @Override
    public void received (Connection connection, Object object) {
        if (object instanceof RemoveObjectEvent) {
            RemoveObjectEvent character = (RemoveObjectEvent) object;
            ServerGame game = ServerFoundation.INSTANCE
                    .getServerGame(character.gameIndex);

            ServerPlayer player1 = game.getPlayerByConnection(connection);
            ServerPlayer player2 = game.getEnemy(player1);
            if (player1 == null || player2 == null) {
                return;
            }

            player1.removeCharacter(character.characterIndex);
            player2.removeCharacter(character.characterIndex);

            /*ServerFoundation.INSTANCE.getServer().sendToTCP(
                    player2.getConnection().getID(),
                    character
            );*/
        }
    }
}
