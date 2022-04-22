package hse.java.cr;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
    private final AssetManager manager = new AssetManager();

    public static final AssetDescriptor<Sound> clickSound =
            new AssetDescriptor<>("sounds/clickSound.mp3", Sound.class);

    public static final AssetDescriptor<TextureAtlas> uiAtlas =
            new AssetDescriptor<>("ui/button.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> golemAtlas =
            new AssetDescriptor<>("ui/button.atlas", TextureAtlas.class);

    public static final AssetDescriptor<Texture> mainMenuBackground =
            new AssetDescriptor<Texture>("backgrounds/game_background_2.png", Texture.class);

    public static final AssetDescriptor<Texture> gameBackground =
            new AssetDescriptor<Texture>("backgrounds/game_background_2.png", Texture.class);

    public void load() {
        manager.load(clickSound);
        manager.load(mainMenuBackground);
        manager.load(golemAtlas);
        manager.load(uiAtlas);
    }

    public <T> T get(AssetDescriptor<T> assetDescriptor) {
        return manager.get(assetDescriptor);
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