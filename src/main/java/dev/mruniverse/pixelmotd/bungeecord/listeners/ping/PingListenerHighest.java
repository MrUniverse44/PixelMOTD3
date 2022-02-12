package dev.mruniverse.pixelmotd.bungeecord.listeners.ping;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.listeners.PingBuilder;
import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.Ping;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.commons.enums.MotdType;
import dev.mruniverse.pixelmotd.commons.enums.Type;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PingListenerHighest implements Listener,Ping {

    private final PixelMOTD plugin;
    private final PingBuilder pingBuilder;

    private boolean isWhitelisted;
    private boolean hasOutdatedClient;
    private boolean hasOutdatedServer;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    public PingListenerHighest(PixelMOTD plugin) {
        this.plugin = plugin;
        this.pingBuilder = new PingBuilder(plugin);
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPing(final ProxyPingEvent event) {
        final ServerPing ping = event.getResponse();

        if (ping == null || event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
            return;
        }

        PendingConnection data = event.getConnection();

        final int protocol = data.getVersion();

        if (isWhitelisted) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX,ping,protocol);
                return;
            }
            pingBuilder.execute(plugin.getStorage().getPriority().get(Type.WHITELISTED), ping,protocol);
            return;
        }
        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.NORMAL_HEX,ping,protocol);
                return;
            }
            pingBuilder.execute(plugin.getStorage().getPriority().get(Type.DEFAULT),ping,protocol);
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            pingBuilder.execute(MotdType.OUTDATED_SERVER,ping,protocol);
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            pingBuilder.execute(MotdType.OUTDATED_CLIENT,ping,protocol);
        }



    }
}
