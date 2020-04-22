package me.freakzboy.events;

import me.freakzboy.data.DataProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        DataProfileManager.addDataProfile(e.getPlayer());
    }

}
