package dev.mruniverse.pixelmotd.velocity.storage;

import dev.mruniverse.pixelmotd.commons.PluginStorage;
import dev.mruniverse.pixelmotd.velocity.PixelMOTD;

public class Storage extends PluginStorage {
    private final PixelMOTD builder;

    public Storage(PixelMOTD builder) {
        this.builder = builder;
    }

    public void loadCommand(String command) {
        //TODO
    }


}
