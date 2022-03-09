package dev.mruniverse.pixelmotd.bungeecord.listeners.ping;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.listeners.PingBuilder;
import dev.mruniverse.pixelmotd.bungeecord.storage.Storage;
import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.FileStorage;
import dev.mruniverse.pixelmotd.commons.Ping;
import dev.mruniverse.pixelmotd.commons.enums.FileSaveMode;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.commons.enums.MotdType;
import dev.mruniverse.pixelmotd.commons.enums.Type;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Cancellable;

import java.net.SocketAddress;

public abstract class AbstractPingListener implements Ping {
    private final PixelMOTD plugin;
    private final PingBuilder pingBuilder;

    private boolean isWhitelisted;
    private boolean hasOutdatedClient;
    private boolean hasOutdatedServer;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    public AbstractPingListener(PixelMOTD plugin) {
        this.plugin = plugin;
        this.pingBuilder = new PingBuilder(plugin);
        load();
    }

    private void load() {
        Storage storage = plugin.getStorage();
        FileStorage fileStorage = storage.getFiles();

        fileStorage.reloadFile(FileSaveMode.WHITELIST);
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

    public void execute(final ProxyPingEvent event) {
        final ServerPing ping = event.getResponse();

        if (ping == null || event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
            return;
        }

        PendingConnection data = event.getConnection();

        final int protocol = data.getVersion();

        final SocketAddress socketAddress = data.getSocketAddress();

        final String user = getPlayerDatabase().getPlayer(socketAddress.toString());

        if (isWhitelisted) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX, ping, protocol, user);
                return;
            }
            pingBuilder.execute(plugin.getStorage().getPriority().get(Type.WHITELISTED), ping, protocol, user);
            return;
        }
        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.NORMAL_HEX, ping, protocol, user);
                return;
            }
            pingBuilder.execute(plugin.getStorage().getPriority().get(Type.DEFAULT), ping, protocol, user);
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            pingBuilder.execute(MotdType.OUTDATED_SERVER, ping, protocol, user);
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            pingBuilder.execute(MotdType.OUTDATED_CLIENT, ping, protocol, user);
        }
    }
}
