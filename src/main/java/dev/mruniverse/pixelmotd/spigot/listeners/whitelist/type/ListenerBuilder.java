package dev.mruniverse.pixelmotd.spigot.listeners.whitelist.type;

import dev.mruniverse.pixelmotd.spigot.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.spigot.listeners.whitelist.AbstractWhitelistListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class ListenerBuilder extends AbstractWhitelistListener implements EventExecutor, Listener {

    public ListenerBuilder(PixelMOTDBuilder plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        if (event instanceof PlayerLoginEvent) {
            checkPlayer((PlayerLoginEvent)event);
            return;
        }
        if (event instanceof PlayerTeleportEvent) {
            checkPlayer((PlayerTeleportEvent)event);
        }
    }
}
