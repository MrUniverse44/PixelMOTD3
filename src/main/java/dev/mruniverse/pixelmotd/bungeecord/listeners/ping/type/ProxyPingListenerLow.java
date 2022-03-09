package dev.mruniverse.pixelmotd.bungeecord.listeners.ping.type;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.listeners.ping.AbstractPingListener;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxyPingListenerLow extends AbstractPingListener implements Listener {

    public ProxyPingListenerLow(PixelMOTD plugin) {
        super(plugin);
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPing(final ProxyPingEvent event) {
        execute(event);
    }
}
