package hse.java.cr.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import hse.java.cr.events.JoinRequestEvent;
import hse.java.cr.events.JoinResponseEvent;

public class JoinListener extends Listener {
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof JoinRequestEvent) {
            final JoinRequestEvent joinRequestEvent = (JoinRequestEvent) object;
            Server server = GameServer.instance.server;

            final JoinResponseEvent joinResponseEvent = new JoinResponseEvent();
            int connectionId = GameServer.instance.server.getConnections().length % 2;
            joinResponseEvent.isLeft = (connectionId == 1);

            server.sendToTCP(server.getConnections()[(connectionId + 1) % 2].getID(), joinResponseEvent);
        }
    }
}
