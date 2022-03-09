package dev.mruniverse.pixelmotd.velocity.listeners.whitelist.type;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import dev.mruniverse.pixelmotd.velocity.PixelMOTD;
import dev.mruniverse.pixelmotd.velocity.listeners.whitelist.AbstractWhitelistListener;

public class PlayerConnectEarly extends AbstractWhitelistListener {

    public PlayerConnectEarly(PixelMOTD plugin) {
        super(plugin);
    }

    @Subscribe(order = PostOrder.EARLY)
    public void onLogin(LoginEvent event) {
        execute(event);
    }

}
