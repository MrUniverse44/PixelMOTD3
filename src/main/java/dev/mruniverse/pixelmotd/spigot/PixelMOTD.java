package dev.mruniverse.pixelmotd.spigot;

import dev.mruniverse.pixelmotd.spigot.storage.FileStorage;

import dev.mruniverse.pixelmotd.spigot.storage.GuardianFiles;
import dev.mruniverse.pixelmotd.spigot.utils.GuardianLogger;
import dev.mruniverse.pixelmotd.spigot.utils.Loader;
import dev.mruniverse.pixelmotd.spigot.whitelist.AbstractWhitelistListener;
import dev.mruniverse.pixelmotd.spigot.whitelist.type.CustomWhitelistListener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class PixelMOTD extends JavaPlugin {

    private Loader loader;
    private GuardianLogger logger;
    private FileStorage storage;

    @Override
    public void onEnable() {
        loader = new Loader(this);

        loader.load();

        AbstractWhitelistListener abstractWhitelistListener = new CustomWhitelistListener(this);

        EventPriority customExtraPriority = getEventPriority(storage.getControl(GuardianFiles.SETTINGS).getString("settings.extras-event-priority"));

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvent(PlayerLoginEvent.class, abstractWhitelistListener,customExtraPriority, abstractWhitelistListener,this,true);

    }

    private EventPriority getEventPriority(String priorityLevel) {
        try {
            if (priorityLevel == null) return EventPriority.NORMAL;
            return EventPriority.valueOf(priorityLevel.toUpperCase());
        } catch (Throwable ignored) {
            return EventPriority.NORMAL;
        }
    }

    public void setStorage(FileStorage storage) { this.storage = storage; }
    public void setLogger(GuardianLogger logger) { this.logger = logger; }

    public Loader getLoader() { return loader; }

    public FileStorage getStorage() { return storage; }

    public GuardianLogger getLogs() {
        if(logger == null) logger = new GuardianLogger("PixelMOTD", "dev.mruniverse.pixelmotd.spigot.");
        return logger;
    }
}
