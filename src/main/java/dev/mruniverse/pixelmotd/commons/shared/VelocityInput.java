package dev.mruniverse.pixelmotd.commons.shared;

import dev.mruniverse.pixelmotd.commons.InputManager;
import dev.mruniverse.pixelmotd.velocity.PixelMOTD;

import java.io.InputStream;

public class VelocityInput implements InputManager {
    private final PixelMOTD plugin;

    public VelocityInput(PixelMOTD plugin) {
        this.plugin = plugin;
    }

    @Override
    public InputStream getInputStream(String resource) {
        return plugin.getClass().getResourceAsStream(resource);
    }

    @Override
    public boolean isBungee() {
        return false;
    }
}
