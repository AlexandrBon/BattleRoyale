package hse.java.cr.helpers;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class Packer {
    public static void main (String[] args) {
        Settings settings = new Settings();
        settings.alias = false;
        settings.maxHeight = 1024;
        settings.maxWidth = 1024;
        TexturePacker.process(settings, "assets/golemAnimation1",
                "assets/golemAnimation2",
                "packed");
    }
}