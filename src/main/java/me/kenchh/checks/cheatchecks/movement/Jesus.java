package me.kenchh.checks.cheatchecks.movement;

import me.kenchh.checks.CheatCategory;
import me.kenchh.checks.Check;
import me.kenchh.checks.fails.FailType;
import me.kenchh.checks.interfaces.Movement;
import me.kenchh.data.DataProfile;
import me.kenchh.utils.LocationUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class Jesus extends Check implements Movement {
    public Jesus() {
        super("Jesus", CheatCategory.MOVEMENT);
    }

    @Override
    public void move(PlayerMoveEvent e, DataProfile dp, Player p, double deltaY, double deltaH, double deltadeltaY, double deltadeltaH) {

    }
}
