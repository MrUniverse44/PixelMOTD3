package dev.mruniverse.pixelmotd.spigot;

import dev.mruniverse.pixelmotd.global.FileStorageBuilder;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.shared.SpigotInput;
import dev.mruniverse.pixelmotd.spigot.listeners.PingListener;
import dev.mruniverse.pixelmotd.spigot.listeners.packets.PacketListener;
import dev.mruniverse.pixelmotd.spigot.storage.Storage;
import dev.mruniverse.pixelmotd.spigot.utils.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class PixelMOTDBuilder extends JavaPlugin {

    private Storage storage;

    private ExternalLib externalLib;

    private Ping ping;

    @Override
    public void onEnable() {
        storage = new Storage(this);
        storage.setInputManager(new SpigotInput(this));
        storage.setLogs(new GuardianLogger("PixelMOTD","PixelMOTD", "dev.mruniverse.pixelmotd."));
        storage.setStorage(new FileStorageBuilder(storage.getLogs(),getDataFolder(),storage.getInputManager()));
        storage.loadCommand("pmotd");
        storage.loadCommand("pixelmotd");
        boolean hasVia = getServer().getPluginManager().isPluginEnabled("ViaVersion");
        boolean hasProtocol = getServer().getPluginManager().isPluginEnabled("ProtocolLIB");
        if(hasVia) {
            storage.getLogs().info("ProtocolAPI will use ViaVersionAPI to get the protocol version of the player.");
            externalLib = new ViaVersion();

        }
        String priority = storage.getFiles().getControl(GuardianFiles.SETTINGS).getString("settings.extras-event-priority","HIGH");
        if(hasProtocol && !hasVia) {
            externalLib = new ProtocolLIB();
            storage.getLogs().info("ProtocolAPI will use ProtocolLIB to get the protocol version of the player.");
            ping = new PacketListener(this,Priority.getFromText(priority));
            return;
        }
        ping = new PingListener(this,Priority.getFromText(priority));
        storage.getLogs().info("ProtocolAPI don't find ViaVersion or ProtocolLIB in the server.");
        storage.getLogs().info("--------------------------------------------------------------");
        storage.getLogs().info("The outdatedClient and outdatedServer motd will not work.");
        storage.getLogs().info("The plugin only will be using 'motds' and 'whitelist' path.");
        storage.getLogs().info("Without ProtocolLIB the plugin will not load hover");
        storage.getLogs().info("and the ServerPing-Protocol option will not work.");

    }

    public Storage getStorage() {
        return storage;
    }

    public Ping getPing() {
        return ping;
    }

    public int getProtocolVersion(final Player player) {
        return externalLib.getProtocol(player);
    }


}
