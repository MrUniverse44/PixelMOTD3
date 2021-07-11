package dev.mruniverse.pixelmotd.bungeecord.motd;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import net.md_5.bungee.api.config.ServerInfo;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MotdUtils {

    public static String getServers(PixelMOTD plugin, @Nullable String message) {
        if(message==null) message = "PixelCore v9.0.0";
        message = message.replace("%online%",plugin.getProxy().getOnlineCount() + "")
                .replace("%max%",plugin.getMax() + "");
        if(message.contains("%online_")) {
            for (ServerInfo server : plugin.getProxy().getServers().values()) {
                message = message.replace("%online_" + server.getName() + "%", server.getPlayers().size() + "");
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