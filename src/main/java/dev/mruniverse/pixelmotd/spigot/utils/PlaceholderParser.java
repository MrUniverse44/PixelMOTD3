package dev.mruniverse.pixelmotd.spigot.utils;

import dev.mruniverse.pixelmotd.global.GLogger;
import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderParser {
    public static String parse(GLogger logs,String message) {
        try {
            return PlaceholderAPI.setPlaceholders(null, message);
        }catch (Throwable ignored) {
            logs.error("You tried to load an placeholder and this placeholder can't load in the motd");
            logs.error("Maybe this placeholder need to be loaded by a player and can't be loaded in the motd");
            logs.error("This is the motd-line: &f'" + message + "&f'.");
            return message;
        }
    }
}
