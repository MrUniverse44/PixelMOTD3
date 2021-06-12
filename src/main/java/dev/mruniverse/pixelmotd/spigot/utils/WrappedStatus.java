package dev.mruniverse.pixelmotd.spigot.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedServerPing;

public class WrappedStatus extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Status.Server.SERVER_INFO;

    public WrappedStatus(PacketContainer packet) {
        super(packet, TYPE);
    }

    public WrappedServerPing getJsonResponse() {
        return this.handle.getServerPings().read(0);
    }
}
