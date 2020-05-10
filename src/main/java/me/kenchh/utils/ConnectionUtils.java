package me.kenchh.utils;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ConnectionUtils {

    public static int getPing(Player p) {
        return ((CraftPlayer)p).getHandle().ping;
    }

}
