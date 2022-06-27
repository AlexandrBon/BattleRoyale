package hse.java.cr.wrappers;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

    public static final AssetDescriptor<Skin> skin =
            new AssetDescriptor<>("skin/skin-composer-ui.json", Skin.class);

    public static final AssetDescriptor<TextureAtlas> uiAtlas =
            new AssetDescriptor<>("ui/ui.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenGoblin =
            new AssetDescriptor<>("greenGoblin/greenGoblinWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> grayGolem =
            new AssetDescriptor<>("grayGolem/grayGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenGolem =
            new AssetDescriptor<>("greenGolem/greenGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenOrc =
            new AssetDescriptor<>("greenOrc/greenOrcWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> lavaGolem =
            new AssetDescriptor<>("lavaGolem/lavaGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> dirtGolem =
            new AssetDescriptor<>("dirtGolem/dirtGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> iceGolem =
            new AssetDescriptor<>("iceGolem/iceGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> brownGolem =
            new AssetDescriptor<>("brownGolem/brownGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenOgre =
            new AssetDescriptor<>("greenOgre/greenOgreWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> cardsAtlas =
            new AssetDescriptor<>("cardTextures/cardTextures.atlas", TextureAtlas.class);

    public static final AssetDescriptor<Texture> mainMenuBackground =
            new AssetDescriptor<>("backgrounds/game_background_1.png", Texture.class);

    public static final AssetDescriptor<Texture> gameBackground =
            new AssetDescriptor<>("backgrounds/game_background_2.png", Texture.class);

    @SuppressWarnings({"rawtypes"})
    public void load() {
        Field[] fields = getFields(Assets.class);
        for (Field field : fields) {
            Object object = null;
            try {
                object = field.get(null);
            } catch (ReflectionException ignored) {
            }
            if (object instanceof AssetDescriptor) {
                manager.load((AssetDescriptor) object);
            }
        }
    }

    public <T> T get(AssetDescriptor<T> assetDescriptor) {
        return manager.get(assetDescriptor);
    }

    public AssetManager getManager() {
        return manager;
    }

    public static TextureAtlas getTextureAtlas(String s) {
        return stringToTextureAtlas.get(s);
    }

    public boolean updateManager(int millis) {
        return manager.update(millis);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void fillStringToTextureAtlasMap() throws ReflectionException {
        Field[] fields = getFields(Assets.class);
        for (Field field : fields) {
            Object object = field.get(null);
            if (object instanceof AssetDescriptor && get((AssetDescriptor) object) instanceof TextureAtlas) {
                stringToTextureAtlas.put(field.getName(),
                        get((AssetDescriptor<TextureAtlas>) object));
            }
        }
    }

    public void dispose() {
        manager.dispose();
    }
}