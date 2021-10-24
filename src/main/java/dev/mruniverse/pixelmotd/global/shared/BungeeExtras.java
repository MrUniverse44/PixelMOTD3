package dev.mruniverse.pixelmotd.global.shared;

import dev.mruniverse.pixelmotd.global.Extras;
import dev.mruniverse.pixelmotd.bungeecord.PixelMOTDBuilder;

public class BungeeExtras implements Extras {

    private final PixelMOTDBuilder plugin;

    private final int max;

    public BungeeExtras(PixelMOTDBuilder plugin) {
        this.plugin = plugin;
        this.max = plugin.getProxy().getConfigurationAdapter().getListeners().iterator().next().getMaxPlayers();
    }


    @Override
    public String getVariables(String message,int customOnline,int customMax) {
        return message.replace("%online%","" + plugin.getProxy().getOnlineCount())
                .replace("%max%","" + max)
                .replace("%fake_online%","" + customOnline)
                .replace("%fake_max%","" + customMax);
    }

    @Override
    public String getEvents(String message) {
        return message;
    }

    @Override
    public String getCentered(String message) {
        return message;
    }
}
