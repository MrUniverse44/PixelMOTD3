package dev.mruniverse.pixelmotd.commons.players;

import java.util.HashMap;
import java.util.Map;

public class PlayersDatabase {

    private final Map<String, String> playersMap = new HashMap<>();

    public boolean exists(String key) {
        return playersMap.containsKey(key);
    }

    public String getPlayer(String key) {
        return playersMap.computeIfAbsent(key, V -> "unknown#1");
    }

    public void add(String key, String value) {
        playersMap.put(key, value);
    }

    public void fromSocket(String socket, String user) {
        socket = socket.replace("/","");

        String[] key = socket.split(":");

        add(key[0], user);
    }

    public void clear() {
        playersMap.clear();
    }

}
