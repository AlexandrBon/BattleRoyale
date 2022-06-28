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

    // font assets
    public static final AssetDescriptor<Skin> skin =
            new AssetDescriptor<>("skin/skin-composer-ui.json", Skin.class);

    // UI assets
    public static final AssetDescriptor<TextureAtlas> uiAtlas =
            new AssetDescriptor<>("ui/ui.atlas", TextureAtlas.class);

    // characters assets
    public static final AssetDescriptor<TextureAtlas> greenGoblinWalking =
            new AssetDescriptor<>("fire/fire_1.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> healWave =
            new AssetDescriptor<>("healWave/healWave.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> explosion =
            new AssetDescriptor<>("explosion/explosion.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenGoblinAttacking =
            new AssetDescriptor<>("greenGoblin/greenGoblinAttacking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> grayGolemWalking =
            new AssetDescriptor<>("grayGolem/grayGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> grayGolemAttacking =
            new AssetDescriptor<>("grayGolem/grayGolemAttacking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenGolemWalking =
            new AssetDescriptor<>("greenGolem/greenGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenGolemAttacking =
            new AssetDescriptor<>("greenGolem/greenGolemAttacking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenOrcWalking =
            new AssetDescriptor<>("greenOrc/greenOrcWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenOrcAttacking =
            new AssetDescriptor<>("greenOrc/greenOrcAttacking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> lavaGolemWalking =
            new AssetDescriptor<>("lavaGolem/lavaGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> lavaGolemAttacking =
            new AssetDescriptor<>("lavaGolem/lavaGolemAttacking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> dirtGolemWalking =
            new AssetDescriptor<>("dirtGolem/dirtGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> dirtGolemAttacking =
            new AssetDescriptor<>("dirtGolem/dirtGolemAttacking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> iceGolemWalking =
            new AssetDescriptor<>("iceGolem/iceGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> iceGolemAttacking =
            new AssetDescriptor<>("iceGolem/iceGolemAttacking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> brownGolemWalking =
            new AssetDescriptor<>("brownGolem/brownGolemWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> brownGolemAttacking =
            new AssetDescriptor<>("brownGolem/brownGolemAttacking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenOgreWalking =
            new AssetDescriptor<>("greenOgre/greenOgreWalking.atlas", TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> greenOgreAttacking =
            new AssetDescriptor<>("greenOgre/greenOgreAttacking.atlas", TextureAtlas.class);

    // card assets
    public static final AssetDescriptor<TextureAtlas> cardsAtlas =
            new AssetDescriptor<>("cardTextures/cardTextures.atlas", TextureAtlas.class);

    // backgrounds assets
    public static final AssetDescriptor<Texture> mainMenuBackground =
            new AssetDescriptor<>("backgrounds/game_background_1.png", Texture.class);

    public static final AssetDescriptor<Texture> gameBackground =
            new AssetDescriptor<>("backgrounds/game_background_2.png", Texture.class);

    // spells assets
    public static final AssetDescriptor<TextureAtlas> fire =
            new AssetDescriptor<>("brownGolem/brownGolemWalking.atlas", TextureAtlas.class);

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