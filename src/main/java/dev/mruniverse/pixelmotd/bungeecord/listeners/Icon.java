package dev.mruniverse.pixelmotd.bungeecord.listeners;

import dev.mruniverse.pixelmotd.global.GLogger;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import net.md_5.bungee.api.Favicon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Icon {
    private final MotdType motdType;
    private final Favicon favicon;
    private final GLogger logs;
    private final String name;

    public Icon(GLogger logs, MotdType motdType, File icon) {
        this.favicon  = getFavicon(icon);
        this.motdType = motdType;
        this.name     = icon.getName();
        this.logs     = logs;
    }

    public MotdType getType() {
        return motdType;
    }

    public String getName() {
        return name;
    }

    public Favicon getFavicon() {
        return favicon;
    }

    private Favicon getFavicon(File icon) {
        try {
            BufferedImage image = ImageIO.read(icon);
            return Favicon.create(image);
        } catch (IOException exception) {
            logs.error("Can't create favicon: " + name + ", maybe the icon is not 64x64 or is broken. Showing Exception:");
            logs.error(exception);
            return null;
        }
    }

}
