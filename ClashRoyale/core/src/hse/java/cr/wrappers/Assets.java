package hse.java.cr.wrappers;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.HashMap;

import static com.badlogic.gdx.utils.reflect.ClassReflection.getFields;

public class Assets {
    private final AssetManager manager = new AssetManager();
    private static final HashMap<String, TextureAtlas>
            stringToTextureAtlas = new HashMap<>();

    public static final AssetDescriptor<Sound> clickSound =
            new AssetDescriptor<>("sounds/clickSound.mp3", Sound.class);

    public static final AssetDescriptor<TextureAtlas> uiAtlas =
            new AssetDescriptor<>("ui/button.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenGoblin =
            new AssetDescriptor<>("greenGoblin/greenGoblin.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> grayGolem =
            new AssetDescriptor<>("grayGolem/grayGolemRun.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenGolem =
            new AssetDescriptor<>("greenGolem/golemAnimation_2.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> brownGolem =
            new AssetDescriptor<>("brownGolem/brownGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> cardsAtlas =
            new AssetDescriptor<>("cardTextures/cardTextures.atlas", TextureAtlas.class);

    public static final AssetDescriptor<Texture> mainMenuBackground =
            new AssetDescriptor<>("backgrounds/game_background_1.png", Texture.class);

    public static final AssetDescriptor<Texture> gameBackground =
            new AssetDescriptor<>("backgrounds/game_background_2.png", Texture.class);

    public void load() {
        manager.load(clickSound);
        manager.load(mainMenuBackground);
        manager.load(gameBackground);
        manager.load(cardsAtlas);
        manager.load(greenGoblin);
        manager.load(grayGolem);
        manager.load(greenGolem);
        manager.load(brownGolem);
        manager.load(uiAtlas);
    }

    public <T> T get(AssetDescriptor<T> assetDescriptor) {
        return manager.get(assetDescriptor);
    }

    public AssetManager getManager() {
        return manager;
    }

    public static TextureAtlas stringToTextureAtlas(String s) {
        return stringToTextureAtlas.get(s);
    }

    public boolean updateManager(int millis) {
        return manager.update(millis);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void fillStringToTextureAtlasMap() throws ReflectionException {
        for (Field field : getFields(Assets.class)) {
            if (get((AssetDescriptor) field.get(null)) instanceof TextureAtlas) {
                stringToTextureAtlas.put(field.getName(),
                        get((AssetDescriptor<TextureAtlas>) field.get(null)));
            }
        }
    }

    public void dispose() {
        manager.dispose();
    }
}