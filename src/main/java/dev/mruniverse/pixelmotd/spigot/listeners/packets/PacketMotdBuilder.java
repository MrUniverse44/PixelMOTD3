package dev.mruniverse.pixelmotd.spigot.listeners.packets;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.google.common.io.Files;
import dev.mruniverse.pixelmotd.global.GLogger;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacketMotdBuilder {

    private final Random random = new Random();

    private final GLogger logs;

    public PacketMotdBuilder(GLogger logs) {
        this.logs = logs;
    }
    @SuppressWarnings("UnstableApiUsage")
    public WrappedServerPing.CompressedImage getIcon(File[] icons, String icon, File motdFolder) {
        if (icons != null && icons.length != 0) {
            List<File> validIcons = new ArrayList<>();
            if(icon.equalsIgnoreCase("RANDOM")) {
                for (File image : icons) {
                    if (Files.getFileExtension(image.getPath()).equals("png")) {
                        validIcons.add(image);
                    }
                }
            } else {
                File finding = new File(motdFolder,icon);
                if(!finding.exists()) {
                    logs.error("File " + icon + " doesn't exists");
                } else {
                    if (Files.getFileExtension(finding.getPath()).equals("png")) {
                        validIcons.add(finding);
                    } else {
                        logs.error("This image " + icon + " isn't (.png) format");
                    }
                }
            }
            if (validIcons.size() != 0) {
                return getCompressedImage(validIcons.get(random.nextInt(validIcons.size())));
            }
        }
        return null;
    }

    private WrappedServerPing.CompressedImage getCompressedImage(File file) {
        try {
            return WrappedServerPing.CompressedImage.fromPng(ImageIO.read(file));
        } catch(Throwable ignored) {
            reportBadImage(file.getPath());
            return null;
        }
    }

    private void reportBadImage(String filePath) {
        logs.warn("Can't read image: &b" + filePath + "&f. Please check image size: 64x64 or check if the image isn't corrupted.");
    }
}
