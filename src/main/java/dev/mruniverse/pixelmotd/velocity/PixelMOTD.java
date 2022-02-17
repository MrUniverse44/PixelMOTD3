package dev.mruniverse.pixelmotd.velocity;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.FileStorageBuilder;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.commons.enums.InitialMode;
import dev.mruniverse.pixelmotd.commons.shared.ConfigVersion;
import dev.mruniverse.pixelmotd.commons.shared.VelocityInput;

import org.checkerframework.checker.nullness.qual.NonNull;

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
        version = "9.2.3.1-SNAPSHOT",
        description = "Simple Motd Plugin",
        url = "https://github.com/MrUniverse44/PixelMOTD3",
        authors = {"MrUniverse44"}
)
public class PixelMOTD {

    private final ProxyServer server;

    private final Logger logger;

    private final Storage storage;

    private final ConfigVersion configVersion;

    private final Path dataDirectory;

    private final CommandManager commandManager;

    private final Metrics.Factory metricsFactory;

    // connect to the server and logger
    @Inject
    public PixelMOTD(
            final ProxyServer server,
            final Logger logger,
            final CommandManager commandManager,
            @DataDirectory final @NonNull Path dataDirectory,
            final Metrics.Factory metricsFactory,
            final @NonNull Injector injector
            ) {
        this.server = server;
        this.logger = logger;
        this.metricsFactory = metricsFactory;
        this.commandManager = commandManager;
        this.dataDirectory = dataDirectory;

        storage = new Storage(this);
        storage.setInputManager(new VelocityInput(this));
        storage.setLogs(new GuardianVelocityLogger(server,"PixelMOTD", "dev.mruniverse.pixelmotd."));
        storage.setStorage(new FileStorageBuilder(storage.getLogs(), InitialMode.VELOCITY, dataDirectory.toFile(), storage.getInputManager()));
        storage.updatePriority();
        configVersion = new ConfigVersion(storage.getFiles().getControl(GuardianFiles.SETTINGS));

        if (configVersion.isUpdated()) {
            storage.getLogs().info("Your configuration is updated!");
        } else {
            storage.getLogs().info("Your configuration is outdated!");
            configVersion.setWork(false);
        }
    }

    public @NonNull Path dataDirectory() {
        return this.dataDirectory;
    }

    public Storage getStorage() {
        return storage;
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

        server.getEventManager().register(this,new PingListener(this));

        Metrics metrics = metricsFactory.make(this,8509);

        logger.debug("Spigot metrics has been enabled (True)");

        logger.error("PixelMOTD have been successfully enabled! but isn't finished in Velocity");
        logger.error("Velocity support isn't finished so commands,features don't work yet");
        logger.error("So the plugin don't have files generated for velocity for now, sorry.");
        logger.error("For now i recommend use another Motd plugin for velocity");
    }

}
