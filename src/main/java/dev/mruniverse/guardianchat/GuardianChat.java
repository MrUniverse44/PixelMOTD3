package dev.mruniverse.guardianchat;

import dev.mruniverse.guardianchat.listener.CustomChatListener;
import dev.mruniverse.guardianchat.listener.abstracts.AbstractCustomChatListener;
import dev.mruniverse.guardianchat.storage.FileStorage;
import dev.mruniverse.guardianchat.storage.GuardianFiles;
import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.utils.ExternalLogger;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class GuardianChat extends JavaPlugin {

    private ExternalLogger logger;
    private FileStorage storage;
    private AbstractCustomChatListener abstractCustomChatListener;

    @Override
    public void onEnable() {
        logger = GuardianLIB.initLogger(this, "GuardianChat", "dev.mruniverse.guardianchat.");
        storage = new FileStorage(this);
        /*
         * Abstracts
         */
        abstractCustomChatListener = new CustomChatListener();
        /*
         * Priorities
         */
        EventPriority customChatPriority = getEventPriority(storage.getControl(GuardianFiles.SETTINGS).getString("custom-chat.priority"));
        /*
         * Registers
         */
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvent(AsyncPlayerChatEvent.class,abstractCustomChatListener,customChatPriority,abstractCustomChatListener,this,true);

    }

    private EventPriority getEventPriority(String priorityLevel) {
        try {
            if (priorityLevel == null) return EventPriority.NORMAL;
            return EventPriority.valueOf(priorityLevel.toUpperCase());
        } catch (Throwable ignored) {
            return EventPriority.NORMAL;
        }
    }

    public FileStorage getStorage() { return storage; }

    public AbstractCustomChatListener getAbstractChatListener() {
        return abstractCustomChatListener;
    }

    public ExternalLogger getLogs() {
        if(logger == null) logger = GuardianLIB.initLogger(this,"GuardianChat","dev.mruniverse.guardianchat.");
        return logger;
    }
}
