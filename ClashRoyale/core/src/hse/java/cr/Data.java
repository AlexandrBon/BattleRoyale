package hse.java.cr;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Data {
    private final AssetManager manager = new AssetManager();

    public void load() {
        for (int i = 0; i < 12; i++) {
            String path = "golemAnimation1/0_Golem_Run Slashing_0" + (i < 10 ? "0" + i : i) + ".png";
            manager.load(path, Texture.class);
        }
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