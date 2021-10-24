package dev.mruniverse.pixelmotd.bungeecord.listeners;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.Extras;
import dev.mruniverse.pixelmotd.global.enums.*;
import dev.mruniverse.pixelmotd.global.iridiumcolorapi.IridiumColorAPI;
import dev.mruniverse.pixelmotd.global.shared.BungeeExtras;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.util.List;

public class PingBuilder {
    private final PixelMOTDBuilder plugin;

    private final MotdBuilder builder;

    private final Extras extras;

    private boolean playerSystem;

    private Control control;

    public PingBuilder(PixelMOTDBuilder plugin) {
        this.plugin = plugin;
        this.builder = new MotdBuilder(plugin.getStorage().getLogs());
        this.extras = new BungeeExtras(plugin);
        load();
    }

    public void update() { load(); }

    private void load() {
        playerSystem = plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.player-system.toggle",true);
        control = plugin.getStorage().getFiles().getControl(GuardianFiles.MOTDS);
    }

    private String getMotd(MotdType type) {
        List<String> motds = control.getContent(type.getPath().replace(".",""),false);
        return motds.get(control.getRandom().nextInt(motds.size()));
    }

    public void execute(MotdType motdType, ServerPing ping,int code) {
        String motd = getMotd(motdType);
        String line1,line2,completed;
        int online,max;

        motdType.setMotd(motd);

        if(plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.icon-system")) {
            File motdFolder = IconFolders.getIconFolderFromText(plugin.getStorage().getFiles(), motdType.getSettings(MotdSettings.ICONS_FOLDER), motdType, motd);
            File[] icons = motdFolder.listFiles();
            Favicon img = builder.getIcon(icons, control.getString(motdType.getSettings(MotdSettings.ICONS_ICON)), motdFolder);
            if(img != null) ping.setFavicon(img);
        }

        if(control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TYPE)));
            online = mode.execute(control,motdType,MotdSettings.getValuePath(mode,false),ping.getPlayers().getOnline());
        } else {
            online = ping.getPlayers().getOnline();
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
            MotdProtocol protocol = MotdProtocol.getFromText(control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MODIFIER)));
            if(protocol == MotdProtocol.ALWAYS_POSITIVE) {
                ping.getVersion().setProtocol(code);
            } else if (protocol == MotdProtocol.ALWAYS_NEGATIVE) {
                ping.getVersion().setProtocol(-1);
            }
            String result;
            if(!motdType.isHexMotd()) {
                result = ChatColor.translateAlternateColorCodes('&', extras.getVariables(control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MESSAGE)), online, max));
            } else {
                result = IridiumColorAPI.process(extras.getVariables(control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MESSAGE)), online, max));
            }
            ping.getVersion().setName(result);
        }

        if(!motdType.isHexMotd()) {
            line1 = control.getColoredString(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getColoredString(motdType.getSettings(MotdSettings.LINE2));
            completed = extras.getVariables(line1,online,max) + "\n" + extras.getVariables(line2,online,max);
        } else {
            line1 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE2));
            try {
                completed = IridiumColorAPI.process(extras.getVariables(line1,online,max)) + "\n" + IridiumColorAPI.process(extras.getVariables(line2,online,max));
            }catch (Throwable ignored) {
                completed = ChatColor.translateAlternateColorCodes('&',extras.getVariables(line1,online,max)) + "\n" + ChatColor.translateAlternateColorCodes('&',extras.getVariables(line2,online,max));
            }
        }

        ping.setDescriptionComponent(new TextComponent(completed));
        ping.getPlayers().setOnline(online);
        ping.getPlayers().setMax(max);

    }

    public ServerPing.PlayerInfo[] getHover(MotdType motdType, int online,int max) {
        int ids = 0;
        ServerPing.PlayerInfo[] hoverToShow = new ServerPing.PlayerInfo[0];
        List<String> lines;
        if(playerSystem) {
            lines = extras.getConvertedLines(control.getColoredStringList(motdType.getSettings(MotdSettings.HOVER_LINES)),control.getInt(motdType.getSettings(MotdSettings.HOVER_MORE_PLAYERS)));
        } else {
            lines = control.getColoredStringList(motdType.getSettings(MotdSettings.HOVER_LINES));
        }
        for(String line : lines) {
            try {
                hoverToShow = addHoverLine(hoverToShow, new ServerPing.PlayerInfo(extras.getVariables(line,online,max), String.valueOf(ids)));
            } catch (Throwable ignored) {
                plugin.getStorage().getLogs().info("Can't show the hover, please check if everything is correct in your motd config.");
            }
            ids++;
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
