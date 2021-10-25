package dev.mruniverse.pixelmotd.bungeecord;

import dev.mruniverse.pixelmotd.bungeecord.listeners.PingListener;
import dev.mruniverse.pixelmotd.bungeecord.storage.Storage;
import dev.mruniverse.pixelmotd.bungeecord.utils.GuardianLogger;
import dev.mruniverse.pixelmotd.bungeecord.utils.Metrics;
import dev.mruniverse.pixelmotd.bungeecord.utils.ServerStatusChecker;
import dev.mruniverse.pixelmotd.global.FileStorageBuilder;
import dev.mruniverse.pixelmotd.global.Ping;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.shared.BungeeInput;
import net.md_5.bungee.api.plugin.Plugin;

public class PixelMOTDBuilder extends Plugin {
    private static PixelMOTDBuilder instance;

    private ServerStatusChecker checker = null;

    private Ping ping;

    private Storage storage;

    @Override
    public void onEnable() {
        instance = this;
        storage = new Storage(this);
        storage.setInputManager(new BungeeInput(this));
        storage.setLogs(new GuardianLogger(this,"PixelMOTD", "dev.mruniverse.pixelmotd."));
        storage.setStorage(new FileStorageBuilder(storage.getLogs(),getDataFolder(),storage.getInputManager()));
        storage.loadCommand("pmotd");
        storage.loadCommand("pixelmotd");
        ping = new PingListener(this);
        Metrics bukkitMetrics = new Metrics(instance, 8509);
        storage.getLogs().debug(String.format("Spigot metrics has been enabled &7(%s)", bukkitMetrics.isEnabled()));
        if(storage.getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.server-status.toggle",false)) {
            checker = new ServerStatusChecker(this);
            checker.start();
        }
    }

    public Ping getPing() {
        return ping;
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
