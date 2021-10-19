package dev.mruniverse.pixelmotd.spigot.utils;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.entity.Player;

public class ProtocolLIB implements ExternalLib {
    @Override
    public int getProtocol(Player player) {
        return ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
    }
}
