package dev.mruniverse.pixelmotd.bungeecord.listeners.ping.type;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.listeners.ping.AbstractPingListener;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxyPingListenerNormal extends AbstractPingListener implements Listener {

    public ProxyPingListenerNormal(PixelMOTD plugin) {
        super(plugin);
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPing(final ProxyPingEvent event) {
        execute(event);
    }
}
