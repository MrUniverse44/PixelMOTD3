package dev.mruniverse.pixelmotd.bungeecord.utils;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.bungeecord.motd.CustomMotdListener;
import dev.mruniverse.pixelmotd.bungeecord.motd.MotdPlayers;
import dev.mruniverse.pixelmotd.bungeecord.storage.FileStorage;
import dev.mruniverse.pixelmotd.bungeecord.utils.command.MainCommand;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.MotdPlayersType;
import dev.mruniverse.pixelmotd.global.enums.MotdType;

import java.util.HashMap;

public class Loader {
    private final PixelMOTD plugin;
    private final HashMap<MotdType, MotdPlayers> online = new HashMap<>();
    private final HashMap<MotdType, MotdPlayers> max = new HashMap<>();
    private CustomMotdListener motdListener = null;

    public Loader(PixelMOTD plugin) {
        this.plugin = plugin;
    }

    public void load() {
        int max = plugin.getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers();
        plugin.setMax(max);

        plugin.setLogger(new GuardianLogger(plugin,"PixelMOTD", "dev.mruniverse.pixelmotd.bungeecord."));

        FileStorage currentStorage = new FileStorage(plugin);

        String lang = currentStorage.getControl(GuardianFiles.SETTINGS).getString("settings.language","en");

        currentStorage.setMessages(lang);

        plugin.setStorage(currentStorage);

        new Metrics(plugin, 8509);
        plugin.getLogs().info("Metrics has been loaded.");

        motdListener = new CustomMotdListener(plugin);

        plugin.getProxy().getPluginManager().registerListener(plugin,motdListener);
        plugin.getLogs().info("Motd listener has been registered.");

        loadPlayers();
    }

    public void loadPlayers() {
        online.clear();
        max.clear();
        for(MotdType type : MotdType.values()) {
            online.put(type,new MotdPlayers(plugin,type, MotdPlayersType.ONLINE));
            max.put(type,new MotdPlayers(plugin,type,MotdPlayersType.MAX));
            plugin.getLogs().info("Motd Players Online in MotdType: " + type.getName() + " has been updated!");
            plugin.getLogs().info("Motd Players Max in MotdType: " + type.getName() + " has been updated!");
        }
    }

    public void unloadPlayers() {
        online.clear();
        max.clear();
    }

    public HashMap<MotdType, MotdPlayers> getMax() { return max; }

    public HashMap<MotdType, MotdPlayers> getOnline() { return online; }

    public void loadCommand(String command) {
        plugin.getProxy().getPluginManager().registerCommand(plugin, new MainCommand(plugin, command));
    }

    public CustomMotdListener getMotdListener() {
        return motdListener;
    }

}
