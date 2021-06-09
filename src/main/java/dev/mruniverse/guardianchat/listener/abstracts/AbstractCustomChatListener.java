package dev.mruniverse.guardianchat.listener.abstracts;

import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;

public abstract class AbstractCustomChatListener implements EventExecutor,Listener {

    public void sendChat(AsyncPlayerChatEvent event) {
        /*
         * working - send chat
         */
        event.setCancelled(true);

    }
}
