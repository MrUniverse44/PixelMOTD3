package dev.mruniverse.pixelmotd.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.FileStorageBuilder;
import dev.mruniverse.pixelmotd.commons.Ping;
import dev.mruniverse.pixelmotd.commons.Priority;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.commons.enums.InitialMode;
import dev.mruniverse.pixelmotd.commons.shared.ConfigVersion;
import dev.mruniverse.pixelmotd.commons.shared.VelocityInput;

import dev.mruniverse.pixelmotd.velocity.listeners.whitelist.AbstractWhitelistListener;
import dev.mruniverse.pixelmotd.velocity.listeners.whitelist.type.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.nio.file.Path;

import dev.mruniverse.pixelmotd.velocity.listeners.PingListener;
import dev.mruniverse.pixelmotd.velocity.storage.Storage;
import dev.mruniverse.pixelmotd.velocity.utils.GuardianVelocityLogger;
import dev.mruniverse.pixelmotd.velocity.utils.Metrics;
import org.slf4j.Logger;

@SuppressWarnings("unused")
@Plugin(
        id = "pixelmotd",
        name = "PixelMOTD",
        version = "9.2.4-SNAPSHOT",
        description = "Simple Motd Plugin",
        url = "https://github.com/MrUniverse44/PixelMOTD3",
        authors = {"MrUniverse44"}
)
public class PixelMOTD {

    @Inject
    private ProxyServer server;

    @Inject
    private Logger logger;

    private Storage storage;

    private Ping ping;

    private ConfigVersion configVersion;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    private File pluginFolder = null;

    @Inject
    private CommandManager commandManager;

    @Inject
    private Metrics.Factory metricsFactory;

    public @NonNull Path dataDirectory() {
        return this.dataDirectory;
    }

    public Storage getStorage() {
        return storage;
    }

    public Ping getPing() {
        return ping;
    }

    public ProxyServer getServer() {
        return server;
    }

    public void update(Control control) {
        this.configVersion.setControl(control);
    }

    public ConfigVersion getConfigVersion() {
        return configVersion;
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {

        File principal = dataDirectory.toFile();

        pluginFolder = new File(principal.getParentFile(), "PixelMOTD");

        if (!pluginFolder.exists()) {
            if (pluginFolder.mkdirs()) {
                logger.info("Plugin folder created!");
            }
        }

        storage = new Storage(this);
        storage.setInputManager(new VelocityInput(this));
        storage.setLogs(new GuardianVelocityLogger(server,"PixelMOTD", "dev.mruniverse.pixelmotd."));
        storage.setStorage(new FileStorageBuilder(storage.getLogs(), InitialMode.VELOCITY, pluginFolder, storage.getInputManager()));
        storage.updatePriority();
        configVersion = new ConfigVersion(storage.getFiles().getControl(GuardianFiles.SETTINGS));

        if (configVersion.isUpdated()) {
            storage.getLogs().info("Your configuration is updated!");
        } else {
            storage.getLogs().info("Your configuration is outdated!");
            configVersion.setWork(false);
        }

        ping = new PingListener(this);

        server.getEventManager().register(
                this,
                getPriority(
                        Priority.getFromText(
                                storage.getFiles().getControl(GuardianFiles.SETTINGS).getString(
                                        "settings",
                                        "NORMAL"
                                )
                        )
                )
        );
        server.getEventManager().register(this, ping);

        Metrics metrics = metricsFactory.make(this,8509);

        logger.debug("Spigot metrics has been enabled (True)");

        logger.error("PixelMOTD have been successfully enabled! but isn't finished in Velocity");
        logger.error("Velocity support isn't finished so commands,features don't work yet");
        logger.error("So the plugin don't have files generated for velocity for now, sorry.");
        logger.error("For now i recommend use another Motd plugin for velocity");
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        ping.getPlayerDatabase().clear();
    }

    public File getPluginFolder() {
        return pluginFolder;
    }

    public AbstractWhitelistListener getPriority(Priority priority) {
        switch (priority) {
            case HIGHEST:
            case HIGH:
            case LOWEST:
            case LOW:
            case MONITOR:
            default:
                logger.info("You are using a priority that is not for Velocity");
                logger.info("The plugin is using priority: NORMAL, for Whitelist and Blacklist");
                return new PlayerConnectNormal(this);
            case EARLY:
                return new PlayerConnectEarly(this);
            case LATE:
                return new PlayerConnectLate(this);
            case LAST:
                return new PlayerConnectLast(this);
            case FIRST:
                return new PlayerConnectFirst(this);
            case NORMAL:
                return new PlayerConnectNormal(this);
        }
    }

}
