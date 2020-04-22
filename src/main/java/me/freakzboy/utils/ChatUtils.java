package me.freakzboy.utils;

import org.bukkit.entity.Player;

public class ChatUtils {

    public static void clearClientChat(Player player) {
        for(int i=1; i<=255; i++) {
            player.sendMessage(" ");
        }
    }

}
