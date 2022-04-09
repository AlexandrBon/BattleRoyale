package hse.java.cr;

import com.badlogic.gdx.Game;

import hse.java.cr.screens.LoadingScreen;

public class Starter extends Game {
    private Data assets;

    public Data getAssets() {
        return assets;
    }

    @Override
    public void create() {
        assets = new Data();
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