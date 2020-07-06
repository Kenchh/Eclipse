package me.kenchh.events;

import me.kenchh.checks.Check;
import me.kenchh.checks.CheckManager;
import me.kenchh.checks.interfaces.Movement;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MoveEvent implements Listener {

    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());
        /* TODO TELEPORT TICKS */
        dp.airticks = 0;
        dp.lastLocationMillis = System.currentTimeMillis();
        dp.lastLocation = e.getTo();
        dp.lastLocationOnGround = e.getPlayer().getLocation();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();

        double deltaY = MathUtils.round(to.getY() - from.getY());
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();

        double deltaH = MathUtils.round(Math.sqrt(((deltaX*deltaX) + (deltaZ*deltaZ))));

        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());

        dp.updateAirTicks();
        dp.updateVanillaOnGroundTicks();

        double deltadeltaY = MathUtils.round(deltaY - dp.lastDeltaY);
        double deltadeltaH = MathUtils.round(deltaH - dp.lastDeltaH);

        for(Check c : CheckManager.checks) {
            if(c instanceof Movement) {
                if(dp.currentCheckMode == null || dp.currentCheckMode.isIgnored(c)) {
                    ((Movement) c).move(e, dp, e.getPlayer(), deltaY, deltaH, deltadeltaY, deltadeltaH);
                } else {
                    ((Movement) dp.currentCheckMode.getCustomCheck(c)).move(e, dp, e.getPlayer(), deltaY, deltaH, deltadeltaY, deltadeltaH);
                }
            }
        }

        /** ------ All last variables after this line ------ */

        dp.lastVanillaOnGround = e.getPlayer().isOnGround();

        dp.lastLocationMillis = System.currentTimeMillis();
        dp.lastLocation = from;

        dp.lastDeltaY = MathUtils.round(deltaY);
        if(dp.speedticks <= 0) {
            dp.lastDeltaH = MathUtils.round(deltaH);
        } else {
            dp.speedticks--;
        }

        dp.lastDeltaDeltaY = deltadeltaY;
        dp.lastDeltaDeltaH = deltadeltaH;

    }

}
