package me.kenchh.events;

import me.kenchh.checks.Check;
import me.kenchh.checks.CheckManager;
import me.kenchh.checks.interfaces.Movement;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MoveEvent implements Listener {

    /** Resets the last values to the teleported locations.
     *  If this is not done, then it the difference will be too huge and cause false flags.
     */
    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());

        dp.airticks = 0;
        dp.lastLocationMillis = System.currentTimeMillis();
        dp.lastLocation = e.getTo();
        dp.lastLocationOnGround = e.getPlayer().getLocation();
    }

    /**
     * EXPLANATION
     *
     * Delta stands for difference.
     *
     * Meaning, that
     * deltaY -> is the difference between the last player Y Coordinate and the current one.
     *
     * deltaH -> is the difference between the last player X AND Z Coordinate and the current one. (Which is basically the horizontal distance)
     *
     * deltaDeltaY/H -> is the difference between last deltaY/H and current deltaY/H. (Difference in velocity)
     *          (In science, you would call this acceleration - In math, derivative of the velocity)
     */

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
        dp.updateCustomAirTicks();
        dp.updateVanillaOnGroundTicks();

        double deltadeltaY = MathUtils.round(deltaY - dp.lastDeltaY);
        double deltadeltaH = MathUtils.round(deltaH - dp.lastDeltaH);

        for(Check c : CheckManager.checks) {

            if(dp.ignoredChecks.containsKey(c)) {
                continue;
            }

            if(c instanceof Movement) {
                if(dp.currentCheckMode == null || dp.currentCheckMode.isDefault(c)) {
                    ((Movement) c).move(e, dp, e.getPlayer(), deltaY, deltaH, deltadeltaY, deltadeltaH);
                } else {
                    ((Movement) dp.currentCheckMode.getCustomCheck(c)).move(e, dp, e.getPlayer(), deltaY, deltaH, deltadeltaY, deltadeltaH);
                }
            }
        }

        /**
          ------ All "last" variables after this line ------
                Setting the current values as the last ones, to test in the next upcoming tick in this MoveEvent.
         */

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
