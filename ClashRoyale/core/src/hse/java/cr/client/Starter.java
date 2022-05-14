package hse.java.cr.client;

import com.badlogic.gdx.Game;

import com.esotericsoftware.kryonet.Client;
import hse.java.cr.wrappers.Assets;
import hse.java.cr.screens.LoadingScreen;

public class Starter extends Game {
    private Assets assets;
    private static Client client;

    public static void setClient(Client client) {
        Starter.client = client;
    }

    public static Client getClient() {
        return client;
    }

    public Assets getAssets() {
        return assets;
    }

    @Override
    public void create() {
        assets = new Assets();
        assets.load();
        setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        assets.dispose();
    }
}