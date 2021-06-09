package dev.mruniverse.pixelmotd.spigot;

import com.comphenix.protocol.events.ListenerPriority;
import dev.mruniverse.pixelmotd.spigot.listener.CustomMotdListener;
import dev.mruniverse.pixelmotd.spigot.storage.FileStorage;
import dev.mruniverse.pixelmotd.spigot.storage.GuardianFiles;

import dev.mruniverse.pixelmotd.spigot.utils.GuardianLogger;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class PixelMOTD extends JavaPlugin {

    private GuardianLogger logger;
    private FileStorage storage;

    @Override
    public void onEnable() {
        logger = new GuardianLogger("PixelMOTD", "dev.mruniverse.pixelmotd.spigot.");
        storage = new FileStorage(this);

        new CustomMotdListener(this,getEventPriority(storage.getControl(GuardianFiles.SETTINGS).getString("settings.event-priority")));
    }

    private ListenerPriority getEventPriority(String priorityLevel) {
        try {
            if (priorityLevel == null) return ListenerPriority.HIGH;
            return ListenerPriority.valueOf(priorityLevel.toUpperCase());
        } catch (Throwable ignored) {
            return ListenerPriority.HIGH;
        }
    }

    public FileStorage getStorage() { return storage; }

    public GuardianLogger getLogs() {
        if(logger == null) logger = new GuardianLogger("PixelMOTD", "dev.mruniverse.pixelmotd.spigot.");
        return logger;
    }
}
