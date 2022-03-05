package dev.mruniverse.pixelmotd.velocity.listeners;

import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.Extras;
import dev.mruniverse.pixelmotd.commons.FileStorage;
import dev.mruniverse.pixelmotd.commons.GLogger;
import dev.mruniverse.pixelmotd.commons.enums.*;
import dev.mruniverse.pixelmotd.commons.shared.VelocityExtras;
import dev.mruniverse.pixelmotd.velocity.PixelMOTD;
import dev.mruniverse.pixelmotd.velocity.storage.Storage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PingBuilder {
    private final Map<MotdType, List<String>> motdsMap = new HashMap<>();

    private final PixelMOTD plugin;

    private final MotdBuilder builder;

    private final Extras extras;

    private boolean iconSystem = true;

    private boolean playerSystem;

    private Control control;

    public PingBuilder(PixelMOTD plugin) {
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
        Storage storage = plugin.getStorage();
        FileStorage fileStorage = storage.getFiles();

        iconSystem = fileStorage.getControl(GuardianFiles.SETTINGS).getStatus("settings.icon-system");
        playerSystem = fileStorage.getControl(GuardianFiles.SETTINGS).getStatus("settings.player-system.toggle",true);


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

    public void execute(MotdType motdType, ProxyPingEvent event, int code, String user) {

        final GLogger logs = plugin.getStorage().getLogs();

        if (!plugin.getConfigVersion().isWork()) {
            logs.info("Your configuration is outdated,please check your config for missing paths, paths issues or update the plugin for new paths!");
            logs.info("You can backup your plugin files and let the plugin create new files to fix the issue");
            logs.info("Or apply manually file changes and update the config-version of the settings.yml to the latest config-version.");
            return;
        }

        ServerPing.Builder ping = event.getPing().asBuilder();

        String motd = getMotd(motdType);

        String line1, line2, completed;
        int online, max;

        motdType.setMotd(motd);

        if (iconSystem) {
            Favicon img = builder.getFavicon(motdType, control.getString(motdType.getSettings(MotdSettings.ICONS_ICON)));
            if (img != null) {
                ping.favicon(img);
            }
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(
                    control.getString(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TYPE))
            );
            online = mode.execute(control,
                    motdType,
                    MotdSettings.getValuePath(mode,false),
                    plugin.getServer().getPlayerCount()
            );
        } else {
            online = plugin.getServer().getPlayerCount();
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_MAX_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(
                    control.getString(motdType.getSettings(MotdSettings.PLAYERS_MAX_TYPE))
            );

            if (mode != MotdPlayersMode.EQUALS) {
                max = mode.execute(control,
                        motdType,
                        MotdSettings.getValuePath(mode, false),
                        plugin.getServer().getConfiguration().getShowMaxPlayers()
                );
            } else {
                max = mode.execute(control,
                        motdType,
                        MotdSettings.getValuePath(mode, false),
                        online
                );
            }
        } else {
            max = plugin.getServer().getConfiguration().getShowMaxPlayers();
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.HOVER_TOGGLE))) {
            ping.samplePlayers(getHover(motdType, online, max, user));
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

            protocolName = LegacyComponentSerializer.builder().character('&').build().deserialize(
                    extras.getVariables(
                            control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MESSAGE)),
                            online,
                            max,
                            user
                    )
            ).content();

            ping.version(new ServerPing.Version(p1,protocolName));
        }
        Component result;
        if (!motdType.isHexMotd()) {
            line1 = control.getColoredString(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getColoredString(motdType.getSettings(MotdSettings.LINE2));
            completed = extras.getVariables(line1, online, max, user) + "\n" + extras.getVariables(line2, online, max, user);
            result = Component.text(completed);
        } else {
            line1 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE1));
            line2 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE2));
            try {
                completed = extras.getVariables(line1, online, max, user) + "\n" + extras.getVariables(line2, online, max, user);
                result = Component.text(completed);
                
            } catch (NullPointerException exception) {
                plugin.getStorage().getLogs().error(exception);
                completed = LegacyComponentSerializer.builder().character('&').build().deserialize(extras.getVariables(line1, online, max, user) + "\n" + extras.getVariables(line2, online, max, user)).content();
                result = Component.text(completed);
            }
        }

        ping.description(result);
        ping.onlinePlayers(online);
        ping.maximumPlayers(max);
        event.setPing(ping.build());
    }

    public ServerPing.SamplePlayer[] getHover(MotdType motdType, int online, int max, String user) {
        ServerPing.SamplePlayer[] hoverToShow = new ServerPing.SamplePlayer[0];
        List<String> lines;
        if (playerSystem) {
            lines = extras.getConvertedLines(
                    control.getColoredStringList(
                            motdType.getSettings(MotdSettings.HOVER_LINES)),
                    control.getInt(motdType.getSettings(MotdSettings.HOVER_MORE_PLAYERS))
            );
        } else {
            lines = control.getColoredStringList(motdType.getSettings(MotdSettings.HOVER_LINES));
        }
        for (String line : lines) {
            UUID id = UUID.randomUUID();
            hoverToShow = addHoverLine(hoverToShow, new ServerPing.SamplePlayer(extras.getVariables(line, online, max, user), id));
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
