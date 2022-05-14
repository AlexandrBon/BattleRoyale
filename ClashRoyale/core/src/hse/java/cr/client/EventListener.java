package hse.java.cr.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hse.java.cr.events.NewCharacterEvent;

public class EventListener extends Listener {
    Player player;

    public EventListener(Player player) {
        this.player = player;
    }

    @Override
    public void received (Connection connection, Object object) {
        if (object instanceof NewCharacterEvent) {
            NewCharacterEvent newCharacterEvent = (NewCharacterEvent) object;
            player.addCharacter(newCharacterEvent.characterName,
                    newCharacterEvent.x,
                    newCharacterEvent.y);
        }
    }
}
