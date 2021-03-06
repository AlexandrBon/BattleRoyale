package hse.java.cr.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hse.java.cr.events.NewCharacterEvent;
import hse.java.cr.events.PlayerUpdateEvent;
import hse.java.cr.events.PlayerUpdateEvent.ObjectState;
import hse.java.cr.events.StatusEvent;

import java.util.Map;

public class EventListener extends Listener {
    Player player;

    public EventListener(Player player) {
        this.player = player;
    }

    @Override
    public void received (Connection connection, Object object) {
        if (object instanceof NewCharacterEvent) {
            NewCharacterEvent newCharacterEvent = (NewCharacterEvent) object;
            player.addCharacter(
                    newCharacterEvent.characterName,
                    Gdx.graphics.getWidth() - newCharacterEvent.x,
                    newCharacterEvent.y
            );
        } else if (object instanceof PlayerUpdateEvent) {
            PlayerUpdateEvent playerUpdateEvent = (PlayerUpdateEvent) object;
            Map<Integer, Vector2> map = player.serverCharacterPositions;
            ObjectState[] objectStates = playerUpdateEvent.objectStates;
            for (ObjectState state : objectStates) {
                assert state != null;
                map.put(state.index, new Vector2(state.x, state.y));
            }
        } else if (object instanceof StatusEvent) {
            player.setStatus(
                    ((StatusEvent) object).win
                            ? Player.Status.WIN
                            : Player.Status.LOSE
            );
        }
    }
}
