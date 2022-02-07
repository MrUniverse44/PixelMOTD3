package dev.mruniverse.pixelmotd.bungeecord.listeners.whitelist;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTDBuilder;
import dev.mruniverse.pixelmotd.global.Control;
import dev.mruniverse.pixelmotd.global.Converter;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;

public abstract class AbstractWhitelistListener implements Listener {

    private final String security = ChatColor.translateAlternateColorCodes('&',"&cWarning PixelMOTD detected a Null IP from player: &6");

    private final TextComponent suggest = new TextComponent(ChatColor.translateAlternateColorCodes('&',"&cIf you want a Block-Null-IPs features suggest it to me and i will add this feature."));

    private Control whitelist;

    private Control blacklist;

    public AbstractWhitelistListener(PixelMOTDBuilder builder) {
        whitelist = builder.getStorage().getFiles().getControl(GuardianFiles.WHITELIST);
        blacklist = builder.getStorage().getFiles().getControl(GuardianFiles.BLACKLIST);
    }

    public void update(PixelMOTDBuilder plugin) {
        whitelist = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST);
        blacklist = plugin.getStorage().getFiles().getControl(GuardianFiles.BLACKLIST);
    }

    public void checkPlayer(LoginEvent event) {
        if (event == null) {
            return;
        }

        final PendingConnection connection = event.getConnection();

        if (connection == null) {
            return;
        }

        if (connection.getUniqueId() == null) {
            sendMessage(new TextComponent(security + connection.getName()));
            sendMessage(suggest);
            return;
        }

        final String user = connection.getName();
        final String uuid = connection.getUniqueId().toString();

        if (whitelist.getStatus("whitelist.global.Enabled")) {
            if (!whitelist.getStringList("players.global.by-name").contains(user) && !whitelist.getStringList("players.global.by-uuid").contains(uuid)) {
                String reason = Converter.ListToString(whitelist.getStringList("whitelist.global.kick-message"));
                event.setCancelReason(
                        new TextComponent(
                                ChatColor.translateAlternateColorCodes('&',
                                        reason.replace("%uuid%",user)
                                                .replace("%nick%",user)
                                                .replace("%author%",whitelist.getString("whitelist.global.author"))
                                                .replace("%reason%",whitelist.getString("whitelist.global.reason"))
                                                .replace("%type%","Server")
                                )
                        )
                );
                event.setCancelled(true);
                return;
            }
        }
        if (blacklist.getStatus("blacklist.global.Enabled")) {
            if (blacklist.getStringList("players.global.by-name").contains(user) || blacklist.getStringList("players.global.by-uuid").contains(uuid)) {
                String reason = Converter.ListToString(blacklist.getStringList("blacklist.global.kick-message"));
                event.setCancelReason(
                        new TextComponent(
                                ChatColor.translateAlternateColorCodes('&',
                                        reason.replace("%uuid%",user)
                                                .replace("%nick%",user)
                                                .replace("%author%",blacklist.getString("blacklist.global.author"))
                                                .replace("%reason%",blacklist.getString("blacklist.global.reason"))
                                                .replace("%type%","Server")
                                )
                        )
                );
                event.setCancelled(true);
            }
        }
    }

    public void checkPlayer(ServerConnectEvent event) {
        if (event == null) {
            return;
        }
        if (event.getPlayer() == null) {
            return;
        }

        final ProxiedPlayer player = event.getPlayer();

        if (player.getUniqueId() == null) {
            sendMessage(new TextComponent(security + player.getName()));
            sendMessage(suggest);
            return;
        }

        final String user = player.getName();
        final String uuid = player.getUniqueId().toString();
        final String server = event.getTarget().getName();

        if (whitelist.getStatus("whitelist." + server + ".Enabled")) {
            if (!whitelist.getStringList("players." + server + ".by-name").contains(user) && !whitelist.getStringList("players." + server + ".by-uuid").contains(uuid)) {
                String reason = Converter.ListToString(whitelist.getStringList("whitelist." + server + ".kick-message"));
                player.sendMessage(
                        new TextComponent(
                                ChatColor.translateAlternateColorCodes('&',
                                        reason.replace("%uuid%",user)
                                                .replace("%nick%",user)
                                                .replace("%author%",whitelist.getString("whitelist." + server + ".author"))
                                                .replace("%reason%",whitelist.getString("whitelist." + server + ".reason"))
                                                .replace("%type%","Server")
                                )
                        )
                );
                event.setCancelled(true);
                return;
            }
        }
        if (blacklist.getStatus("blacklist." + server + ".Enabled")) {
            if (blacklist.getStringList("players." + server + ".by-name").contains(user) || blacklist.getStringList("players." + server + ".by-uuid").contains(uuid)) {
                String reason = Converter.ListToString(blacklist.getStringList("blacklist." + server + ".kick-message"));
                player.sendMessage(
                        new TextComponent(
                                ChatColor.translateAlternateColorCodes('&',
                                        reason.replace("%uuid%",user)
                                                .replace("%nick%",user)
                                                .replace("%author%",blacklist.getString("blacklist." + server + ".author"))
                                                .replace("%reason%",blacklist.getString("blacklist." + server + ".reason"))
                                                .replace("%type%","Server")
                                )
                        )
                );
                event.setCancelled(true);
            }
        }
    }

    private void sendMessage(TextComponent component) {
        ProxyServer.getInstance().getConsole().sendMessage(component);
    }

}
