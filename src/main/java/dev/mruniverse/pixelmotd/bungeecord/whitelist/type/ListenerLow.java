package dev.mruniverse.pixelmotd.bungeecord.whitelist.type;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.whitelist.AbstractWhitelistListener;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ListenerLow extends AbstractWhitelistListener implements Listener {
    public ListenerLow(PixelMOTD plugin) {

        super(plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(LoginEvent event) {
        checkPlayer(event);
    }
}
