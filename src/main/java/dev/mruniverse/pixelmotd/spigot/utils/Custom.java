package dev.mruniverse.pixelmotd.spigot.utils;

import org.bukkit.entity.Player;

public class Custom implements ExternalLib {

    @Override
    public int getProtocol(Player player) {
        return -1;
    }
}
