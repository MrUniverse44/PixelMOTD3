package dev.mruniverse.pixelmotd.bungeecord;

import dev.mruniverse.pixelmotd.bungeecord.listeners.PingListener;
import dev.mruniverse.pixelmotd.bungeecord.listeners.whitelist.AbstractWhitelistListener;
import dev.mruniverse.pixelmotd.bungeecord.listeners.whitelist.type.*;
import dev.mruniverse.pixelmotd.bungeecord.storage.Storage;
import dev.mruniverse.pixelmotd.bungeecord.utils.GuardianLogger;
import dev.mruniverse.pixelmotd.bungeecord.utils.Metrics;
import dev.mruniverse.pixelmotd.bungeecord.utils.ServerStatusChecker;
import dev.mruniverse.pixelmotd.global.*;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.InitialMode;
import dev.mruniverse.pixelmotd.global.shared.BungeeInput;
import dev.mruniverse.pixelmotd.global.shared.ConfigVersion;
import dev.mruniverse.pixelmotd.global.utils.Updater;
import net.md_5.bungee.api.plugin.Plugin;

public class PixelMOTDBuilder extends Plugin {
    private static PixelMOTDBuilder instance;

    private ServerStatusChecker checker = null;

    private Ping ping;

    private AbstractWhitelistListener whitelist;

    private Storage storage;

    private ConfigVersion configVersion;

    @Override
    public void onEnable() {

        instance = this;

        storage = new Storage(this);
        storage.setInputManager(new BungeeInput(this));
        storage.setLogs(new GuardianLogger(this,"PixelMOTDBuilder", "dev.mruniverse.pixelmotd."));
        storage.setStorage(new FileStorageBuilder(storage.getLogs(), InitialMode.BUNGEECORD,getDataFolder(),storage.getInputManager()));
        storage.loadCommand("pmotd");
        storage.loadCommand("pixelmotd");
        configVersion = new ConfigVersion(storage.getFiles().getControl(GuardianFiles.SETTINGS));
        ping = new PingListener(this);

        loadWhitelist();

        Metrics bukkitMetrics = new Metrics(instance, 8509);

        if(storage.getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.update-check")) {
            if (storage.getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.auto-download-updates")) {
                new Updater(storage.getLogs(), getDescription().getVersion(), 37177, getDataFolder(), Updater.UpdateType.CHECK_DOWNLOAD);
            } else {
                new Updater(storage.getLogs(), getDescription().getVersion(), 37177, getDataFolder(), Updater.UpdateType.VERSION_CHECK);
            }

        }

        if(configVersion.isUpdated()) {
            storage.getLogs().info("Your configuration is updated!");
        } else {
            storage.getLogs().info("Your configuration is outdated!");
            configVersion.setWork(false);
        }
        storage.getLogs().debug(String.format("Spigot metrics has been enabled &7(%s)", bukkitMetrics.isEnabled()));
        if(storage.getFiles().getControl(GuardianFiles.SETTINGS).getStatus("settings.server-status.toggle",false)) {
            checker = new ServerStatusChecker(this);
            checker.start();
        }
    }

    private void loadWhitelist() {
        String value = storage.getFiles().getControl(GuardianFiles.SETTINGS).getString("settings.extras-event-priority", "HIGH");
        Priority priority = Priority.getFromText(value);
        switch (priority) {
            case LOW:
                whitelist = new ListenerLow(this);
                break;
            case LOWEST:
                whitelist = new ListenerLowest(this);
                break;
            case HIGH:
                whitelist = new ListenerHigh(this);
                break;
            case MONITOR:
            case NORMAL:
                whitelist = new ListenerNormal(this);
                break;
            case HIGHEST:
                whitelist = new ListenerHighest(this);
                break;
        }
        getProxy().getPluginManager().registerListener(this,whitelist);
    }

    public void update(Control control) {
        this.configVersion.setControl(control);
    }

    public ConfigVersion getConfigVersion() {
        return configVersion;
    }

    public Ping getPing() {
        return ping;
    }

    public AbstractWhitelistListener getWhitelist() {
        return whitelist;
    }

    public ServerStatusChecker getChecker() {
        return checker;
    }

    @SuppressWarnings("unused")
    public static PixelMOTDBuilder getInstance() {
        return instance;
    }

    public Storage getStorage() {
        return storage;
    }
}
