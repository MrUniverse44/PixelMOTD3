package dev.mruniverse.pixelmotd.bungeecord.utils;

import dev.mruniverse.pixelmotd.bungeecord.PixelMOTD;
import dev.mruniverse.pixelmotd.commons.Control;
import dev.mruniverse.pixelmotd.commons.enums.GuardianFiles;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/*
 * This code was created by Phoenix616
 * And modified a little bit by me
 * Please give credits to him, this option was added by this code.
 */
@SuppressWarnings("unused")
public class ServerStatusChecker {
    private final PixelMOTD plugin;

    private final List<ScheduledTask> pingTask = new ArrayList<>();

    private Map<String, Boolean> statusMap = new ConcurrentHashMap<>();

    private Control control;

    private int pingTimeout = 500;

    private String online;

    private String offline;

    public ServerStatusChecker(PixelMOTD plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        this.control = plugin.getStorage().getFiles().getControl(GuardianFiles.SETTINGS);
        online = control.getColoredString("settings.server-status.online","&a&lONLINE");
        offline = control.getColoredString("settings.server-status.offline","&c&lOFFLINE");
    }

    public void update() {
        load();
    }

    public void start() {
        stop();
        int pingOnline = control.getInt("settings.server-status.intervals.online", 10);
        int pingOffline = control.getInt("settings.server-status.intervals.offline", 10);
        pingTimeout = control.getInt("settings.server-status.intervals.timeout", 500);
        if (pingOnline == pingOffline) {
            if (pingOnline == 0)
                return;
            pingTask.add(plugin.getProxy().getScheduler().schedule(plugin, () -> {
                if (control.getStatus("settings.server-status.toggle")) {
                    refreshStatusMap(plugin.getProxy().getServers().values());
                } else {
                    stop();
                }
            }, 10, pingOnline, TimeUnit.SECONDS));
        } else {
            refreshStatusMap(plugin.getProxy().getServers().values());
            if (pingOnline != 0) {
                pingTask.add(plugin.getProxy().getScheduler().schedule(plugin, () -> {
                    if (control.getStatus("settings.server-status.toggle")) {
                        refreshStatusMap(getOnlineServers());
                    } else {
                        stop();
                    }
                }, 10, pingOnline, TimeUnit.SECONDS));
            }
            if (pingOffline != 0) {
                pingTask.add(plugin.getProxy().getScheduler().schedule(plugin, () -> {
                    if (control.getStatus("settings.server-status.toggle")) {
                        refreshStatusMap(getOfflineServers());
                    } else {
                        stop();
                    }
                }, 10, pingOffline, TimeUnit.SECONDS));
            }
        }
    }

    public Collection<ServerInfo> getOnlineServers() {
        List<ServerInfo> onlineServers = new ArrayList<>();
        for(Map.Entry<String, Boolean> entry : getStatusMap().entrySet()) {
            if (entry.getValue() != null && entry.getValue()) {
                ServerInfo server = plugin.getProxy().getServerInfo(entry.getKey());
                if (server != null) {
                    onlineServers.add(server);
                }
            }
        }
        return onlineServers;
    }

    public Collection<ServerInfo> getOfflineServers() {
        List<ServerInfo> offlineServers = new ArrayList<>();
        for(Map.Entry<String, Boolean> entry : getStatusMap().entrySet()) {
            if (entry.getValue() != null && !entry.getValue()) {
                ServerInfo server = plugin.getProxy().getServerInfo(entry.getKey());
                if (server != null) {
                    offlineServers.add(server);
                }
            }
        }
        return offlineServers;
    }

    @SuppressWarnings("deprecation")
    public void refreshStatusMap(Collection<ServerInfo> servers) {
        for(final ServerInfo server : servers) {
            if (server.getPlayers().size() == 0) {
                plugin.getProxy().getScheduler().runAsync(plugin, () -> setStatus(server, isReachable(server.getAddress())));
            } else {
                setStatus(server, true);
            }
        }
    }

    private boolean isReachable(InetSocketAddress address) {
        Socket socket = new Socket();
        try {
            socket.connect(address, pingTimeout);
            socket.close();
            return true;
        } catch(IOException ignored) {
            return false;
        }
    }

    private void setStatus(ServerInfo server, boolean online) {
        statusMap.put(server.getName(), online);
    }

    public Boolean getStatus(ServerInfo server) {
        return statusMap.get(server.getName());
    }

    public void stop() {
        statusMap = new ConcurrentHashMap<>();
        Iterator<ScheduledTask> taskIt = pingTask.iterator();
        while(taskIt.hasNext()) {
            taskIt.next().cancel();
            taskIt.remove();
        }
    }

    public Map<String, Boolean> getStatusMap() {
        return statusMap;
    }

    public String getServerStatus(String server) {
        if (statusMap.containsKey(server)) {
            boolean status = statusMap.get(server);
            if (status) {
                return online;
            }
            return offline;
        }
        return offline;
    }
}
