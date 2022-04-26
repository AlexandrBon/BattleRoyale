package hse.java.cr.helpers;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class Packer {
    public static void main (String[] args) {
        Settings settings = new Settings();
        settings.alias = false;
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;
        TexturePacker.process(settings, "assets/goblinAnimation_1",
                "assets/goblinAnimation_1",
                "goblinAnimation_1");
    }
}