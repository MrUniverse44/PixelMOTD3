package dev.mruniverse.pixelmotd.spigot;

import dev.mruniverse.pixelmotd.global.FileStorageBuilder;
import dev.mruniverse.pixelmotd.global.shared.SpigotInput;
import dev.mruniverse.pixelmotd.spigot.storage.Storage;
import dev.mruniverse.pixelmotd.spigot.utils.GuardianLogger;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class PixelMOTDBuilder extends JavaPlugin {

    private Storage storage;

    @Override
    public void onEnable() {
        storage = new Storage(this);
        storage.setInputManager(new SpigotInput(this));
        storage.setStorage(new FileStorageBuilder(storage.getLogs(),getDataFolder(),storage.getInputManager()));
        storage.setLogs(new GuardianLogger("PixelMOTDBuilder","PixelMOTDBuilder", "dev.mruniverse.pixelmotd."));
        storage.loadCommand("pmotd");
        storage.loadCommand("pixelmotd");
    }

    public Storage getStorage() {
        return storage;
    }


}
