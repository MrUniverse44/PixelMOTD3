package dev.mruniverse.pixelmotd.spigot.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import dev.mruniverse.pixelmotd.spigot.PixelMOTD;

public class CustomMotdListener extends PacketAdapter {
    private final PixelMOTD plugin;

    public CustomMotdListener(PixelMOTD plugin, ListenerPriority priority) {
        super(plugin,priority, PacketType.Status.Server.SERVER_INFO);
        this.plugin = plugin;
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(final PacketEvent event){
        if(event.isCancelled()) return;
        if (event.getPacketType() != PacketType.Status.Server.SERVER_INFO) return;

        /*
         * MOTD SYSTEM
         */

    }
}
