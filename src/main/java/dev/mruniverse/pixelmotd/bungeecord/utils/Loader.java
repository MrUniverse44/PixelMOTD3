package dev.mruniverse.pixelmotd.bungeecord.utils;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.motd.CustomMotdListener;
import dev.mruniverse.pixelmotd.bungeecord.storage.FileStorage;
import dev.mruniverse.pixelmotd.bungeecord.utils.command.MainCommand;

public class Loader {
    private final PixelMOTD plugin;
    private CustomMotdListener motdListener = null;

    public Loader(PixelMOTD plugin) {
        this.plugin = plugin;
    }

    public void load() {
        int max = plugin.getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers();
        plugin.setMax(max);

        plugin.setLogger(new GuardianLogger(plugin,"PixelMOTD", "dev.mruniverse.pixelmotd.bungeecord."));

        FileStorage currentStorage = new FileStorage(plugin);

        plugin.setStorage(currentStorage);

        new Metrics(plugin, 8509);

        motdListener = new CustomMotdListener(plugin);
    }

    public void loadCommand(String command) {
        plugin.getProxy().getPluginManager().registerCommand(plugin, new MainCommand(plugin, command));
    }

    public CustomMotdListener getMotdListener() {
        return motdListener;
    }

}
