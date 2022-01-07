package dev.mruniverse.pixelmotd.spigot.listeners;

import dev.mruniverse.pixelmotd.global.Ping;
import dev.mruniverse.pixelmotd.global.Priority;
import dev.mruniverse.pixelmotd.global.enums.GuardianFiles;
import dev.mruniverse.pixelmotd.global.enums.Type;
import dev.mruniverse.pixelmotd.spigot.PixelMOTDBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener, Ping {

    private final PixelMOTDBuilder plugin;

    private final PingBuilder pingBuilder;

    private boolean isWhitelisted;

    public PingListener(PixelMOTDBuilder plugin) {
        this.plugin = plugin;
        this.pingBuilder = new PingBuilder(plugin);
        plugin.getStorage().getLogs().info("Custom Priority Don't Affect without ProtocolLIB");
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
        load();
    }

    private void load() {
        isWhitelisted = plugin.getStorage().getFiles().getControl(GuardianFiles.WHITELIST).getStatus("whitelist.global.Enabled");
        pingBuilder.update();
    }

    @Override
    public void update() {
        load();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMotd(ServerListPingEvent event) {

        if(isWhitelisted) {
            pingBuilder.execute(plugin.getStorage().getPriority().get(Type.WHITELISTED),event);
            return;
        }
        pingBuilder.execute(plugin.getStorage().getPriority().get(Type.DEFAULT),event);
    }

    @Override
    public void setWhitelist(boolean status) {
        isWhitelisted = status;
    }

    public static EventPriority get(Priority priority) {
        switch (priority) {
            case HIGHEST:
                return EventPriority.HIGHEST;
            case NORMAL:
                return EventPriority.NORMAL;
            default:
            case HIGH:
                return EventPriority.HIGH;
            case LOWEST:
                return EventPriority.LOWEST;
            case LOW:
                return EventPriority.LOW;
            case MONITOR:
                return EventPriority.MONITOR;
        }
    }
}
