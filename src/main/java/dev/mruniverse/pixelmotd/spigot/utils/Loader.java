package dev.mruniverse.pixelmotd.spigot.utils;

import com.comphenix.protocol.events.ListenerPriority;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.motd.CustomMotdListener;
import dev.mruniverse.pixelmotd.spigot.storage.FileStorage;

public class Loader {
    private final PixelMOTD plugin;
    private CustomMotdListener motdListener = null;
    public Loader(PixelMOTD plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.setLogger(new GuardianLogger("PixelMOTD", "dev.mruniverse.pixelmotd.spigot."));

        FileStorage currentStorage = new FileStorage(plugin);

        plugin.setStorage(currentStorage);

        new Metrics(plugin, 8509);

        motdListener = new CustomMotdListener(plugin,getEventPriority(currentStorage.getControl(GuardianFiles.SETTINGS).getString("settings.event-priority")));
    }

    public CustomMotdListener getMotdListener() {
        return motdListener;
    }

    private ListenerPriority getEventPriority(String priorityLevel) {
        try {
            if (priorityLevel == null) return ListenerPriority.HIGH;
            return ListenerPriority.valueOf(priorityLevel.toUpperCase());
        } catch (Throwable ignored) {
            return ListenerPriority.HIGH;
        }
    }
}
