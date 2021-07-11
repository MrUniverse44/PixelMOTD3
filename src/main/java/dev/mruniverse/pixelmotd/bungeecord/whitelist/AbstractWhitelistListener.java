package dev.mruniverse.pixelmotd.bungeecord.whitelist;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.spigot.motd.MotdUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;


public abstract class AbstractWhitelistListener implements Listener {
    private final Configuration whitelist;

    public AbstractWhitelistListener(PixelMOTD plugin) {
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

    public void checkPlayer(LoginEvent loginEvent) {
        if(loginEvent == null) return;
        if(loginEvent.getConnection() == null) return;
        final String user = loginEvent.getConnection().getName();
        final String uuid = loginEvent.getConnection().getUniqueId().toString();
        if(whitelist.getBoolean("whitelist.toggle")) {
            if(!whitelist.getStringList("whitelist.players-name").contains(user) || !whitelist.getStringList("whitelist.players-uuid").contains(uuid)) {
                String reason = MotdUtils.ListToString(whitelist.getStringList("whitelist.kick-message"));
                loginEvent.setCancelReason(new TextComponent(ChatColor.translateAlternateColorCodes('&',reason.replace("%whitelist_author%",getAuthor()).replace("%type%","Server"))));
            }
            return;
        }
        if(whitelist.getBoolean("blacklist.toggle")) {
            if(whitelist.getStringList("blacklist.players-name").contains(user) || whitelist.getStringList("blacklist.players-uuid").contains(uuid)) {
                String reason = MotdUtils.ListToString(whitelist.getStringList("blacklist.kick-message"));
                loginEvent.setCancelReason(new TextComponent(ChatColor.translateAlternateColorCodes('&',reason.replace("%blacklist_author%",getAuthor()).replace("%type%","Server"))));
            }
        }
    }
}

