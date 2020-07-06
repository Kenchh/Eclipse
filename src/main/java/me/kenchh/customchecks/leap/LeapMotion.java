package me.kenchh.customchecks.leap;

import me.kenchh.checks.CheckManager;
import me.kenchh.checks.cheatchecks.movement.Motion;
import me.kenchh.checks.fails.FailType;
import me.kenchh.data.DataProfile;
import me.kenchh.main.Eclipse;
import me.kenchh.utils.ConnectionUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class LeapMotion extends Motion {

    @Override
    public void move(PlayerMoveEvent e, DataProfile dp, Player p, double deltaY, double deltaH, double deltadeltaY, double deltadeltaH) {
        if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if(p.getAllowFlight()) {
            return;
        }

        if(p.isInsideVehicle()) {
            return;
        }

        /** B: Checks if this wrong deltaY occurs at a wrong time when the player is in the air **/
        int B_maxairticks = 3;
        if (ConnectionUtils.getPing(p) >= 60) {
            if(ConnectionUtils.getPing(p) < 250) {
                B_maxairticks = B_maxairticks * ConnectionUtils.getPing(p) / 60;
            } else {
                B_maxairticks = B_maxairticks * 250 / 60;
            }
        }

        if (deltaY > 0.75D && dp.airticks > B_maxairticks) {
            fail(p, FailType.B, "dY: " + deltaY + " " + "aT: " + dp.airticks);
        }

        if(CheckManager.getParentCheck(this).checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "dY: " + deltaY);
            p.sendMessage(Eclipse.prefix + "dH: " + deltaH);
            p.sendMessage(Eclipse.prefix + "aT: " + dp.airticks);
            p.sendMessage(Eclipse.prefix + "ddY: " + deltadeltaY);
            p.sendMessage(Eclipse.prefix + "ddH: " + deltadeltaH);
        }
    }
}
