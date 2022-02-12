package dev.mruniverse.pixelmotd.spigot.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.Ping;
import dev.mruniverse.pixelmotd.commons.Priority;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.commons.enums.MotdType;
import dev.mruniverse.pixelmotd.commons.enums.Type;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;

public class PacketListener extends PacketAdapter implements Ping {

    private final PixelMOTD plugin;
    private final PacketPingBuilder pingBuilder;

    private boolean isWhitelisted;
    private boolean hasOutdatedClient;
    private boolean hasOutdatedServer;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    public PacketListener(PixelMOTD plugin, Priority priority) {
        super(plugin,get(priority), PacketType.Status.Server.SERVER_INFO);
        this.plugin = plugin;
        this.pingBuilder = new PacketPingBuilder(plugin);
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
        load();
    }

    private void load() {
        final Control control = plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS);
        isWhitelisted = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getStatus("whitelist.global.Enabled");
        hasOutdatedClient = control.getStatus("settings.outdated-client-motd",true);
        hasOutdatedServer = control.getStatus("settings.outdated-server-motd",true);
        MAX_PROTOCOL = control.getInt("settings.max-server-protocol",756);
        MIN_PROTOCOL = control.getInt("settings.min-server-protocol",47);
    }

    @Override
    public void update() {
        load();
        pingBuilder.update();
    }

    @Override
    public void setWhitelist(boolean status) { isWhitelisted = status; }

    @Override
    public void onPacketSending(final PacketEvent event) {
        if (event.getPacketType() != PacketType.Status.Server.SERVER_INFO) return;
        if (event.isCancelled()) return;
        if (event.getPlayer() == null) return;

        final WrappedServerPing ping = event.getPacket().getServerPings().read(0);

        final int protocol = plugin.getProtocolVersion(event.getPlayer());

        if (isWhitelisted) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX,ping, protocol);
                return;
            }
            pingBuilder.execute(plugin.getStorage().getPriority().get(Type.WHITELISTED),ping, protocol);
            return;
        }

        if (MAX_PROTOCOL < protocol) {
            if (hasOutdatedServer) {
                pingBuilder.execute(MotdType.OUTDATED_SERVER,ping, protocol);
            } else {
                if (protocol >= 735) {
                    pingBuilder.execute(MotdType.NORMAL_HEX,ping, protocol);
                    return;
                }
                pingBuilder.execute(plugin.getStorage().getPriority().get(Type.DEFAULT),ping, protocol);
            }
            return;
        }
        if (MIN_PROTOCOL > protocol) {
            if (hasOutdatedClient) {
                pingBuilder.execute(MotdType.OUTDATED_CLIENT, ping, protocol);
            } else {
                if (protocol >= 735) {
                    pingBuilder.execute(MotdType.NORMAL_HEX,ping, protocol);
                    return;
                }
                pingBuilder.execute(plugin.getStorage().getPriority().get(Type.DEFAULT),ping, protocol);
            }

            return;
        }

        if (protocol >= 735) {
            pingBuilder.execute(MotdType.NORMAL_HEX,ping, protocol);
        } else {
            pingBuilder.execute(plugin.getStorage().getPriority().get(Type.DEFAULT), ping, protocol);
        }
    }

    public static ListenerPriority get(Priority priority) {
        switch (priority) {
            case HIGHEST:
                return ListenerPriority.HIGHEST;
            case NORMAL:
                return ListenerPriority.NORMAL;
            default:
            case HIGH:
                return ListenerPriority.HIGH;
            case LOWEST:
                return ListenerPriority.LOWEST;
            case LOW:
                return ListenerPriority.LOW;
            case MONITOR:
                return ListenerPriority.MONITOR;
        }
    }
}
