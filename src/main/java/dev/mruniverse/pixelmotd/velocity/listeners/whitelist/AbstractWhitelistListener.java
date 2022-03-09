package dev.mruniverse.pixelmotd.velocity.listeners.whitelist;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.Converter;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.velocity.PixelMOTD;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public abstract class AbstractWhitelistListener {

    private final PixelMOTD plugin;

    private Control whitelist;

    private Control blacklist;

    public AbstractWhitelistListener(PixelMOTD plugin) {
        this.plugin = plugin;
        load();
    }

    public void update() {
        load();
    }

    private void load() {
        whitelist = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST);
        blacklist = plugin.getStorage().getFiles().getControl(GuardianFiles.BLACKLIST);
    }

    public void execute(LoginEvent event) {
        if (event == null) {
            return;
        }

        final Player player = event.getPlayer();

        InetSocketAddress socketAddress = player.getRemoteAddress();

        if (socketAddress != null) {
            InetAddress address = socketAddress.getAddress();

            plugin.getPing().getPlayerDatabase().add(address.getHostAddress(), player.getUsername());
        }

        final String user = player.getUsername();
        final String uuid = player.getUniqueId().toString();

        if (whitelist.getStatus("whitelist.global.Enabled")) {
            if (!whitelist.getStringList("players.global.by-name").contains(user) && !whitelist.getStringList("players.global.by-uuid").contains(uuid)) {
                String reason = Converter.ListToString(whitelist.getStringList("whitelist.global.kick-message"));
                reason = reason.replace("%uuid%",user)
                        .replace("%nick%",user)
                        .replace("%author%",whitelist.getString("whitelist.global.author"))
                        .replace("%reason%",whitelist.getString("whitelist.global.reason"))
                        .replace("%type%","Server");
                ResultedEvent.ComponentResult result = ResultedEvent.ComponentResult.denied(
                        LegacyComponentSerializer.builder().character('&').build().deserialize(reason)
                );
                event.setResult(result);
                return;
            }
        }
        if (blacklist.getStatus("blacklist.global.Enabled")) {
            if (blacklist.getStringList("players.global.by-name").contains(user) || blacklist.getStringList("players.global.by-uuid").contains(uuid)) {
                String reason = Converter.ListToString(blacklist.getStringList("blacklist.global.kick-message"));
                reason = reason.replace("%uuid%",user)
                        .replace("%nick%",user)
                        .replace("%author%",blacklist.getString("blacklist.global.author"))
                        .replace("%reason%",blacklist.getString("blacklist.global.reason"))
                        .replace("%type%","Server");
                ResultedEvent.ComponentResult result = ResultedEvent.ComponentResult.denied(
                        LegacyComponentSerializer.builder().character('&').build().deserialize(reason)
                );
                event.setResult(result);
            }
        }
    }
}
