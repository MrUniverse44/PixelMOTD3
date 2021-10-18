package dev.mruniverse.pixelmotd.bungeecord;

import dev.mruniverse.pixelmotd.bungeecord.storage.Storage;
import dev.mruniverse.pixelmotd.bungeecord.utils.GuardianLogger;
import dev.mruniverse.pixelmotd.global.FileStorageBuilder;
import dev.mruniverse.pixelmotd.global.shared.BungeeInput;
import net.md_5.bungee.api.plugin.Plugin;

public class PixelMOTDBuilder extends Plugin {
    private Storage storage;

    @Override
    public void onEnable() {
        storage = new Storage(this);
        storage.setInputManager(new BungeeInput(this));
        storage.setStorage(new FileStorageBuilder(storage.getLogs(),getDataFolder(),storage.getInputManager()));
        storage.setLogs(new GuardianLogger(this,"PixelMOTDBuilder", "dev.mruniverse.pixelmotd."));
        storage.loadCommand("pmotd");
        storage.loadCommand("pixelmotd");
    }

    public Storage getStorage() {
        return storage;
    }
}
