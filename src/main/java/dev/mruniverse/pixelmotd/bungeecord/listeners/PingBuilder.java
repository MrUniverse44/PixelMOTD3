package dev.mruniverse.pixelmotd.bungeecord.listeners;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.Extras;
import dev.mruniverse.pixelmotd.global.enums.*;
import dev.mruniverse.pixelmotd.global.iridiumcolorapi.IridiumColorAPI;
import dev.mruniverse.pixelmotd.global.minedown.MineDown;
import dev.mruniverse.pixelmotd.global.shared.BungeeExtras;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;

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
        this.extras = new BungeeExtras(plugin);
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

    public void execute(MotdType motdType, ServerPing ping,int code) {
        if(!plugin.getConfigVersion().isWork()) {
            plugin.getStorage().getLogs().info("Your configuration is outdated,please check your config for missing paths, paths issues or update the plugin for new paths!");
            plugin.getStorage().getLogs().info("You can backup your plugin files and let the plugin create new files to fix the issue");
            plugin.getStorage().getLogs().info("Or apply manually file changes and update the config-version of the settings.yml to the latest config-version.");
            return;
        }
        String motd;
        try {
            motd = getMotd(motdType);
        } catch (Exception ignored) {
            plugin.getStorage().getLogs().error("This file isn't updated to the latest file or the motd-path is incorrect, can't find motds for MotdType: " + motdType.getName());
            return;
        }
        String line1,line2,completed;
        int online,max;

        motdType.setMotd(motd);

        if(plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.icon-system")) {
            Favicon img = builder.getFavicon(motdType, control.getString(motdType.getSettings(MotdSettings.ICONS_ICON)));
            if(img != null) ping.setFavicon(img);
        }

        if(control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TYPE)));
            online = mode.execute(control,motdType,MotdSettings.getValuePath(mode,false),ProxyServer.getInstance().getOnlineCount());
        } else {
            online = ProxyServer.getInstance().getOnlineCount();
        }

        if(control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_MAX_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_MAX_TYPE)));
            if(mode != MotdPlayersMode.EQUALS) {
                max = mode.execute(control, motdType, MotdSettings.getValuePath(mode, false), ping.getPlayers().getMax());
            } else {
                max = mode.execute(control, motdType, MotdSettings.getValuePath(mode, false), online);
            }
        } else {
            max = ping.getPlayers().getMax();
        }

        if(control.getStatus(motdType.getSettings(MotdSettings.HOVER_TOGGLE))) {
            ping.getPlayers().setSample(getHover(motdType,online,max));
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PROTOCOL_TOGGLE))) {
            MotdProtocol protocol = MotdProtocol.getFromText(
                    control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MODIFIER)),
                    code
            );

            if(protocol == MotdProtocol.ALWAYS_POSITIVE || protocol == MotdProtocol.ALWAYS_NEGATIVE) {
                ping.getVersion().setProtocol(protocol.getCode());
            }

            String result;
            if(!motdType.isHexMotd()) {
                result = ChatColor.translateAlternateColorCodes('&', extras.getVariables(control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MESSAGE)), online, max));
            } else {
                result = IridiumColorAPI.process(extras.getVariables(control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MESSAGE)), online, max));
            }
            ping.getVersion().setName(result);
        }

        TextComponent result = new TextComponent("");
        if(!motdType.isHexMotd()) {
            line1 = control.getColoredString(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getColoredString(motdType.getSettings(MotdSettings.LINE2));
            completed = extras.getVariables(line1,online,max) + "\n" + extras.getVariables(line2,online,max);
            result.addExtra(completed);
        } else {
            line1 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE2));
            try {
                if(plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getString("settings.hex-system-process","IRIDIUMCOLORAPI").equalsIgnoreCase("IRIDIUMCOLORAPI")) {
                    TextComponent firstLine = new TextComponent(IridiumColorAPI.process(extras.getVariables(line1, online, max) + "&f"));
                    TextComponent secondLine = new TextComponent(IridiumColorAPI.process(extras.getVariables(line2, online, max) + "&f"));
                    result.addExtra(firstLine);
                    result.addExtra("\n");
                    result.addExtra(secondLine);
                } else {
                    completed = extras.getVariables(line1,online,max) + "\n" + extras.getVariables(line2,online,max);
                    result = new TextComponent(new MineDown(completed.replace('§', '&')).urlDetection(false).toComponent());
                }
            }catch (Exception exception) {
                plugin.getStorage().getLogs().error(exception);
                completed = ChatColor.translateAlternateColorCodes('&',extras.getVariables(line1,online,max)) + "\n" + ChatColor.translateAlternateColorCodes('&',extras.getVariables(line2,online,max));
                result.addExtra(completed);
            }
        }

        ping.setDescriptionComponent(result);
        ping.getPlayers().setOnline(online);
        ping.getPlayers().setMax(max);

    }

    public ServerPing.PlayerInfo[] getHover(MotdType motdType, int online,int max) {
        ServerPing.PlayerInfo[] hoverToShow = new ServerPing.PlayerInfo[0];
        List<String> lines;
        if(playerSystem) {
            lines = extras.getConvertedLines(
                    control.getColoredStringList(motdType.getSettings(MotdSettings.HOVER_LINES)),
                    control.getInt(motdType.getSettings(MotdSettings.HOVER_MORE_PLAYERS))
            );
        } else {
            lines = control.getColoredStringList(motdType.getSettings(MotdSettings.HOVER_LINES));
        }
        try {
            final UUID uuid = new UUID(0,0);
            for (String line : lines) {
                hoverToShow = addHoverLine(hoverToShow, new ServerPing.PlayerInfo(extras.getVariables(line, online, max), uuid));
            }
        }catch(Exception exception) {
            plugin.getStorage().getLogs().info("Can't show the hover, please check if everything is correct in your motd config.");
        }
        return hoverToShow;
    }
    private ServerPing.PlayerInfo[] addHoverLine(ServerPing.PlayerInfo[] player, ServerPing.PlayerInfo info) {
        ServerPing.PlayerInfo[] hoverText = new ServerPing.PlayerInfo[player.length + 1];
        System.arraycopy(player, 0, hoverText, 0, player.length);
        hoverText[player.length] = info;
        return hoverText;
    }
}
