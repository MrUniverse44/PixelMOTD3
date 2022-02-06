package dev.mruniverse.pixelmotd.spigot.listeners.packets;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.mruniverse.pixelmotd.global.GLogger;
import dev.mruniverse.pixelmotd.global.enums.MotdType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Icon {
    private final WrappedServerPing.CompressedImage favicon;
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

    public WrappedServerPing.CompressedImage getFavicon() {
        return favicon;
    }

    private WrappedServerPing.CompressedImage getFavicon(File icon) {
        try {
            BufferedImage image = ImageIO.read(icon);
            return WrappedServerPing.CompressedImage.fromPng(image);
        } catch (Exception exception) {
            logs.error("Can't create favicon: " + name + ", maybe the icon is not 64x64 or is broken. Showing Exception:");
            logs.error(exception);
            return null;
        }
    }

}

