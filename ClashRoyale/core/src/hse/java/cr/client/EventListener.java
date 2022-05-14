package hse.java.cr.client;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.events.NewCharacterEvent;
import hse.java.cr.model.Character;

public class EventListener extends Listener {
    Stage gameStage;
    Assets assets;

    public EventListener(Stage gameStage, Assets assets) {
        this.gameStage = gameStage;
        this.assets = assets;
    }

    @Override
    public void received (Connection connection, Object object) {
        if (object instanceof NewCharacterEvent) {
            NewCharacterEvent newCharacterEvent = (NewCharacterEvent) object;
            gameStage.addActor(new Character(
                    assets.get(Assets.brownGolem),
                    newCharacterEvent.x - 200,
                    newCharacterEvent.y,
                    true));
            System.out.println("EventListener");
        }
    }
}
