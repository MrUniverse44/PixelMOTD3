package dev.mruniverse.pixelmotd.bungeecord.listeners;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.Ping;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener,Ping {

    private final PixelMOTDBuilder plugin;
    private final PingBuilder pingBuilder;

    private boolean isWhitelisted;
    private boolean hasOutdatedClient;
    private boolean hasOutdatedServer;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    public PingListener(PixelMOTDBuilder plugin) {
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

    @EventHandler(priority = 64)
    public void onPing(final ProxyPingEvent event) {
        final ServerPing ping = event.getResponse();

        if (ping == null || event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
            return;
        }

        PendingConnection data = event.getConnection();

        final int protocol = data.getVersion();

        if(isWhitelisted) {
            if(protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX,ping,protocol);
                return;
            }
            pingBuilder.execute(MotdType.WHITELIST,ping,protocol);
            return;
        }
        if(!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if(protocol >= 735) {
                pingBuilder.execute(MotdType.NORMAL_HEX,ping,protocol);
                return;
            }
            pingBuilder.execute(MotdType.NORMAL,ping,protocol);
            return;
        }
        if(MAX_PROTOCOL < protocol && hasOutdatedServer) {
            pingBuilder.execute(MotdType.OUTDATED_SERVER,ping,protocol);
            return;
        }
        if(MIN_PROTOCOL > protocol && hasOutdatedClient) {
            pingBuilder.execute(MotdType.OUTDATED_CLIENT,ping,protocol);
        }



    }
}
