package dev.mruniverse.pixelmotd.spigot.listeners;

import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.Extras;
import dev.mruniverse.pixelmotd.commons.FileStorage;
import dev.mruniverse.pixelmotd.commons.GLogger;
import dev.mruniverse.pixelmotd.commons.enums.*;
import dev.mruniverse.pixelmotd.commons.iridiumcolorapi.IridiumColorAPI;
import dev.mruniverse.pixelmotd.commons.shared.SpigotExtras;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.storage.Storage;
import dev.mruniverse.pixelmotd.spigot.utils.PlaceholderParser;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PingBuilder {
    private final Map<MotdType, List<String>> motdsMap = new HashMap<>();

    private final PixelMOTD plugin;

    private final MotdBuilder builder;

    private final Extras extras;

    private boolean iconSystem = true;

    private Control control;

    public PingBuilder(PixelMOTD plugin) {
        this.plugin  = plugin;
        this.builder = new MotdBuilder(plugin, plugin.getStorage().getLogs());
        this.extras  = new SpigotExtras(plugin);
        load();
    }

    public void update() {
        load();
        builder.update();
        extras.update();
    }

    private void load() {
        Storage storage = plugin.getStorage();
        FileStorage fileStorage = storage.getFiles();

        iconSystem = fileStorage.getControl(GuardianFiles.SETTINGS).getStatus("settings.icon-system");

        fileStorage.reloadFile(FileSaveMode.MOTDS);
        control = fileStorage.getControl(GuardianFiles.MOTDS);

        motdsMap.clear();

        for (MotdType motdType : MotdType.values()) {

            List<String> motds = control.getContent(
                    motdType.getPath().replace(".", ""),
                    false
            );

            motdsMap.put(
                    motdType,
                    motds
            );

            storage.getLogs().info("&aMotds loaded for type &e" + motdType.getName() + "&a, motds loaded: &f" + motds.toString().replace("[","").replace("]",""));
        }
    }

    private List<String> loadMotds(MotdType type) {
        List<String> list = control.getContent(
                type.getPath().replace(".", ""),
                false
        );
        motdsMap.put(
                type,
                list
        );
        return list;
    }

    private String getMotd(MotdType type) {
        List<String> motds = motdsMap.get(type);
        if (motds == null) {
            motds = loadMotds(type);
        }
        return motds.get(control.getRandom().nextInt(motds.size()));
    }

    public void execute(MotdType motdType, ServerListPingEvent ping) {

        final GLogger logs = plugin.getStorage().getLogs();

        if (!plugin.getConfigVersion().isWork()) {
            logs.info("Your configuration is outdated,please check your config for missing paths, paths issues or update the plugin for new paths!");
            logs.info("You can backup your plugin files and let the plugin create new files to fix the issue");
            logs.info("Or apply manually file changes and update the config-version of the settings.yml to the latest config-version.");
            return;
        }

        String motd = getMotd(motdType);

        String line1, line2, completed;
        int online, max;

        motdType.setMotd(motd);

        if (iconSystem) {
            CachedServerIcon img = builder.getFavicon(motdType, control.getString(motdType.getSettings(MotdSettings.ICONS_ICON)));
            if (img != null) {
                ping.setServerIcon(img);
            }
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TYPE)));
            online = mode.execute(control,motdType,MotdSettings.getValuePath(mode,false),ping.getNumPlayers());
        } else {
            online = ping.getNumPlayers();
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_MAX_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_MAX_TYPE)));
            if (mode != MotdPlayersMode.EQUALS) {
                max = mode.execute(control, motdType, MotdSettings.getValuePath(mode, false), ping.getMaxPlayers());
            } else {
                max = mode.execute(control, motdType, MotdSettings.getValuePath(mode, false), online);
            }
        } else {
            max = ping.getMaxPlayers();
        }

        if (!motdType.isHexMotd()) {
            line1 = control.getColoredString(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getColoredString(motdType.getSettings(MotdSettings.LINE2));
            completed = extras.getVariables(line1,online,max) + "\n" + extras.getVariables(line2,online,max);
        } else {
            line1 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE2));

            completed = IridiumColorAPI.process(extras.getVariables(line1,online,max)) + "\n" + IridiumColorAPI.process(extras.getVariables(line2,online,max));
        }

        if (plugin.hasPAPI()) {
            completed = PlaceholderParser.parse(plugin.getStorage().getLogs(),completed);
        }

        ping.setMotd(completed);
        ping.setMaxPlayers(max);

    }
}
