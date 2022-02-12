package dev.mruniverse.pixelmotd.spigot.storage;

import dev.mruniverse.pixelmotd.commons.PluginStorage;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.utils.command.MainCommand;
import org.bukkit.command.PluginCommand;


public class Storage extends PluginStorage {
    private final PixelMOTD plugin;

    public Storage(PixelMOTD plugin) {
        this.plugin = plugin;
    }

    public void loadCommand(String command) {
        PluginCommand cmd = plugin.getCommand(command);
        if (cmd == null) return;
        cmd.setExecutor(new MainCommand(plugin,command));
    }
}
