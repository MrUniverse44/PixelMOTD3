package dev.mruniverse.pixelmotd.spigot.listeners;

import dev.mruniverse.pixelmotd.spigot.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.spigot.utils.Ping;
import dev.mruniverse.pixelmotd.spigot.utils.Priority;
import org.bukkit.event.Listener;

public class PingListener implements Listener, Ping {

    private final PixelMOTDBuilder plugin;

    public PingListener(PixelMOTDBuilder plugin, Priority priority) {
        this.plugin = plugin;
    }

    @Override
    public void update() {

    }

    @Override
    public void setWhitelist(boolean status) {

    }

}
