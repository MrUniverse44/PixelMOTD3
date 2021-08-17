package dev.mruniverse.pixelmotd.velocity;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import org.slf4j.Logger;

@Plugin(
        id = "pixelmotd",
        name = "PixelMOTD",
        version = "9.0.0",
        description = "Simple Motd Plugin",
        url = "darkness.studios",
        authors = {"MrUniverse44"}
)
public class PixelMOTD {
    private final ProxyServer server;
    private final Logger logger;
    private final CommandManager commandManager;
    private final Path dataDirectory;
    private final Injector injector;

    @Inject
    public PixelMOTD(
        final @NonNull ProxyServer server,
        final @NonNull Logger logger,
        final @NonNull CommandManager manager,
        @DataDirectory final @NonNull Path dataDirectory,
        final @NonNull Injector injector
    ) {
        this.server = server;
        this.logger = logger;
        this.commandManager = manager;
        this.dataDirectory = dataDirectory;
        this.injector = injector;
        logger.error("PixelMOTD have been successfully enabled! but isn't finished in Velocity");
        logger.error("Velocity support isn't finished so commands,features don't work yet");
        logger.error("So the plugin don't have files generated for velocity for now, sorry.");
        logger.error("For now i recommend use another Motd plugin for velocity");
    }
}
