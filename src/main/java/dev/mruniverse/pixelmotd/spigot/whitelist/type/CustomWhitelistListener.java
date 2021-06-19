package dev.mruniverse.pixelmotd.spigot.whitelist.type;

import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.whitelist.AbstractWhitelistListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class CustomWhitelistListener extends AbstractWhitelistListener implements EventExecutor, Listener {

    public CustomWhitelistListener(PixelMOTD plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        try {
            if(event instanceof PlayerLoginEvent) {
                checkPlayer((PlayerLoginEvent)event);
            }
        }catch (Throwable ignored) {}
    }
}
