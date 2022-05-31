package hse.java.cr.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hse.java.cr.events.GameStartsEvent;
import hse.java.cr.events.EnemyInfo;
import hse.java.cr.screens.MainScreen;

public class JoinResponseListener extends Listener {
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof EnemyInfo) {
            final EnemyInfo enemyInfo = (EnemyInfo) object;
            Player.isLeft = enemyInfo.isLeft;
            Player.enemyUsername = enemyInfo.enemyUsername;
        } else if (object instanceof GameStartsEvent) {
            Player.gameIndex = ((GameStartsEvent) object).gameIndex;
            MainScreen.isGameRunning = true;
        }
    }
}
