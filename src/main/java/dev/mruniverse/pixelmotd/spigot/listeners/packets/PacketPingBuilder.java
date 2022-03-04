package dev.mruniverse.pixelmotd.spigot.listeners.packets;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.Extras;
import dev.mruniverse.pixelmotd.commons.enums.*;
import dev.mruniverse.pixelmotd.commons.iridiumcolorapi.IridiumColorAPI;
import dev.mruniverse.pixelmotd.commons.shared.SpigotExtras;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;

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
        iconSystem = plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.icon-system");
        playerSystem = plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.player-system.toggle",true);
        control = plugin.getStorage().getFiles().getControl(GuardianFiles.MOTDS);

        for (MotdType motdType : MotdType.values()) {
            motdsMap.put(
                    motdType,
                    control.getContent(
                            motdType.getPath().replace(".", ""),
                            false
                    )
            );
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

    public void execute(MotdType motdType, WrappedServerPing ping,int code) {

        String motd = getMotd(motdType);

        String line1, line2, completed;
        int online, max;

        motdType.setMotd(motd);

        if (iconSystem) {
            WrappedServerPing.CompressedImage img = builder.getFavicon(motdType, control.getString(motdType.getSettings(MotdSettings.ICONS_ICON)));
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
            ping.setPlayers(getHover(motdType,online,max));
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

            if (!motdType.isHexMotd()) {
                result = ChatColor.translateAlternateColorCodes('&', extras.getVariables(control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MESSAGE)), online, max));
            } else {
                result = IridiumColorAPI.process(extras.getVariables(control.getString(motdType.getSettings(MotdSettings.PROTOCOL_MESSAGE)), online, max));
            }

            ping.setVersionName(result);
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

        ping.setMotD(completed);
        ping.setPlayersOnline(online);
        ping.setPlayersMaximum(max);

    }

    public List<WrappedGameProfile> getHover(MotdType motdType, int online, int max) {
        List<WrappedGameProfile> result = new ArrayList<>();
        if (motdType.isHexMotd()) {
            if (playerSystem) {
                for (String line : extras.getConvertedLines(control.getStringList(motdType.getSettings(MotdSettings.HOVER_LINES)), control.getInt(motdType.getSettings(MotdSettings.HOVER_MORE_PLAYERS)))) {
                    result.add(new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), IridiumColorAPI.process(extras.getVariables(line, online, max))));
                }
            } else {
                for (String line : control.getStringList(motdType.getSettings(MotdSettings.HOVER_LINES))) {
                    result.add(new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), IridiumColorAPI.process(extras.getVariables(line, online, max))));
                }
            }
            return result;
        }

        if (playerSystem) {
            for (String line : extras.getConvertedLines(control.getColoredStringList(motdType.getSettings(MotdSettings.HOVER_LINES)), control.getInt(motdType.getSettings(MotdSettings.HOVER_MORE_PLAYERS)))) {
                result.add(new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), extras.getVariables(line, online, max)));
            }
        } else {
            for (String line : control.getColoredStringList(motdType.getSettings(MotdSettings.HOVER_LINES))) {
                result.add(new WrappedGameProfile(UUID.fromString("0-0-0-0-0"), extras.getVariables(line, online, max)));
            }
        }
        return result;
    }

}
