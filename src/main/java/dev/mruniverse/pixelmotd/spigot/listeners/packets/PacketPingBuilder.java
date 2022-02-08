package dev.mruniverse.pixelmotd.spigot.listeners.packets;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.Extras;
import dev.mruniverse.pixelmotd.global.enums.*;
import dev.mruniverse.pixelmotd.global.iridiumcolorapi.IridiumColorAPI;
import dev.mruniverse.pixelmotd.global.shared.SpigotExtras;
import dev.mruniverse.pixelmotd.spigot.PixelMOTDBuilder;

import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacketPingBuilder {
    private final PixelMOTDBuilder plugin;

    private final PacketMotdBuilder builder;

    private final Extras extras;

    private boolean playerSystem;

    private Control control;

    public PacketPingBuilder(PixelMOTDBuilder plugin) {
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
        playerSystem = plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.player-system.toggle",true);
        control = plugin.getStorage().getFiles().getControl(GuardianFiles.MOTDS);
    }

    private String getMotd(MotdType type) {
        List<String> motds = control.getContent(type.getPath().replace(".",""),false);
        return motds.get(control.getRandom().nextInt(motds.size()));
    }

    public void execute(MotdType motdType, WrappedServerPing ping,int code) {
        String motd = getMotd(motdType);
        String line1, line2, completed;
        int online, max;

        motdType.setMotd(motd);

        if (plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.icon-system")) {
            WrappedServerPing.CompressedImage img = builder.getFavicon(motdType, control.getString(motdType.getSettings(MotdSettings.ICONS_ICON)));
            if (img != null) ping.setFavicon(img);
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
            try {
                completed = IridiumColorAPI.process(extras.getVariables(line1,online,max)) + "\n" + IridiumColorAPI.process(extras.getVariables(line2,online,max));
            }catch (Exception ignored) {
                completed = ChatColor.translateAlternateColorCodes('&',extras.getVariables(line1,online,max)) + "\n" + ChatColor.translateAlternateColorCodes('&',extras.getVariables(line2,online,max));
            }
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
