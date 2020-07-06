package me.kenchh.packet;

import org.bukkit.entity.Player;

public interface PacketListener {

    void readPacket(Player p, Object packet);

}
