package dev.mruniverse.pixelmotd.spigot.whitelist;

import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import dev.mruniverse.pixelmotd.spigot.motd.MotdUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.EventExecutor;

public abstract class AbstractWhitelistListener implements Listener, EventExecutor {
    private FileConfiguration whitelist;

    public AbstractWhitelistListener(PixelMOTD plugin) {
        whitelist = plugin.getStorage().getControl(GuardianFiles.WHITELIST);
    }

    public void update(PixelMOTD plugin) {
        whitelist = plugin.getStorage().getControl(GuardianFiles.WHITELIST);
    }

    public String getAuthor() {
        String author = whitelist.getString("whitelist.author");
        if(author == null) author = "CONSOLE";
        if(!author.equalsIgnoreCase("CONSOLE")) {
            return whitelist.getString("whitelist.author");
        } else {
            if(whitelist.getBoolean("whitelist.customConsoleName.toggle")) {
                return whitelist.getString("whitelist.customConsoleName.name");
            }
            return "Console";
        }
    }


    public void checkPlayer(PlayerLoginEvent loginEvent) {
        final Player player = loginEvent.getPlayer();
        if(whitelist.getBoolean("whitelist.toggle")) {
            if(!whitelist.getStringList("whitelist.players-name").contains(player.getName()) || !whitelist.getStringList("whitelist.players-uuid").contains(player.getUniqueId().toString())) {
                String reason = MotdUtils.ListToString(whitelist.getStringList("whitelist.kick-message"));
                loginEvent.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, ChatColor.translateAlternateColorCodes('&',reason.replace("%whitelist_author%",getAuthor()).replace("%type%","Server")));
            }
            return;
        }
        if(whitelist.getBoolean("blacklist.toggle")) {
            if(whitelist.getStringList("blacklist.players-name").contains(player.getName()) || whitelist.getStringList("blacklist.players-uuid").contains(player.getUniqueId().toString())) {
                String reason = MotdUtils.ListToString(whitelist.getStringList("blacklist.kick-message"));
                loginEvent.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, ChatColor.translateAlternateColorCodes('&',reason.replace("%blacklist_author%",getAuthor()).replace("%type%","Server")));
            }
        }
    }
}
