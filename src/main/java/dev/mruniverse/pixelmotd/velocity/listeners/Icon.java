package dev.mruniverse.pixelmotd.velocity.listeners;

import dev.mruniverse.pixelmotd.commons.GLogger;
import dev.mruniverse.pixelmotd.commons.enums.MotdType;
import com.velocitypowered.api.util.Favicon;

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
        this.motdType = motdType;
        this.name     = icon.getName();
        this.logs     = logs;
        this.favicon  = getFavicon(icon);
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
            logs.info("&aIcon loaded: &6" + icon.getName() + "&a of MotdType &6" + motdType.getName());
            return Favicon.create(image);
        } catch (IOException exception) {
            logs.error("Can't create favicon: " + name + ", maybe the icon is not 64x64 or is broken. Showing Exception:");
            logs.error(exception);
            return null;
        }
    }

}
