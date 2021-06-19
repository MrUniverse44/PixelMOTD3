package dev.mruniverse.pixelmotd.spigot.motd;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    public static String ListToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        int line = 0;
        int maxLine = list.size();
        for (String lines : list) {
            line++;
            if(line != maxLine) {
                builder.append(lines).append("\n");
            } else {
                builder.append(lines);
            }
        }
        return builder.toString();
    }


}
