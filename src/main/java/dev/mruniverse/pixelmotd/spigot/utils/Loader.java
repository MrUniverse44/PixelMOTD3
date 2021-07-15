package dev.mruniverse.pixelmotd.spigot.utils;

import com.comphenix.protocol.events.ListenerPriority;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.MotdPlayersType;
import dev.mruniverse.pixelmotd.global.enums.MotdType;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.motd.CustomMotdListener;
import dev.mruniverse.pixelmotd.spigot.motd.MotdPlayers;
import dev.mruniverse.pixelmotd.spigot.storage.FileStorage;
import dev.mruniverse.pixelmotd.spigot.utils.command.MainCommand;
import org.bukkit.command.PluginCommand;

import java.util.HashMap;

public class Loader {
    private final PixelMOTD plugin;
    private CustomMotdListener motdListener = null;
    private final HashMap<MotdType, MotdPlayers> online = new HashMap<>();
    private final HashMap<MotdType, MotdPlayers> max = new HashMap<>();
    public Loader(PixelMOTD plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.setLogger(new GuardianLogger("PixelMOTD", "dev.mruniverse.pixelmotd.spigot."));

        FileStorage currentStorage = new FileStorage(plugin);

        String lang = currentStorage.getControl(GuardianFiles.SETTINGS).getString("settings.language","en");

        currentStorage.setMessages(lang);

        plugin.setStorage(currentStorage);

        new Metrics(plugin, 8509);
        plugin.getLogs().info("Metrics has been loaded!");

        loadPlayers();

        motdListener = new CustomMotdListener(plugin,getEventPriority(currentStorage.getControl(GuardianFiles.SETTINGS).getString("settings.event-priority")));
        plugin.getLogs().info("Motd Listener has been registered!");
    }

    private void loadPlayers() {
        online.clear();
        max.clear();
        for(MotdType type : MotdType.values()) {
            online.put(type,new MotdPlayers(plugin,type,MotdPlayersType.ONLINE));
            max.put(type,new MotdPlayers(plugin,type,MotdPlayersType.MAX));
            plugin.getLogs().info("Motd Players Online in MotdType: " + type.getName() + " has been updated!");
            plugin.getLogs().info("Motd Players Max in MotdType: " + type.getName() + " has been updated!");
        }
    }

    public HashMap<MotdType, MotdPlayers> getMax() { return max; }

    public HashMap<MotdType, MotdPlayers> getOnline() { return online; }

    public void loadCommand(String command) {
        PluginCommand cmd = plugin.getCommand(command);
        if(cmd == null) return;
        cmd.setExecutor(new MainCommand(plugin,command));
    }

    public CustomMotdListener getMotdListener() {
        return motdListener;
    }

    private ListenerPriority getEventPriority(String priorityLevel) {
        try {
            if (priorityLevel == null) return ListenerPriority.HIGH;
            return ListenerPriority.valueOf(priorityLevel.toUpperCase());
        } catch (Throwable ignored) {
            return ListenerPriority.HIGH;
        }
    }
}
