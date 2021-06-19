package dev.mruniverse.pixelmotd.spigot.listener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class MotdUtils {

    public static String getWorlds(@Nullable String message) {
        if(message==null) message = "PixelCore v9.0.0";
        message = message.replace("%online%",Bukkit.getOnlinePlayers().size() + "")
        .replace("%max%",Bukkit.getMaxPlayers() + "");
        if(message.contains("%online_")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                message = message.replace("%online_" + world.getName() + "%", world.getPlayers().size() + "");
            }
        }
        return message;
    }


}
