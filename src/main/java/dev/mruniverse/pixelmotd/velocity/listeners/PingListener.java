package dev.mruniverse.pixelmotd.velocity.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.Ping;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import dev.mruniverse.pixelmotd.velocity.PixelMOTDBuilder;

public class PingListener implements Ping {
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

    @Subscribe(order = PostOrder.EARLY)
    public void onMotdPing(ProxyPingEvent event) {
        ServerPing.Builder ping = event.getPing().asBuilder();

        final int protocol = ping.getVersion().getProtocol();

        if (isWhitelisted) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX,event,protocol);
                return;
            }
            pingBuilder.execute(MotdType.WHITELIST,event,protocol);
            return;
        }
        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.NORMAL_HEX,event,protocol);
                return;
            }
            pingBuilder.execute(MotdType.NORMAL,event,protocol);
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            pingBuilder.execute(MotdType.OUTDATED_SERVER,event,protocol);
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            pingBuilder.execute(MotdType.OUTDATED_CLIENT,event,protocol);
        }
    }
}
