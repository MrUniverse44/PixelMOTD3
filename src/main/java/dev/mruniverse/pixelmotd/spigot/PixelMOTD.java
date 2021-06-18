package dev.mruniverse.pixelmotd.spigot;

import dev.mruniverse.pixelmotd.spigot.storage.FileStorage;

import dev.mruniverse.pixelmotd.spigot.utils.GuardianLogger;
import dev.mruniverse.pixelmotd.spigot.utils.Loader;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class PixelMOTD extends JavaPlugin {

    private GuardianLogger logger;
    private FileStorage storage;

    @Override
    public void onEnable() {
        new Loader(this).load();
    }

    public void setStorage(FileStorage storage) { this.storage = storage; }
    public void setLogger(GuardianLogger logger) { this.logger = logger; }

    public FileStorage getStorage() { return storage; }

    public GuardianLogger getLogs() {
        if(logger == null) logger = new GuardianLogger("PixelMOTD", "dev.mruniverse.pixelmotd.spigot.");
        return logger;
    }
}
