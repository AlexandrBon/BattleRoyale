package hse.java.cr.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import hse.java.cr.events.JoinRequestEvent;
import hse.java.cr.events.JoinResponseEvent;
import hse.java.cr.server.GameServer;

public class JoinResponseListener extends Listener {
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof JoinResponseEvent) {
            final JoinResponseEvent joinResponseEvent = (JoinResponseEvent) object;
            Player.isLeft = joinResponseEvent.isLeft;
        }
    }
}
