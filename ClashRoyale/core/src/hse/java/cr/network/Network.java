package hse.java.cr.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import hse.java.cr.events.*;
import org.jetbrains.annotations.NotNull;

public class Network {
    static public void register (@NotNull EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(NewCharacterEvent.class);
        kryo.register(JoinRequestEvent.class);
        kryo.register(EnemyInfo.class);
        kryo.register(GameStartsEvent.class);
        kryo.register(PlayerUpdateEvent.class);
        kryo.register(PlayerUpdateEvent.ObjectState.class);
        kryo.register(PlayerUpdateEvent.ObjectState[].class);
        kryo.register(StatusEvent.class);
        kryo.register(RemoveObjectEvent.class);
    }
}