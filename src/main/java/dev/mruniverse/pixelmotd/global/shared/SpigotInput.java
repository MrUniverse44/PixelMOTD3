package dev.mruniverse.pixelmotd.global.shared;

import dev.mruniverse.pixelmotd.global.InputManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;

public class SpigotInput implements InputManager {
    private final JavaPlugin plugin;

    public SpigotInput(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public InputStream getInputStream(String resource) {
        return plugin.getResource(resource);
    }

    @Override
    public boolean isBungee() {
        return false;
    }
}
