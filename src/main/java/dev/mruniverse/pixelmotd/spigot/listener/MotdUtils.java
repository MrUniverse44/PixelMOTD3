package dev.mruniverse.pixelmotd.spigot.listener;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class MotdUtils {

    public static String getWorlds(String message) {
        if(message.contains("%online_")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                message = message.replace("%online_" + world.getName() + "%", world.getPlayers().size() + "");
            }
        }
        return message;
    }


}
