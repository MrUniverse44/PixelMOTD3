package dev.mruniverse.pixelmotd.spigot.listeners;

import dev.mruniverse.pixelmotd.commons.GLogger;
import dev.mruniverse.pixelmotd.commons.enums.MotdType;
import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Icon {
    private final CachedServerIcon favicon;
    private final MotdType motdType;
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

    public CachedServerIcon getFavicon() {
        return favicon;
    }

    private CachedServerIcon getFavicon(File icon) {
        try {
            BufferedImage image = ImageIO.read(icon);
            logs.info("&aIcon loaded: &6" + icon.getName() + "&a of MotdType &6" + motdType.getName());
            return Bukkit.loadServerIcon(image);
        } catch (Exception exception) {
            logs.error("Can't create favicon: " + name + ", maybe the icon is not 64x64 or is broken. Showing Exception:");
            logs.error(exception);
            return null;
        }
    }

}

