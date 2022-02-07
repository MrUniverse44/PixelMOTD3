package dev.mruniverse.pixelmotd.spigot.storage;

import dev.mruniverse.pixelmotd.global.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.spigot.utils.command.MainCommand;
import org.bukkit.command.PluginCommand;


public class Storage extends PixelMOTD {
    private final PixelMOTDBuilder plugin;

    public Storage(PixelMOTDBuilder plugin) {
        this.plugin = plugin;
    }

    public void loadCommand(String command) {
        PluginCommand cmd = plugin.getCommand(command);
        if (cmd == null) return;
        cmd.setExecutor(new MainCommand(plugin,command));
    }
}
