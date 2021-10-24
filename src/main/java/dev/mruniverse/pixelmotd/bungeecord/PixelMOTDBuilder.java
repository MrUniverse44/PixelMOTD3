package dev.mruniverse.pixelmotd.bungeecord;

import dev.mruniverse.pixelmotd.bungeecord.storage.Storage;
import dev.mruniverse.pixelmotd.bungeecord.utils.GuardianLogger;
import dev.mruniverse.pixelmotd.bungeecord.utils.ServerStatusChecker;
import dev.mruniverse.pixelmotd.global.FileStorageBuilder;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.shared.BungeeInput;
import net.md_5.bungee.api.plugin.Plugin;

public class PixelMOTDBuilder extends Plugin {
    private static PixelMOTDBuilder instance;

    private ServerStatusChecker checker = null;

    private Storage storage;

    @Override
    public void onEnable() {
        instance = this;
        storage = new Storage(this);
        storage.setInputManager(new BungeeInput(this));
        storage.setStorage(new FileStorageBuilder(storage.getLogs(),getDataFolder(),storage.getInputManager()));
        storage.setLogs(new GuardianLogger(this,"PixelMOTD", "dev.mruniverse.pixelmotd."));
        storage.loadCommand("pmotd");
        storage.loadCommand("pixelmotd");
        if(storage.getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.server-status.toggle",false)) {
            checker = new ServerStatusChecker(this);
            checker.start();
        }
    }

    public ServerStatusChecker getChecker() {
        return checker;
    }

    public static PixelMOTDBuilder getInstance() {
        return instance;
    }

    public Storage getStorage() {
        return storage;
    }
}
