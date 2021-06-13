package dev.mruniverse.pixelmotd.spigot.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.google.common.io.Files;
import com.google.inject.Inject;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.storage.Configuration;
import dev.mruniverse.pixelmotd.spigot.storage.GuardianFiles;
import dev.mruniverse.pixelmotd.spigot.utils.WrappedStatus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.imageio.ImageIO;
import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("unused")
public class CustomMotdListener extends PacketAdapter {

    private final PixelMOTD plugin;

    private final Random random = new Random();

    @Inject
    @Named("motds")
    private Configuration motds;

    @Inject
    @Named("events")
    private Configuration events;

    @Inject
    @Named("whitelist")
    private Configuration whitelist;


    public CustomMotdListener(PixelMOTD plugin, ListenerPriority priority) {
        super(plugin,priority, PacketType.Status.Server.SERVER_INFO);
        this.plugin = plugin;
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    private String getMotd(MotdType motdType) {
        ConfigurationSection section = motds.getConfigurationSection(motdType.getMotdsUsingPath());
        if(section == null) return "E991PX";
        List<String> motdToGet = new ArrayList<>(section.getKeys(false));
        return motdToGet.get(random.nextInt(motdToGet.size()));
    }

    private MotdInformation getCurrentMotd(int currentProtocol,int max,int online) {
        if(whitelist.getBoolean("whitelist.toggle")) {
            return new MotdInformation(MotdType.WHITELIST,getMotd(MotdType.WHITELIST),max,online);
        }
        FileConfiguration settings = plugin.getStorage().getControl(GuardianFiles.SETTINGS);

        boolean outdatedClientMotd = settings.getBoolean("settings.outdated-client-motd");
        boolean outdatedServerMotd = settings.getBoolean("settings.outdated-server-motd");

        int maxProtocol = plugin.getStorage().getControl(GuardianFiles.SETTINGS).getInt("settings.max-server-protocol");
        int minProtocol = plugin.getStorage().getControl(GuardianFiles.SETTINGS).getInt("settings.min-server-protocol");

        if(!outdatedClientMotd && !outdatedServerMotd || currentProtocol >= minProtocol && currentProtocol <= maxProtocol) {
            return new MotdInformation(MotdType.NORMAL,getMotd(MotdType.NORMAL),max,online);
        }
        if(maxProtocol < currentProtocol && outdatedServerMotd) {
            return new MotdInformation(MotdType.OUTDATED_SERVER,getMotd(MotdType.OUTDATED_SERVER),max,online);
        }
        if(minProtocol > currentProtocol && outdatedClientMotd) {
            return new MotdInformation(MotdType.OUTDATED_CLIENT,getMotd(MotdType.OUTDATED_CLIENT),max,online);
        }
        return new MotdInformation(MotdType.NORMAL,getMotd(MotdType.NORMAL),max,online);
    }

    public static List<WrappedGameProfile> getHover(List<String> hover) {
        List<WrappedGameProfile> result = new ArrayList<>();
        final UUID id = UUID.fromString("0-0-0-0-0");
        for(String line : hover) {
            result.add(new WrappedGameProfile(id, line));
        }
        return result;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onPacketSending(final PacketEvent event){
        if(event.isCancelled()) return;
        if(event.getPlayer() == null) return;
        if (event.getPacketType() != PacketType.Status.Server.SERVER_INFO) return;

        WrappedStatus packet = new WrappedStatus(event.getPacket());
        WrappedServerPing ping = packet.getJsonResponse();

        int max = Bukkit.getMaxPlayers();
        int online = Bukkit.getOnlinePlayers().size();
        int protocol;

        protocol = ProtocolLibrary.getProtocolManager().getProtocolVersion(event.getPlayer());

        MotdInformation info = getCurrentMotd(protocol,max,online);

        if(info.getHexStatus() && protocol >= 721) {
            ping.setMotD(info.getHexAllMotd());
        } else {
            ping.setMotD(info.getAllMotd());
        }

        if(info.getHoverStatus()) {
            ping.setPlayers(getHover(info.getHover()));
        }

        if(plugin.getStorage().getControl(GuardianFiles.SETTINGS).getBoolean("settings.icon-system") && motds.getBoolean(info.getMotdType().getPath() + "settings.icon")) {

            File[] icons;
            File motdFolder;

            if(info.getCustomIconStatus()) {
                motdFolder = plugin.getStorage().getIconsFolder(info.getMotdType(),info.getMotdName());
            } else {
                motdFolder = plugin.getStorage().getIconsFolder(info.getMotdType());
            }

            icons = motdFolder.listFiles();
            if (icons != null && icons.length != 0) {

                List<File> validIcons = new ArrayList<>();

                if(info.getCustomIconName().equalsIgnoreCase("RANDOM")) {
                    for (File image : icons) {
                        if (Files.getFileExtension(image.getPath()).equals("png")) {
                            validIcons.add(image);
                        }
                    }

                } else {

                    File finding = new File(motdFolder,info.getCustomIconName());

                    if(!finding.exists()) {
                        plugin.getLogs().error("File " + info.getCustomIconName() + " doesn't exists");
                    } else {

                        if (Files.getFileExtension(finding.getPath()).equals("png")) {
                            validIcons.add(finding);
                        } else {
                            plugin.getLogs().error("This image " + info.getCustomIconName() + " need be (.png) 64x64, this image isn't (.png) format");
                        }
                    }
                }

                if (validIcons.size() != 0) {
                    WrappedServerPing.CompressedImage image = getCompressedImage(validIcons.get(random.nextInt(validIcons.size())));
                    if (image != null) ping.setFavicon(image);
                }

            }
        }

        /*
         * PLAYERS SYSTEM - PROTOCOL VERSION MODIFIER
         */

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
        plugin.getLogs().warn("Can't read image: &b" + filePath + "&f. Please check image size: 64x64 or check if the image isn't corrupted.");
    }
}
