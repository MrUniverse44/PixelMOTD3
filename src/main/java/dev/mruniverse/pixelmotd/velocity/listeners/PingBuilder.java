package dev.mruniverse.pixelmotd.velocity.listeners;

import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.Extras;
import dev.mruniverse.pixelmotd.global.enums.*;
import dev.mruniverse.pixelmotd.global.shared.VelocityExtras;
import dev.mruniverse.pixelmotd.velocity.PixelMOTDBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.UUID;

public class PingBuilder {
    private final PixelMOTDBuilder plugin;

    private final MotdBuilder builder;

    private final Extras extras;

    private boolean playerSystem;

    private Control control;

    public PingBuilder(PixelMOTDBuilder plugin) {
        this.plugin = plugin;
        this.builder = new MotdBuilder(plugin, plugin.getStorage().getLogs());
        this.extras = new VelocityExtras(plugin);
        load();
    }

    public void update() {
        load();
        builder.update();
        extras.update();
    }

    private void load() {
        playerSystem = plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.player-system.toggle",true);
        control = plugin.getStorage().getFiles().getControl(GuardianFiles.MOTDS);
    }

    private String getMotd(MotdType type) {
        List<String> motds = control.getContent(type.getPath().replace(".",""),false);
        return motds.get(control.getRandom().nextInt(motds.size()));
    }

    public void execute(MotdType motdType, ProxyPingEvent event, int code) {

        if (!plugin.getConfigVersion().isWork()) {
            plugin.getStorage().getLogs().info("Your configuration is outdated,please check your config for missing paths, paths issues or update the plugin for new paths!");
            plugin.getStorage().getLogs().info("You can backup your plugin files and let the plugin create new files to fix the issue");
            plugin.getStorage().getLogs().info("Or apply manually file changes and update the config-version of the settings.yml to the latest config-version.");
            return;
        }

        ServerPing.Builder ping = event.getPing().asBuilder();

        String motd;
        try {
            motd = getMotd(motdType);
        } catch (Throwable ignored) {
            plugin.getStorage().getLogs().error("This file isn't updated to the latest file or the motd-path is incorrect, can't find motds for MotdType: " + motdType.getName());
            return;
        }
        String line1,line2,completed;
        int online,max;

        motdType.setMotd(motd);

        if (plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.icon-system")) {
            Favicon img = builder.getFavicon(motdType, control.getString(motdType.getSettings(MotdSettings.ICONS_ICON)));
            if (img != null) ping.favicon(img);
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TYPE)));
            online = mode.execute(control,motdType,MotdSettings.getValuePath(mode,false), plugin.getServer().getPlayerCount());
        } else {
            online = plugin.getServer().getPlayerCount();
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_MAX_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_MAX_TYPE)));
            if (mode != MotdPlayersMode.EQUALS) {
                max = mode.execute(control, motdType, MotdSettings.getValuePath(mode, false), plugin.getServer().getConfiguration().getShowMaxPlayers());
            } else {
                max = mode.execute(control, motdType, MotdSettings.getValuePath(mode, false), online);
            }
        } else {
            max = plugin.getServer().getConfiguration().getShowMaxPlayers();
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.HOVER_TOGGLE))) {
            ping.samplePlayers(getHover(motdType,online,max));
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PROTOCOL_TOGGLE))) {
            MotdProtocol protocol = MotdProtocol.getFromText(
                    control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MODIFIER)),
                    code
            );
            String protocolName;
            int p1 = ping.getVersion().getProtocol();

            if (protocol == MotdProtocol.ALWAYS_POSITIVE) {
                p1 = code;
            } else if (protocol == MotdProtocol.ALWAYS_NEGATIVE) {
                p1 = -1;
            }

            protocolName = LegacyComponentSerializer.builder().character('&').build().deserialize(extras.getVariables(control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MESSAGE)), online, max)).content();

            ping.version(new ServerPing.Version(p1,protocolName));
        }
        Component result;
        if (!motdType.isHexMotd()) {
            line1 = control.getColoredString(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getColoredString(motdType.getSettings(MotdSettings.LINE2));
            completed = extras.getVariables(line1,online,max) + "\n" + extras.getVariables(line2,online,max);
            result = Component.text(completed);
        } else {
            line1 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE2));
            try {
                /*
                 * REMOVED MINEDOWN & IRIDIUM_COLOR_API TEMPORALLY IN VELOCITY
                 */
                completed = extras.getVariables(line1,online,max) + "\n" + extras.getVariables(line2,online,max);
                result = Component.text(completed);
            }catch (NullPointerException exception) {
                plugin.getStorage().getLogs().error(exception);
                completed = LegacyComponentSerializer.builder().character('&').build().deserialize(extras.getVariables(line1,online,max) + "\n" + extras.getVariables(line2,online,max)).content();
                result = Component.text(completed);
            }
        }

        ping.description(result);
        ping.onlinePlayers(online);
        ping.maximumPlayers(max);
        event.setPing(ping.build());
    }

    public ServerPing.SamplePlayer[] getHover(MotdType motdType, int online, int max) {
        ServerPing.SamplePlayer[] hoverToShow = new ServerPing.SamplePlayer[0];
        List<String> lines;
        if (playerSystem) {
            lines = extras.getConvertedLines(control.getColoredStringList(motdType.getSettings(MotdSettings.HOVER_LINES)),control.getInt(motdType.getSettings(MotdSettings.HOVER_MORE_PLAYERS)));
        } else {
            lines = control.getColoredStringList(motdType.getSettings(MotdSettings.HOVER_LINES));
        }
        for(String line : lines) {
            try {
                UUID id = UUID.randomUUID();
                hoverToShow = addHoverLine(hoverToShow, new ServerPing.SamplePlayer(extras.getVariables(line,online,max), id));
            } catch (Throwable ignored) {
                plugin.getStorage().getLogs().info("Can't show the hover, please check if everything is correct in your motd config.");
            }
        }
        return hoverToShow;
    }
    private ServerPing.SamplePlayer[] addHoverLine(ServerPing.SamplePlayer[] player, ServerPing.SamplePlayer info) {
        ServerPing.SamplePlayer[] hoverText = new ServerPing.SamplePlayer[player.length + 1];
        System.arraycopy(player, 0, hoverText, 0, player.length);
        hoverText[player.length] = info;
        return hoverText;
    }
}
