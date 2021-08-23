package dev.mruniverse.pixelmotd.spigot;

import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.utils.Updater;
import dev.mruniverse.pixelmotd.spigot.storage.FileStorage;

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
    private AbstractWhitelistListener abstractWhitelistListener;

    @Override
    public void onEnable() {
        loader = new Loader(this);

        loader.load();

        loader.loadCommand("pmotd");

        loader.loadCommand("pixelmotd");

        abstractWhitelistListener = new CustomWhitelistListener(this);

        EventPriority customExtraPriority = getEventPriority(storage.getControl(GuardianFiles.SETTINGS).getString("settings.extras-event-priority"));

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvent(PlayerLoginEvent.class, abstractWhitelistListener, customExtraPriority, abstractWhitelistListener, this, true);

        if(getStorage().getControl(GuardianFiles.SETTINGS).getBoolean("settings.update-check",true)) {

            Updater updater;

            if (getStorage().getControl(GuardianFiles.SETTINGS).getBoolean("settings.auto-download-updates", true)) {
                updater = new Updater(getLogs(), getDescription().getVersion(), 37177, getDataFolder(), Updater.UpdateType.DOWNLOAD);
            } else {
                updater = new Updater(getLogs(), getDescription().getVersion(), 37177, getDataFolder(), Updater.UpdateType.VERSION_CHECK);
            }
            switch (updater.getResult()) {
                case FAILED:
                    getLogs().info("Can't download or check latest version of the plugin :( download/check it automatically from website");
                    break;
                case BAD_ID:
                    getLogs().info("We don't found the PluginID ¿was the plugin removed? can't download or check updates :(");
                    break;
                case NO_UPDATE:
                    getLogs().info("You're updated ¡Yey! ;) Thanks for using my plugins.");
                    break;
                case SUCCESS:
                    getLogs().info("The latest update has been downloaded in /downloads/ folder in Plugin Folder.");
                    break;
                case UPDATE_FOUND:
                    getLogs().info("A new update is available");
                    getLogs().info("Download update from PixelMOTD's spigotmc, if you want download it automatically toggle auto-download option in the settings file.");
                    break;
            }
        }

    }

    public AbstractWhitelistListener getListener() { return abstractWhitelistListener; }

    @Override
    public void onDisable() {
        loader.unloadPlayers();
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
