package dev.mruniverse.pixelmotd.velocity.listeners.whitelist.type;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import dev.mruniverse.pixelmotd.velocity.PixelMOTD;
import dev.mruniverse.pixelmotd.velocity.listeners.whitelist.AbstractWhitelistListener;

public class PlayerConnectNormal extends AbstractWhitelistListener {

    public PlayerConnectNormal(PixelMOTD plugin) {
        super(plugin);
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onLogin(LoginEvent event) {
        execute(event);
    }

}
