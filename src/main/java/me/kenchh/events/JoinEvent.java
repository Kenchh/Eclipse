package me.kenchh.events;

import me.kenchh.data.DataProfileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        DataProfileManager.addDataProfile(e.getPlayer());
    }

}
