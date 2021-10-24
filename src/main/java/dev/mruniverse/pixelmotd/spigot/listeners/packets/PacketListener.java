package dev.mruniverse.pixelmotd.spigot.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import dev.mruniverse.pixelmotd.spigot.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.spigot.utils.Ping;
import dev.mruniverse.pixelmotd.spigot.utils.Priority;

public class PacketListener extends PacketAdapter implements Ping {

    private final PixelMOTDBuilder plugin;
    private final PacketPingBuilder pingBuilder;

    private boolean isWhitelisted;
    private boolean hasOutdatedClient;
    private boolean hasOutdatedServer;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    public PacketListener(PixelMOTDBuilder plugin,Priority priority) {
        super(plugin,get(priority), PacketType.Status.Server.SERVER_INFO);
        this.plugin = plugin;
        this.pingBuilder = new PacketPingBuilder(plugin);
        load();
    }

    private void load() {
        final Control control = plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS);
        isWhitelisted = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getStatus("whitelist.toggle");
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
        if(event.isCancelled()) return;
        if(event.getPlayer() == null) return;

        final WrappedServerPing ping = event.getPacket().getServerPings().read(0);

        final int protocol = plugin.getProtocolVersion(event.getPlayer());

        if(isWhitelisted) {
            if(protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX,ping,event.getPlayer());
                return;
            }
            pingBuilder.execute(MotdType.WHITELIST,ping,event.getPlayer());
            return;
        }
        if(!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if(protocol >= 735) {
                pingBuilder.execute(MotdType.NORMAL_HEX,ping,event.getPlayer());
                return;
            }
            pingBuilder.execute(MotdType.NORMAL,ping,event.getPlayer());
            return;
        }
        if(MAX_PROTOCOL < protocol && hasOutdatedServer) {
            pingBuilder.execute(MotdType.OUTDATED_SERVER,ping,event.getPlayer());
            return;
        }
        if(MIN_PROTOCOL > protocol && hasOutdatedClient) {
            pingBuilder.execute(MotdType.OUTDATED_CLIENT,ping,event.getPlayer());
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
