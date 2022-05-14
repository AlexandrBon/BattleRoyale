package hse.java.cr.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import hse.java.cr.events.NewCharacterEvent;

public class NewObjectListener extends Listener {
    Server server;

    public NewObjectListener(Server server) {
        this.server = server;
    }

    @Override
    public void received (Connection connection, Object object) {
        if (object instanceof NewCharacterEvent) {
            NewCharacterEvent newCharacterEvent = (NewCharacterEvent) object;
            System.out.println("NewObjectListener");
            System.out.println(server.getConnections().length);
            if (server.getConnections().length > 0) {
                server.sendToAllTCP(newCharacterEvent);//TCP(server.getConnections()[0].getID(), newCharacterEvent);
            } else {
                System.out.println("ops");
            }
        }
    }
}
