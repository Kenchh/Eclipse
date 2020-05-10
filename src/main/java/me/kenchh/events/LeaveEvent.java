package me.kenchh.events;

import me.kenchh.data.DataProfileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        DataProfileManager.removeDataProfile(e.getPlayer());
    }

}
