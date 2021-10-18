package dev.mruniverse.pixelmotd.bungeecord.storage;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.bungeecord.utils.command.MainCommand;
import dev.mruniverse.pixelmotd.global.PixelMOTD;

public class Storage extends PixelMOTD {
    private final PixelMOTDBuilder plugin;

    public Storage(PixelMOTDBuilder plugin) {
        this.plugin = plugin;
    }

    public void loadCommand(String command) {
        plugin.getProxy().getPluginManager().registerCommand(plugin, new MainCommand(plugin, command));
    }
}