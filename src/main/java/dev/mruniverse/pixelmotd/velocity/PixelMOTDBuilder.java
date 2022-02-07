package dev.mruniverse.pixelmotd.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.FileStorageBuilder;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.InitialMode;
import dev.mruniverse.pixelmotd.global.shared.ConfigVersion;
import dev.mruniverse.pixelmotd.global.shared.VelocityInput;
import dev.mruniverse.pixelmotd.velocity.listeners.MotdBuilder;

import java.nio.file.Path;

import dev.mruniverse.pixelmotd.velocity.listeners.PingListener;
import dev.mruniverse.pixelmotd.velocity.storage.Storage;
import dev.mruniverse.pixelmotd.velocity.utils.GuardianVelocityLogger;
import dev.mruniverse.pixelmotd.velocity.utils.Metrics;
import org.slf4j.Logger;

@SuppressWarnings("unused")
@Plugin(
        id = "pixelmotd",
        name = "PixelMOTDBuilder",
        version = "9.0.0",
        description = "Simple Motd Plugin",
        url = "darkness.studios",
        authors = {"MrUniverse44"}
)
public class PixelMOTDBuilder {

    @Inject
    private final ProxyServer server;
    @Inject
    private final Logger logger;

    private final Storage storage;

    private final ConfigVersion configVersion;

    // path of the plugin
    @Inject
    private @DataDirectory
    Path dataDirectory;

    private final Metrics.Factory metricsFactory;

    // connect to the server and logger
    @Inject
    public PixelMOTDBuilder(ProxyServer server, Logger logger,Metrics.Factory metricsFactory) {
        this.server = server;
        this.logger = logger;
        this.metricsFactory = metricsFactory;

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
