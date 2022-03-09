package dev.mruniverse.pixelmotd.spigot.listeners.whitelist;

import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.Converter;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;

import dev.mruniverse.pixelmotd.spigot.PixelMOTD;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.EventExecutor;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public abstract class AbstractWhitelistListener implements Listener, EventExecutor {

    private final PixelMOTD plugin;

    private Control whitelist;

    private Control blacklist;

    public AbstractWhitelistListener(PixelMOTD builder) {
        plugin = builder;
        whitelist = builder.getStorage().getFiles().getControl(GuardianFiles.WHITELIST);
        blacklist = builder.getStorage().getFiles().getControl(GuardianFiles.BLACKLIST);
    }

    public void update() {
        whitelist = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST);
        blacklist = plugin.getStorage().getFiles().getControl(GuardianFiles.BLACKLIST);
    }

    public void checkPlayer(PlayerLoginEvent loginEvent) {
        if (loginEvent == null) return;

        Player player = loginEvent.getPlayer();

        InetSocketAddress socketAddress = player.getAddress();

        if (socketAddress != null) {
            InetAddress address = socketAddress.getAddress();

            plugin.getPing().getPlayerDatabase().add(address.getHostAddress(), player.getName());
        }

        final String user = player.getName();
        final String uuid = player.getUniqueId().toString();

        if (whitelist.getStatus("whitelist.global.Enabled")) {
            if (!whitelist.getStringList("players.global.by-name").contains(user) && !whitelist.getStringList("players.global.by-uuid").contains(uuid)) {
                String reason = Converter.ListToString(whitelist.getStringList("whitelist.global.kick-message"));
                loginEvent.disallow(
                        PlayerLoginEvent.Result.KICK_WHITELIST,
                        ChatColor.translateAlternateColorCodes('&',
                                reason.replace("%uuid%",user)
                                        .replace("%nick%",user)
                                        .replace("%author%",whitelist.getString("whitelist.global.author"))
                                        .replace("%reason%",whitelist.getString("whitelist.global.reason"))
                                        .replace("%type%","Server")
                        )

                );
                return;
            }
        }
        if (blacklist.getStatus("blacklist.global.Enabled")) {
            if (blacklist.getStringList("players.global.by-name").contains(user) || blacklist.getStringList("players.global.by-uuid").contains(uuid)) {
                String reason = Converter.ListToString(blacklist.getStringList("blacklist.global.kick-message"));
                loginEvent.disallow(
                        PlayerLoginEvent.Result.KICK_BANNED,
                        ChatColor.translateAlternateColorCodes('&',
                                reason.replace("%uuid%",user)
                                        .replace("%nick%",user)
                                        .replace("%author%",blacklist.getString("blacklist.global.author"))
                                        .replace("%reason%",blacklist.getString("blacklist.global.reason"))
                                        .replace("%type%","Server")
                        )
                );
            }
        }
    }

    public void checkPlayer(PlayerTeleportEvent event) {
        if (event == null) {
            return;
        }

        if (event.getTo() == null) {
            return;
        }

        final Player player = event.getPlayer();

        final String user = player.getName();
        final String uuid = player.getUniqueId().toString();

        World actualWorld = event.getPlayer().getWorld();

        World nextWorld = event.getTo().getWorld();

        if (nextWorld == null) {
            nextWorld = actualWorld;
        }

        final String target = nextWorld.getName();

        if (whitelist.getStatus("whitelist." + target + ".Enabled")) {
            if (!whitelist.getStringList("players." + target + ".by-name").contains(user) && !whitelist.getStringList("players." + target + ".by-uuid").contains(uuid)) {
                String reason = Converter.ListToString(whitelist.getStringList("whitelist." + target + ".kick-message"));
                player.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                reason.replace("%uuid%",user)
                                        .replace("%nick%",user)
                                        .replace("%author%",whitelist.getString("whitelist." + target + ".author"))
                                        .replace("%reason%",whitelist.getString("whitelist." + target + ".reason"))
                                        .replace("%type%","Server")
                        )
                );
                event.setCancelled(true);
                return;
            }
        }
        if (blacklist.getStatus("blacklist." + target + ".Enabled")) {
            if (blacklist.getStringList("players." + target + ".by-name").contains(user) || blacklist.getStringList("players." + target + ".by-uuid").contains(uuid)) {
                String reason = Converter.ListToString(blacklist.getStringList("blacklist." + target + ".kick-message"));
                player.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                reason.replace("%uuid%",user)
                                        .replace("%nick%",user)
                                        .replace("%author%",blacklist.getString("blacklist." + target + ".author"))
                                        .replace("%reason%",blacklist.getString("blacklist." + target + ".reason"))
                                        .replace("%type%","Server")
                        )
                );
                event.setCancelled(true);
            }
        }
    }
}
