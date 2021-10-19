package dev.mruniverse.pixelmotd.spigot.utils;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import org.bukkit.entity.Player;

public class ViaVersion implements ExternalLib {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public int getProtocol(Player player) {
        ViaAPI api = Via.getAPI();
        return api.getPlayerVersion(player);
    }
}
