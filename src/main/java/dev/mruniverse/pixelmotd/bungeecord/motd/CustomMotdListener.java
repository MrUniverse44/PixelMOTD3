package dev.mruniverse.pixelmotd.bungeecord.motd;

import com.google.common.io.Files;
import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.MotdSettings;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomMotdListener implements Listener {
    private final PixelMOTD plugin;

    private final Random random = new Random();

    private Configuration motds;

    private Configuration whitelist;


    public CustomMotdListener(PixelMOTD plugin) {
        this.plugin = plugin;
        motds = plugin.getStorage().getControl(GuardianFiles.MOTDS);
        whitelist = plugin.getStorage().getControl(GuardianFiles.WHITELIST);
    }
    @SuppressWarnings("unused")
    public void update() {
        motds = plugin.getStorage().getControl(GuardianFiles.MOTDS);
        whitelist = plugin.getStorage().getControl(GuardianFiles.WHITELIST);
    }

    private String getMotd(MotdType motdType) {
        Configuration section = motds.getSection(motdType.getMotdsUsingPath());
        if(section == null) return "E991PX";
        List<String> motdToGet = new ArrayList<>(section.getKeys());
        return motdToGet.get(random.nextInt(motdToGet.size()));
    }

    private MotdInformation getCurrentMotd(int currentProtocol, int max, int online) {
        if(whitelist.getBoolean("whitelist.toggle")) {
            return new MotdInformation(plugin.getStorage(),MotdType.WHITELIST,getMotd(MotdType.WHITELIST),max,online);
        }
        Configuration settings = plugin.getStorage().getControl(GuardianFiles.SETTINGS);

        boolean outdatedClientMotd = settings.getBoolean("settings.outdated-client-motd");
        boolean outdatedServerMotd = settings.getBoolean("settings.outdated-server-motd");

        int maxProtocol = plugin.getStorage().getControl(GuardianFiles.SETTINGS).getInt("settings.max-server-protocol");
        int minProtocol = plugin.getStorage().getControl(GuardianFiles.SETTINGS).getInt("settings.min-server-protocol");

        if(!outdatedClientMotd && !outdatedServerMotd || currentProtocol >= minProtocol && currentProtocol <= maxProtocol) {
            return new MotdInformation(plugin.getStorage(),MotdType.NORMAL,getMotd(MotdType.NORMAL),max,online);
        }
        if(maxProtocol < currentProtocol && outdatedServerMotd) {
            return new MotdInformation(plugin.getStorage(),MotdType.OUTDATED_SERVER,getMotd(MotdType.OUTDATED_SERVER),max,online);
        }
        if(minProtocol > currentProtocol && outdatedClientMotd) {
            return new MotdInformation(plugin.getStorage(),MotdType.OUTDATED_CLIENT,getMotd(MotdType.OUTDATED_CLIENT),max,online);
        }
        return new MotdInformation(plugin.getStorage(),MotdType.NORMAL,getMotd(MotdType.NORMAL),max,online);
    }

    @SuppressWarnings("UnstableApiUsage")
    @EventHandler(priority = EventPriority.HIGH)
    public void onPingEvent(ProxyPingEvent event) {
        if(event.getConnection() == null) return;
        if(event.getResponse() == null) return;
        if(event instanceof Cancellable && ((Cancellable) event).isCancelled()) return;

        ServerPing ping = event.getResponse();

        PendingConnection data = event.getConnection();

        int max = ping.getPlayers().getMax();
        int online = ping.getPlayers().getOnline();
        int protocol = data.getVersion();

        ServerPing.Protocol protocolInfo;
        ServerPing.Players players;
        ServerPing.PlayerInfo[] hover = ping.getPlayers().getSample();

        final String motd;

        Favicon icon = null;

        MotdInformation info = getCurrentMotd(protocol,max,online);
        plugin.getLogs().info("D");
        if(info.getHexStatus() && protocol >= 721) {
            motd = info.getHexAllMotd();
        } else {
            motd = info.getAllMotd();
        }

        if(info.getHoverStatus()) {
            hover = getHover(info);
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
                    BufferedImage image;
                    try{
                        image = ImageIO.read(validIcons.get(random.nextInt(validIcons.size())));
                        if (image != null) icon = Favicon.create(image);
                    }catch (Throwable throwable) {
                        icon = ping.getFaviconObject();
                        plugin.getLogs().error("Can't put the server-icon.");
                        plugin.getLogs().error(throwable);
                    }
                    if(icon == null) icon = ping.getFaviconObject();
                } else {
                    icon = ping.getFaviconObject();
                }

            }
        }

        if(motds.getBoolean(info.getMotdType().getSettings(MotdSettings.CUSTOM_PROTOCOL_TOGGLE))) {
            ServerPing.Protocol received = ping.getVersion();
            received.setName(MotdUtils.getServers(plugin,motds.getString(info.getMotdType().getSettings(MotdSettings.CUSTOM_PROTOCOL_NAME))));
            if(motds.getBoolean(info.getMotdType().getSettings(MotdSettings.CUSTOM_PROTOCOL_VERSION_TOGGLE))) {
                String value = motds.getString(info.getMotdType().getSettings(MotdSettings.CUSTOM_PROTOCOL_VALUE));
                if(value==null) value = "EQUALS";
                if(value.equalsIgnoreCase("EQUALS") || value.equalsIgnoreCase("SAME")) {
                    received.setProtocol(protocol);
                } else {
                    received.setProtocol(Integer.parseInt(value));
                }
            }
            protocolInfo = received;
        } else {
            protocolInfo = ping.getVersion();
        }

        players = new ServerPing.Players(max,online,hover);

        ServerPing result = new ServerPing(protocolInfo,players,new TextComponent(motd),icon);

        event.setResponse(result);
    }

    private ServerPing.PlayerInfo[] getHover(MotdInformation info) {
        ServerPing.PlayerInfo[] hover = new ServerPing.PlayerInfo[0];
        int number = 0;
        for(String line : info.getHover()) {
            hover = addHoverLine(hover, new ServerPing.PlayerInfo(color(
                    MotdUtils.getServers(plugin,line.replace("&","§")
                            .replace("%online%", info.getOnline() + "")
                            .replace("%max%", info.getMax() + ""))
            ), String.valueOf(number)));
            number++;
        }
        return hover;
    }

    private String color(String message) { return ChatColor.translateAlternateColorCodes('&',message);}

    @SuppressWarnings("ManualArrayCopy")
    private ServerPing.PlayerInfo[] addHoverLine(ServerPing.PlayerInfo[] player, ServerPing.PlayerInfo info) {
        ServerPing.PlayerInfo[] hoverText = new ServerPing.PlayerInfo[player.length + 1];
        for(int id = 0; id < player.length; id++) {
            hoverText[id] = player[id];
        }
        hoverText[player.length] = info;
        return hoverText;
    }
}
