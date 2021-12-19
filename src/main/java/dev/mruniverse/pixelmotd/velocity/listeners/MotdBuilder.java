package dev.mruniverse.pixelmotd.velocity.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;

public class MotdBuilder {
    @Subscribe(order = PostOrder.EARLY)
    public void onMotdPing(ProxyPingEvent event) {
        ServerPing.Builder ping = event.getPing().asBuilder();

        /*
         * HERE THE MOTD CODING
         */

        event.setPing(ping.build());
    }
}
