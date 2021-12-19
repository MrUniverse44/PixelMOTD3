package dev.mruniverse.pixelmotd.global.shared;

import dev.mruniverse.pixelmotd.global.InputManager;
import dev.mruniverse.pixelmotd.velocity.PixelMOTDBuilder;

import java.io.InputStream;

public class VelocityInput implements InputManager {
    private final PixelMOTDBuilder plugin;

    public VelocityInput(PixelMOTDBuilder plugin) {
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
