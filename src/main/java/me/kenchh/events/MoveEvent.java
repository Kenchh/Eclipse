package me.kenchh.events;

import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MoveEvent implements Listener {

    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());
        dp.airticks = 0;
        dp.customairticks = 0;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        double deltaY = to.getY() - from.getY();

        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());
        if(dp.lastDeltaY != deltaY) {
            dp.lastDeltaY = deltaY;
        }
    }

}
