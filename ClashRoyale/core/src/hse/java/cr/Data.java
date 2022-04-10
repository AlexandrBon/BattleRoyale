package hse.java.cr;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Data {
    private final AssetManager manager = new AssetManager();

    public void load() {
        String path = "golemAnimation2/packed.png";
        manager.load(path, Texture.class);
    }

    public AssetManager getManager() {
        return manager;
    }

    public boolean updateManager(int millis) {
        return manager.update(millis);
    }

    public void dispose() {
        manager.dispose();
    }
}