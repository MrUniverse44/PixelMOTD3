package dev.mruniverse.pixelmotd.bungeecord.whitelist.type;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.whitelist.AbstractWhitelistListener;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ListenerLowest extends AbstractWhitelistListener implements Listener {
    public ListenerLowest(PixelMOTD plugin) {

        super(plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(LoginEvent event) {
        checkPlayer(event);
    }
}
