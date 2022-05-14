package hse.java.cr.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import hse.java.cr.events.JoinRequestEvent;
import hse.java.cr.events.JoinResponseEvent;
import hse.java.cr.events.NewCharacterEvent;
import org.jetbrains.annotations.NotNull;

public class Network {
    static public void register (@NotNull EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(NewCharacterEvent.class);
        kryo.register(JoinRequestEvent.class);
        kryo.register(JoinResponseEvent.class);
    }
}