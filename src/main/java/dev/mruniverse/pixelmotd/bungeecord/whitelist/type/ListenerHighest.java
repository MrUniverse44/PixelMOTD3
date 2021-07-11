package dev.mruniverse.pixelmotd.bungeecord.whitelist.type;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.whitelist.AbstractWhitelistListener;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ListenerHighest extends AbstractWhitelistListener implements Listener {
    public ListenerHighest(PixelMOTD plugin) {

        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(LoginEvent event) {
        checkPlayer(event);
    }
}
