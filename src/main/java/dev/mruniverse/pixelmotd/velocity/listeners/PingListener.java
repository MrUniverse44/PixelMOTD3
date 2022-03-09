package dev.mruniverse.pixelmotd.velocity.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.server.ServerPing;
import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.FileStorage;
import dev.mruniverse.pixelmotd.commons.Ping;
import dev.mruniverse.pixelmotd.commons.enums.FileSaveMode;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.commons.enums.MotdType;
import dev.mruniverse.pixelmotd.velocity.storage.Storage;
import dev.mruniverse.pixelmotd.velocity.PixelMOTD;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class PingListener implements Ping {
    private final PixelMOTD plugin;
    private final PingBuilder pingBuilder;

    private boolean isWhitelisted;
    private boolean hasOutdatedClient;
    private boolean hasOutdatedServer;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    public PingListener(PixelMOTD plugin) {
        this.plugin = plugin;
        this.pingBuilder = new PingBuilder(plugin);
        load();
    }

    private void load() {
        Storage storage = plugin.getStorage();
        FileStorage fileStorage = storage.getFiles();

        fileStorage.reloadFile(FileSaveMode.MOTDS);
        fileStorage.reloadFile(FileSaveMode.SETTINGS);

        final Control control = fileStorage.getControl(GuardianFiles.SETTINGS);

        isWhitelisted = fileStorage.getControl(GuardianFiles.WHITELIST).getStatus("whitelist.global.Enabled");
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

    @Subscribe(order = PostOrder.EARLY)
    public void onMotdPing(ProxyPingEvent event) {
        ServerPing.Builder ping = event.getPing().asBuilder();

        final int protocol = ping.getVersion().getProtocol();

        final String user;

        InboundConnection connection = event.getConnection();

        InetSocketAddress socketAddress = connection.getRemoteAddress();

        if (socketAddress != null) {
            InetAddress address = socketAddress.getAddress();

            user = getPlayerDatabase().getPlayer(address.getHostAddress());
        } else {
            user = "unknown#1";
        }

        if (isWhitelisted) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX, event, protocol, user);
                return;
            }
            pingBuilder.execute(MotdType.WHITELIST, event, protocol, user);
            return;
        }
        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.NORMAL_HEX, event, protocol, user);
                return;
            }
            pingBuilder.execute(MotdType.NORMAL, event, protocol, user);
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            pingBuilder.execute(MotdType.OUTDATED_SERVER, event, protocol, user);
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            pingBuilder.execute(MotdType.OUTDATED_CLIENT, event, protocol, user);
        }
    }
}
