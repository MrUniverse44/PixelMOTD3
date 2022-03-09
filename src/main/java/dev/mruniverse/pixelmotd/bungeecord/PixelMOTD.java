package dev.mruniverse.pixelmotd.bungeecord;

import dev.mruniverse.pixelmotd.bungeecord.listeners.ping.type.*;
import dev.mruniverse.pixelmotd.bungeecord.listeners.whitelist.AbstractWhitelistListener;
import dev.mruniverse.pixelmotd.bungeecord.listeners.whitelist.type.*;
import dev.mruniverse.pixelmotd.bungeecord.storage.Storage;
import dev.mruniverse.pixelmotd.bungeecord.utils.GuardianLogger;
import dev.mruniverse.pixelmotd.bungeecord.utils.Metrics;
import dev.mruniverse.pixelmotd.bungeecord.utils.ServerStatusChecker;
import dev.mruniverse.pixelmotd.commons.*;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.commons.enums.InitialMode;
import dev.mruniverse.pixelmotd.commons.shared.BungeeInput;
import dev.mruniverse.pixelmotd.commons.shared.ConfigVersion;
import dev.mruniverse.pixelmotd.commons.utils.Updater;
import net.md_5.bungee.api.plugin.Plugin;

public class PixelMOTD extends Plugin {
    private static PixelMOTD instance;

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
        storage.setLogs(new GuardianLogger(this,"PixelMOTD", "dev.mruniverse.pixelmotd."));
        storage.setStorage(new FileStorageBuilder(storage.getLogs(), InitialMode.BUNGEECORD,getDataFolder(),storage.getInputManager()));
        storage.loadCommand("pmotd");
        storage.loadCommand("pixelmotd");

        Control settings = storage.getFiles().getControl(GuardianFiles.SETTINGS);
        configVersion = new ConfigVersion(settings);
        storage.updatePriority();

        Priority listener = Priority.getFromText(settings.getString("settings.event-priority", "HIGH"));
        switch (listener) {
            default:
            case HIGHEST:
                ping = new ProxyPingListenerHighest(this);
                break;
            case HIGH:
                ping = new ProxyPingListenerHigh(this);
                break;
            case FIRST:
            case LAST:
            case LATE:
            case EARLY:
                ping = new ProxyPingListenerNormal(this);
                GLogger logger = getStorage().getLogs();
                logger.info("You are using a priority that is for Velocity");
                logger.info("The plugin is using priority for ProxyPing: NORMAL, for Whitelist and Blacklist");
                logger.info("Please use a BungeeCord Priority");
                break;
            case MONITOR:
            case NORMAL:
                ping = new ProxyPingListenerNormal(this);
                break;
            case LOW:
                ping = new ProxyPingListenerLow(this);
                break;
            case LOWEST:
                ping = new ProxyPingListenerLowest(this);
                break;
        }

        loadWhitelist(settings);

        Metrics bungeeMetrics = new Metrics(instance, 8509);

        if (settings.getStatus("settings.update-check",true)) {
            if (settings.getStatus("settings.auto-download-updates",true)) {
                new Updater(storage.getLogs(), getDescription().getVersion(), 37177, getDataFolder(), Updater.UpdateType.CHECK_DOWNLOAD);
            } else {
                new Updater(storage.getLogs(), getDescription().getVersion(), 37177, getDataFolder(), Updater.UpdateType.VERSION_CHECK);
            }

        }

        if (configVersion.isUpdated()) {
            storage.getLogs().info("Your configuration is updated!");
        } else {
            storage.getLogs().info("Your configuration is outdated!");
            configVersion.setWork(false);
        }
        storage.getLogs().debug(String.format("Bungee metrics has been enabled &7(%s)", bungeeMetrics.isEnabled()));
        if (settings.getStatus("settings.server-status.toggle",false)) {
            checker = new ServerStatusChecker(this);
            checker.start();
        }
    }

    private void loadWhitelist(Control control) {
        String value = control.getString("settings.extras-event-priority", "HIGH");
        Priority priority = Priority.getFromText(value);
        switch (priority) {
            case LOW:
                whitelist = new WhitelistListenerLow(this);
                break;
            case LOWEST:
                whitelist = new WhitelistListenerLowest(this);
                break;
            case HIGH:
                whitelist = new WhitelistListenerHigh(this);
                break;
            case FIRST:
            case LAST:
            case LATE:
            case EARLY:
                whitelist = new WhitelistListenerNormal(this);
                GLogger logger = getStorage().getLogs();
                logger.info("You are using a priority that is for Velocity");
                logger.info("The plugin is using priority Whitelist / Blacklist Events: NORMAL, for Whitelist and Blacklist");
                logger.info("Please use a BungeeCord Priority");
                break;
            case MONITOR:
            case NORMAL:
                whitelist = new WhitelistListenerNormal(this);
                break;
            case HIGHEST:
                whitelist = new WhitelistListenerHighest(this);
                break;
        }
        getProxy().getPluginManager().registerListener(this,whitelist);
    }

    @Override
    public void onDisable() {
        ping.getPlayerDatabase().clear();
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
    public static PixelMOTD getInstance() {
        return instance;
    }

    public Storage getStorage() {
        return storage;
    }
}
