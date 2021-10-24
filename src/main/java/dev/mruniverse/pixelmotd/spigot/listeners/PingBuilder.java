package dev.mruniverse.pixelmotd.spigot.listeners;

import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.Extras;
import dev.mruniverse.pixelmotd.global.enums.*;
import dev.mruniverse.pixelmotd.global.iridiumcolorapi.IridiumColorAPI;
import dev.mruniverse.pixelmotd.global.shared.SpigotExtras;
import dev.mruniverse.pixelmotd.spigot.PixelMOTDBuilder;
import org.bukkit.ChatColor;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

import java.io.File;
import java.util.List;

public class PingBuilder {
    private final PixelMOTDBuilder plugin;

    private final MotdBuilder builder;

    private final Extras extras;

    private Control control;

    public PingBuilder(PixelMOTDBuilder plugin) {
        this.plugin = plugin;
        this.builder = new MotdBuilder(plugin.getStorage().getLogs());
        this.extras = new SpigotExtras(plugin);
        load();
    }

    public void update() { load(); }

    private void load() {
        control = plugin.getStorage().getFiles().getControl(GuardianFiles.MOTDS);
    }

    private String getMotd(MotdType type) {
        List<String> motds = control.getContent(type.getPath().replace(".",""),false);
        return motds.get(control.getRandom().nextInt(motds.size()));
    }

    public void execute(MotdType motdType, ServerListPingEvent ping) {
        String motd = getMotd(motdType);
        String line1,line2,completed;
        int online,max;

        motdType.setMotd(motd);



        if(plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.icon-system")) {
            File motdFolder = IconFolders.getIconFolderFromText(plugin.getStorage().getFiles(), motdType.getSettings(MotdSettings.ICONS_FOLDER), motdType, motd);
            File[] icons = motdFolder.listFiles();
            CachedServerIcon img = builder.getIcon(icons, control.getString(motdType.getSettings(MotdSettings.ICONS_ICON)), motdFolder);
            if(img != null) ping.setServerIcon(img);
        }

        if(control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TYPE)));
            online = mode.execute(control,motdType,MotdSettings.getValuePath(mode,false),ping.getNumPlayers());
        } else {
            online = ping.getNumPlayers();
        }

        if(control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_MAX_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_MAX_TYPE)));
            if(mode != MotdPlayersMode.EQUALS) {
                max = mode.execute(control, motdType, MotdSettings.getValuePath(mode, false), ping.getMaxPlayers());
            } else {
                max = mode.execute(control, motdType, MotdSettings.getValuePath(mode, false), online);
            }
        } else {
            max = ping.getMaxPlayers();
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

        ping.setMotd(completed);
        ping.setMaxPlayers(max);

    }
}
