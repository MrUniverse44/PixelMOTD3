package dev.mruniverse.pixelmotd.spigot.listeners.packets;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.google.common.io.Files;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.MotdSettings;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import dev.mruniverse.pixelmotd.global.iridiumcolorapi.IridiumColorAPI;
import dev.mruniverse.pixelmotd.spigot.PixelMOTDBuilder;
import org.bukkit.ChatColor;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacketPingBuilder {
    private final PixelMOTDBuilder plugin;

    private final Random random = new Random();

    private Control control;

    public PacketPingBuilder(PixelMOTDBuilder plugin) {
        this.plugin = plugin;
        load();
    }

    public void update() { load(); }

    private void load() {
        control = plugin.getStorage().getFiles().getControl(GuardianFiles.MOTDS);
    }

    private String getMotd(MotdType type) {
        List<String> motds = control.getContent(type.getPath().replace(".",""),false);
        return motds.get(random.nextInt(motds.size()));
    }

    @SuppressWarnings("UnstableApiUsage")
    public void execute(MotdType motdType, WrappedServerPing ping) {
        String motd = getMotd(motdType);
        String line1,line2,completed;

        motdType.setMotd(motd);

        if(!motdType.isHexMotd()) {
            line1 = control.getColoredString(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getColoredString(motdType.getSettings(MotdSettings.LINE2));
            completed = line1 + "\n" + line2;
        } else {
            line1 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE2));
            try {
                completed = IridiumColorAPI.process(line1) + "\n" + IridiumColorAPI.process(line2);
            }catch (Throwable ignored) {
                completed = ChatColor.translateAlternateColorCodes('&',line1) + "\n" + ChatColor.translateAlternateColorCodes('&',line2);
            }
        }

        ping.setMotD(completed);

        if(plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.icon-system")) {
            File[] icons;
            File motdFolder;
            String iconFormat = motdType.getSettings(MotdSettings.ICONS_FOLDER);
            String icon = control.getString(motdType.getSettings(MotdSettings.ICONS_ICON));
            if(!iconFormat.equalsIgnoreCase("DEFAULT") && !iconFormat.equalsIgnoreCase("MAIN_FOLDER")) {
                motdFolder = plugin.getStorage().getFiles().getIconsFolder(motdType,motd);
            } else {
                if(iconFormat.equalsIgnoreCase("MAIN_FOLDER")) {
                    motdFolder = plugin.getStorage().getFiles().getIconsFolder(motdType);
                } else {
                    motdFolder = plugin.getStorage().getFiles().getMainIcons();
                }
            }

            icons = motdFolder.listFiles();
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
                        plugin.getStorage().getLogs().error("File " + icon + " doesn't exists");
                    } else {

                        if (Files.getFileExtension(finding.getPath()).equals("png")) {
                            validIcons.add(finding);
                        } else {
                            plugin.getStorage().getLogs().error("This image " + icon + " need be (.png) 64x64, this image isn't (.png) format");
                        }
                    }
                }

                if (validIcons.size() != 0) {
                    WrappedServerPing.CompressedImage image = getCompressedImage(validIcons.get(random.nextInt(validIcons.size())));
                    if (image != null) ping.setFavicon(image);
                }

            }
        }

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
        plugin.getStorage().getLogs().warn("Can't read image: &b" + filePath + "&f. Please check image size: 64x64 or check if the image isn't corrupted.");
    }
}
