package hse.java.cr.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hse.java.cr.events.ScreenResizeEvent;
import hse.java.cr.server.ServerFoundation;
import hse.java.cr.server.ServerGame;

public class EventListener extends Listener {
    public void received (Connection connection, Object object) {
        if (object instanceof ScreenResizeEvent) {
            ScreenResizeEvent resizeEvent = (ScreenResizeEvent) object;
            ServerGame serverGame = ServerFoundation.INSTANCE
                    .getServerGame(resizeEvent.gameIndex);

            serverGame.getPlayerByConnection(connection)
                    .resizeScreen(resizeEvent.screenWidth, resizeEvent.screenHeight);
        }
    }
}
