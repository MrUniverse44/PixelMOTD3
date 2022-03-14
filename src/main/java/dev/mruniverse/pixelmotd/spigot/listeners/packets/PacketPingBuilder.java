package dev.mruniverse.pixelmotd.spigot.listeners.packets;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
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
import org.bukkit.ChatColor;

import java.util.*;

public class PacketPingBuilder {

    private final Map<MotdType, List<String>> motdsMap = new HashMap<>();

    private final PixelMOTD plugin;

    private final PacketMotdBuilder builder;

    private final Extras extras;

    private boolean iconSystem = true;

    private boolean playerSystem = false;

    private Control control;

    public PacketPingBuilder(PixelMOTD plugin) {
        this.plugin = plugin;
        this.builder = new PacketMotdBuilder(plugin, plugin.getStorage().getLogs());
        this.extras = new SpigotExtras(plugin);
        load();
    }

    public void update() {
        builder.update();
        extras.update();
        load();
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

    /**
     * This method will return the motd-name.
     * @param type MotdType
     * @return String
     */
    private String getMotd(MotdType type) {
        List<String> motds = motdsMap.get(type);

        if (motds == null) {
            motds = loadMotds(type);
        }

        return motds.get(control.getRandom().nextInt(motds.size()));
    }

    public void execute(MotdType motdType, WrappedServerPing ping, int code, String user) {

        String motd = getMotd(motdType);

        String line1, line2, completed;

        int online, max;

        motdType.setMotd(motd);

        boolean hex = motdType.isHexMotd();

        if (iconSystem) {
            WrappedServerPing.CompressedImage img = builder.getFavicon(
                    motdType,
                    control.getString(motdType.getSettings(MotdSettings.ICONS_ICON))
            );
            if (img != null) {
                ping.setFavicon(img);
            }
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_ONLINE_TYPE)));
            online = mode.execute(control,motdType,MotdSettings.getValuePath(mode,false),ping.getPlayersOnline());
        } else {
            online = ping.getPlayersOnline();
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PLAYERS_MAX_TOGGLE))) {
            MotdPlayersMode mode = MotdPlayersMode.getModeFromText(control.getString(motdType.getSettings(MotdSettings.PLAYERS_MAX_TYPE)));

            if (mode != MotdPlayersMode.EQUALS) {
                max = mode.execute(control, motdType, MotdSettings.getValuePath(mode, false), ping.getPlayersMaximum());
            } else {
                max = mode.execute(control, motdType, MotdSettings.getValuePath(mode, false), online);
            }

        } else {
            max = ping.getPlayersMaximum();
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.HOVER_TOGGLE))) {
            ping.setPlayers(
                    getHover(
                            motdType,
                            online,
                            max,
                            user
                    )
            );
        }

        if (control.getStatus(motdType.getSettings(MotdSettings.PROTOCOL_TOGGLE))) {

            MotdProtocol protocol = MotdProtocol.getFromText(
                    control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MODIFIER)),
                    code
            );

            if (protocol == MotdProtocol.ALWAYS_POSITIVE) {
                ping.setVersionProtocol(code);
            } else if (protocol == MotdProtocol.ALWAYS_NEGATIVE) {
                ping.setVersionProtocol(-1);
            }

            String result;

            if (!hex) {
                result = color(
                        extras.getVariables(
                                control.getString(
                                        motdType.getSettings(MotdSettings.PROTOCOL_MESSAGE)
                                ),
                                online,
                                max,
                                user
                        )
                );
            } else {
                result = IridiumColorAPI.process(extras.getVariables(control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MESSAGE)), online, max, user));
            }

            ping.setVersionName(result);
        }

        GLogger logs = plugin.getStorage().getLogs();

        if (!hex) {

            line1 = control.getColoredString(motdType.getSettings(MotdSettings.LINE1));

            if (plugin.hasPAPI()) {
                line1 = PlaceholderParser.parse(logs, user, line1);
            }

            line2 = control.getColoredString(motdType.getSettings(MotdSettings.LINE2));

            if (plugin.hasPAPI()) {
                line2 = PlaceholderParser.parse(logs, user, line2);
            }

            completed = extras.getVariables(line1, online, max, user) + "\n" + extras.getVariables(line2, online, max, user);

        } else {

            line1 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE1));

            if (plugin.hasPAPI()) {
                line1 = PlaceholderParser.parse(logs, user, line1);
            }

            line2 = control.getStringWithoutColors(motdType.getSettings(MotdSettings.LINE2));

            if (plugin.hasPAPI()) {
                line2 = PlaceholderParser.parse(logs, user, line2);
            }

            completed = process(extras.getVariables(line1,online,max, user))
                    + "\n"
                    + process(extras.getVariables(line2,online,max, user)
            );

        }

        ping.setMotD(completed);
        ping.setPlayersOnline(online);
        ping.setPlayersMaximum(max);

    }

    /**
     * This method will return the text with color codes
     * @return String
     */
    public String color(String text) {
        return ChatColor.translateAlternateColorCodes(
                '&',
                text
        );
    }
    /**
     * This method will return the text with Hex Color variables
     * @return String
     */
    public String process(String text) {
        return IridiumColorAPI.process(text);
    }

    /**
     * This method will return the hover for the motd converted with variables and hex colors.
     * Depending on the minecraft version and the MotdType data.
     * @return List<WrappedGameProfile>
     */
    public List<WrappedGameProfile> getHover(MotdType motdType, int online, int max, String user) {
        List<WrappedGameProfile> result = new ArrayList<>();

        List<String> lines;

        boolean hex = motdType.isHexMotd();

        if (hex) {
            lines = control.getStringList(motdType.getSettings(MotdSettings.HOVER_LINES));
        } else {
            lines = control.getColoredStringList(motdType.getSettings(MotdSettings.HOVER_LINES));
        }

        if (playerSystem) {
            lines = extras.getConvertedLines(
                    lines,
                    control.getInt(
                            motdType.getSettings(MotdSettings.HOVER_MORE_PLAYERS)
                    )
            );
        }

        if (hex) {
            for (String line : lines) {
                result.add(
                        new WrappedGameProfile(
                                UUID.fromString("0-0-0-0-0"),
                                color(
                                    IridiumColorAPI.process(
                                            extras.getVariables(line, online, max, user)
                                    )
                                )
                        )
                );
            }
        } else {
            for (String line : lines) {
                result.add(
                        new WrappedGameProfile(
                                UUID.fromString("0-0-0-0-0"),
                                color(
                                    extras.getVariables(line, online, max, user)
                                )
                        )
                );
            }
        }
        return result;
    }

}
