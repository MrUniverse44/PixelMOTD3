package dev.mruniverse.pixelmotd.spigot.listeners;

import dev.mruniverse.pixelmotd.spigot.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.spigot.utils.Ping;
import dev.mruniverse.pixelmotd.spigot.utils.Priority;
import org.bukkit.event.*;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener, Ping {

    private final PixelMOTDBuilder plugin;

    public PingListener(PixelMOTDBuilder plugin, Priority priority) {
        this.plugin = plugin;

    }

    @Override
    public void update() {

    }

    @EventHandler
    public void onMotd(ServerListPingEvent event) {

    }

    @Override
    public void setWhitelist(boolean status) {

    }

    public static EventPriority get(Priority priority) {
        switch (priority) {
            case HIGHEST:
                return EventPriority.HIGHEST;
            case NORMAL:
                return EventPriority.NORMAL;
            default:
            case HIGH:
                return EventPriority.HIGH;
            case LOWEST:
                return EventPriority.LOWEST;
            case LOW:
                return EventPriority.LOW;
            case MONITOR:
                return EventPriority.MONITOR;
        }
    }
}
