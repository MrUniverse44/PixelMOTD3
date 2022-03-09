package dev.mruniverse.pixelmotd.spigot;

import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.FileStorageBuilder;
import dev.mruniverse.pixelmotd.commons.Ping;
import dev.mruniverse.pixelmotd.commons.Priority;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.commons.enums.InitialMode;
import dev.mruniverse.pixelmotd.commons.shared.ConfigVersion;
import dev.mruniverse.pixelmotd.commons.shared.SpigotInput;
import dev.mruniverse.pixelmotd.commons.utils.Updater;
import dev.mruniverse.pixelmotd.spigot.listeners.PingListener;
import dev.mruniverse.pixelmotd.spigot.listeners.packets.PacketListener;
import dev.mruniverse.pixelmotd.spigot.listeners.whitelist.AbstractWhitelistListener;
import dev.mruniverse.pixelmotd.spigot.listeners.whitelist.type.ListenerBuilder;
import dev.mruniverse.pixelmotd.spigot.storage.Storage;
import dev.mruniverse.pixelmotd.spigot.utils.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("unused")
public final class PixelMOTD extends JavaPlugin {

    private static PixelMOTD instance;

    private AbstractWhitelistListener abstractWhitelistListener;

    private Storage storage;

    private ExternalLib externalLib;

    private Ping ping;

    private ConfigVersion configVersion;

    private boolean papi;

    @Override
    public void onEnable() {
        instance = this;
        storage = new Storage(this);
        storage.setInputManager(new SpigotInput(this));
        storage.setLogs(new GuardianLogger("PixelMOTD","dev.mruniverse.pixelmotd.", "dev.mruniverse.pixelmotd."));
        storage.setStorage(new FileStorageBuilder(storage.getLogs(), InitialMode.SPIGOT,getDataFolder(),storage.getInputManager()));
        storage.loadCommand("pmotd");
        storage.loadCommand("pixelmotd");
        papi = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        storage.updatePriority();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Metrics bukkitMetrics = new Metrics(instance, 8509);
                Control settings = storage.getFiles().getControl(GuardianFiles.SETTINGS);
                configVersion = new ConfigVersion(settings);
                storage.getLogs().debug(String.format("Spigot metrics has been enabled &7(%s)", bukkitMetrics.isEnabled()));
                boolean hasProtocol = getServer().getPluginManager().isPluginEnabled("ProtocolLib");
                String priority = settings.getString("settings.extras-event-priority", "HIGH");
                String event = settings.getString("settings.event-priority", "HIGH");

                if (configVersion.isUpdated()) {
                    storage.getLogs().info("Your configuration is updated!");
                } else {
                    storage.getLogs().info("Your configuration is outdated!");
                    configVersion.setWork(false);
                }
                abstractWhitelistListener = new ListenerBuilder(instance);

                if (settings.getStatus("settings.update-check",true)) {
                    if (settings.getStatus("settings.auto-download-updates",true)) {
                        new Updater(storage.getLogs(), getDescription().getVersion(), 37177, getDataFolder(), Updater.UpdateType.CHECK_DOWNLOAD);
                    } else {
                        new Updater(storage.getLogs(), getDescription().getVersion(), 37177, getDataFolder(), Updater.UpdateType.VERSION_CHECK);
                    }

                }

                Priority listenerPriority = Priority.getFromText(priority);
                Priority eventPriority = Priority.getFromText(event);
                if (hasProtocol) {
                    externalLib = new ProtocolLIB();
                    storage.getLogs().info("ProtocolAPI will use ProtocolLIB to get the protocol version of the player.");
                    ping = new PacketListener(instance, eventPriority);
                }
                if (!hasProtocol) {
                    ping = new PingListener(instance);
                    storage.getLogs().info("ProtocolAPI don't find ProtocolLIB in the server.");
                    storage.getLogs().info("--------------------------------------------------------------");
                    storage.getLogs().info("The outdatedClient and outdatedServer motd will not work.");
                    storage.getLogs().info("The plugin only will be using 'motds' and 'whitelist' path.");
                    storage.getLogs().info("Without ProtocolLIB the plugin will not load hover");
                    storage.getLogs().info("and the ServerPing-Protocol option will not work.");
                }

                getServer().getPluginManager().registerEvent(PlayerLoginEvent.class,abstractWhitelistListener,get(listenerPriority),abstractWhitelistListener,instance,true);
                getServer().getPluginManager().registerEvent(PlayerTeleportEvent.class,abstractWhitelistListener,get(listenerPriority),abstractWhitelistListener,instance,true);
            }
        };
        runnable.runTaskLater(this,1L);
    }

    @Override
    public void onDisable() {
        ping.getPlayerDatabase().clear();
    }

    public static PixelMOTD getInstance() {
        return instance;
    }

    public boolean hasPAPI() {
        return papi;
    }

    public Storage getStorage() {
        return storage;
    }

    public void update(Control control) {
        this.configVersion.setControl(control);
    }

    public AbstractWhitelistListener getWhitelist() {
        return abstractWhitelistListener;
    }

    public ConfigVersion getConfigVersion() {
        return configVersion;
    }

    public Ping getPing() {
        return ping;
    }

    public int getProtocolVersion(final Player player) {
        return externalLib.getProtocol(player);
    }

    public static EventPriority get(Priority priority) {
        switch (priority) {
            case HIGHEST:
                return EventPriority.HIGHEST;
            case NORMAL:
                return EventPriority.NORMAL;
            case EARLY:
            case LATE:
            case LAST:
            case FIRST:
            case HIGH:
            default:
                return EventPriority.HIGH;
            case LOWEST:
                return EventPriority.LOWEST;
            case LOW:
                return EventPriority.LOW;
            case MONITOR:
                return EventPriority.MONITOR;
        }
    }


}
