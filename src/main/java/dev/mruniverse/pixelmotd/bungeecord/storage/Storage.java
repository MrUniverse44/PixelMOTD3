package dev.mruniverse.pixelmotd.bungeecord.storage;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.utils.command.MainCommand;
import dev.mruniverse.pixelmotd.commons.PluginStorage;

public class Storage extends PluginStorage {
    private final PixelMOTD plugin;

    public Storage(PixelMOTD plugin) {
        this.plugin = plugin;
    }

    public void loadCommand(String command) {
        plugin.getProxy().getPluginManager().registerCommand(plugin, new MainCommand(plugin, command));
    }
}