package dev.mruniverse.pixelmotd.bungeecord.utils;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;

public class Loader {
    private final PixelMOTD plugin;

    public Loader(PixelMOTD plugin) {
        this.plugin = plugin;
    }

    public void load() {
        int max = plugin.getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers();
        plugin.setMax(max);
    }
}
