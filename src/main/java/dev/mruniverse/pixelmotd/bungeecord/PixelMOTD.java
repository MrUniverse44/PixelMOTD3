package dev.mruniverse.pixelmotd.bungeecord;

import dev.mruniverse.pixelmotd.bungeecord.motd.CustomMotdListener;
import dev.mruniverse.pixelmotd.bungeecord.storage.FileStorage;
import dev.mruniverse.pixelmotd.bungeecord.utils.GuardianLogger;
import dev.mruniverse.pixelmotd.bungeecord.utils.Loader;
import dev.mruniverse.pixelmotd.bungeecord.whitelist.AbstractWhitelistListener;
import dev.mruniverse.pixelmotd.bungeecord.whitelist.type.*;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class PixelMOTD extends Plugin {
    private Loader loader;
    private GuardianLogger logger;
    private FileStorage storage;

    private int max;

    @Override
    public void onEnable() {
        loader = new Loader(this);

        loader.load();

        loader.loadCommand("pmotd");

        loader.loadCommand("pixelmotd");

        AbstractWhitelistListener abstractWhitelistListener;

        PriorityLevel customExtraPriority = getEventPriority(storage.getControl(GuardianFiles.SETTINGS).getString("settings.extras-event-priority"));

        PluginManager pluginManager = getProxy().getPluginManager();

        switch (customExtraPriority) {
            case LOW:
                abstractWhitelistListener = new ListenerLow(this);
                break;
            case LOWEST:
                abstractWhitelistListener = new ListenerLowest(this);
                break;
            case HIGH:
                abstractWhitelistListener = new ListenerHigh(this);
                break;
            case HIGHEST:
                abstractWhitelistListener = new ListenerHighest(this);
                break;
            default:
            case NORMAL:
                abstractWhitelistListener = new ListenerNormal(this);
                break;
        }

        pluginManager.registerListener(this,abstractWhitelistListener);

    }

    @Override
    public void onDisable() {
        loader.unloadPlayers();
    }

    private PriorityLevel getEventPriority(String priorityLevel) {
        try {
            if (priorityLevel == null) return PriorityLevel.NORMAL;
            return PriorityLevel.valueOf(priorityLevel.toUpperCase());
        } catch (Throwable ignored) {
            return PriorityLevel.NORMAL;
        }
    }

    public enum PriorityLevel {
        NORMAL,
        LOW,
        LOWEST,
        HIGH,
        HIGHEST
    }

    public void setMax(int value) {
        max = value;
    }

    public int getMax() {
        return max;
    }

    public void setStorage(FileStorage storage) { this.storage = storage; }
    public void setLogger(GuardianLogger logger) { this.logger = logger; }

    public Loader getLoader() { return loader; }

    public FileStorage getStorage() { return storage; }

    public GuardianLogger getLogs() {
        if(logger == null) logger = new GuardianLogger(this,"PixelMOTD", "dev.mruniverse.pixelmotd.spigot.");
        return logger;
    }
}
