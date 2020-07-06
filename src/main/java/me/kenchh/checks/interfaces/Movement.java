package me.kenchh.checks.interfaces;

import me.kenchh.data.DataProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public interface Movement extends Listener {

    void move(PlayerMoveEvent e, DataProfile dp, Player p, double deltaY, double deltaH, double deltadeltaY, double deltadeltaH);

}
