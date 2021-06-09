package dev.mruniverse.guardianchat.listener;

import dev.mruniverse.guardianchat.listener.abstracts.AbstractCustomChatListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class CustomChatListener extends AbstractCustomChatListener implements EventExecutor,Listener {

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        try {
            if(event instanceof AsyncPlayerChatEvent) {
                sendChat((AsyncPlayerChatEvent)event);
            }
        }catch (Throwable ignored) {}
    }
}
