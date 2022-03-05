package dev.mruniverse.pixelmotd.spigot.utils;

import dev.mruniverse.pixelmotd.commons.GLogger;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PlaceholderParser {
    public static String parse(GLogger logs,String message) {
        try {
            return PlaceholderAPI.setPlaceholders(null, message);
        }catch (Exception ignored) {
            logs.error("You tried to load an placeholder and this placeholder can't load in the motd");
            logs.error("Maybe this placeholder need to be loaded by a player and can't be loaded in the motd");
            logs.error("This is the motd-line: &f'" + message + "&f'.");
            return message;
        }
    }

    public static String parse(GLogger logs,String user, String message) {
        try {
            OfflinePlayer player = Bukkit.getPlayer(user);
            return PlaceholderAPI.setPlaceholders(player, message);
        } catch (Exception ignored) {
            logs.error("You tried to load an placeholder and this placeholder can't load in the motd");
            logs.error("Maybe this placeholder need to be loaded by a player and can't be loaded in the motd");
            logs.error("This is the motd-line: &f'" + message + "&f'.");
            return message;
        }
    }
}
