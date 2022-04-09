package hse.java.cr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.jetbrains.annotations.NotNull;

public class ScreenAssistant {
    @NotNull
    public static Texture getBackground(String pathname) {
        Pixmap pixmapOld = new Pixmap(Gdx.files.internal(pathname));
        Pixmap pixmap = new Pixmap(1280, 720, pixmapOld.getFormat());
        pixmap.drawPixmap(pixmapOld,
                0, 0, pixmapOld.getWidth(), pixmapOld.getHeight(),
                0, 0, pixmap.getWidth(), pixmap.getHeight()
        );
        Texture loadingBackground = new Texture(pixmap);
        pixmapOld.dispose();
        pixmap.dispose();
        return loadingBackground;
    }
}
