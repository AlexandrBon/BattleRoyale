package hse.java.cr;

import com.badlogic.gdx.Game;

import hse.java.cr.screens.LoadingScreen;

public class Starter extends Game {
    private Assets assets;
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