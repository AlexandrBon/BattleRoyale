package hse.java.cr.server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import hse.java.cr.events.NewCharacterEvent;

public class NewObjectListener extends Listener {
    @Override
    public void received (Connection connection, Object object) {
        if (object instanceof NewCharacterEvent) {
            NewCharacterEvent newCharacterEvent = (NewCharacterEvent) object;
            Server server = GameServer.instance.server;
            for (Connection c : server.getConnections()) {
                if (c.getID() != connection.getID()) {
                    server.sendToTCP(c.getID(), newCharacterEvent);
                }
            }
        }
    }
}
