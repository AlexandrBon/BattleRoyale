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

    public static final AssetDescriptor<TextureAtlas> goblinAnimation_1 =
            new AssetDescriptor<>("goblinAnimation_1/goblinAnimation_1.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> golemAnimation_1 =
            new AssetDescriptor<>("golemAnimation_1/golemAnimation_1.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> golemAnimation_2 =
            new AssetDescriptor<>("golemAnimation_2/golemAnimation_2.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> golemAnimation_3 =
            new AssetDescriptor<>("golemAnimation_3/golemAnimation_3.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> cardTextures =
            new AssetDescriptor<>("cardTextures/cardTextures.atlas", TextureAtlas.class);

    public static final AssetDescriptor<Texture> mainMenuBackground =
            new AssetDescriptor<Texture>("backgrounds/game_background_2.png", Texture.class);

    public static final AssetDescriptor<Texture> gameBackground =
            new AssetDescriptor<Texture>("backgrounds/game_background_2.png", Texture.class);

    public void load() {
        manager.load(clickSound);
        manager.load(mainMenuBackground);
        manager.load(cardTextures);
        manager.load(goblinAnimation_1);
        manager.load(golemAnimation_1);
        manager.load(golemAnimation_2);
        manager.load(golemAnimation_3);
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