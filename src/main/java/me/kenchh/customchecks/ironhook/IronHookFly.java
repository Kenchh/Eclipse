package me.kenchh.customchecks.ironhook;

import me.kenchh.checks.cheatchecks.movement.Fly;
import me.kenchh.checks.fails.FailType;
import me.kenchh.data.DataProfile;
import me.kenchh.main.Eclipse;
import me.kenchh.utils.ConnectionUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IronHookFly extends Fly {

    @Override
    public void move(PlayerMoveEvent e, DataProfile dp, Player p, double deltaY, double deltaH, double deltadeltaY, double deltadeltaH) {
        Location from = e.getFrom();
        Location to = e.getTo();

        if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if(p.getAllowFlight()) {
            return;
        }

        double jumpboost = 0;
        for(PotionEffect pe : p.getActivePotionEffects()) {
            if(pe.getType().getName() == PotionEffectType.JUMP.getName()) {
                jumpboost = pe.getAmplifier() + 1;
            }
        }

        if(dp.onGround() == false) {

            /** B: Non complex code, just flags if movement is very rapid and unusual. */
            if (deltaY >= 1.0 + jumpboost/10 && dp.airticks >= 3) {
                fail(p, FailType.B, "dY: " + deltaY);
            }

            /** D: Fails, if players deltaY is above a specific value and has barely changed compared to last tick. */
            int D_maxairticks = 10;
            if (ConnectionUtils.getPing(p) >= 60) {
                if(ConnectionUtils.getPing(p) < 250) {
                    D_maxairticks = D_maxairticks * ConnectionUtils.getPing(p) / 60;
                } else {
                    D_maxairticks = D_maxairticks * 250 / 60;
                }
            }

            if (deltaY >= 0.30 && dp.airticks >= D_maxairticks + jumpboost && (deltadeltaY >= -0.15 && deltadeltaY <= 0.15)) {
                fail(p, FailType.D, "dY: " + deltaY + " " + "aT: " + dp.airticks + " ddY: " + deltadeltaY);
            }

            /** AA: Fails, if player is too long in the air and still has a positive change in y-coordinate */
            int AA_maxairticks = 25;
            if (ConnectionUtils.getPing(p) >= 40) {
                if(ConnectionUtils.getPing(p) < 250) {
                    AA_maxairticks = AA_maxairticks * ConnectionUtils.getPing(p) / 40;
                } else {
                    AA_maxairticks = AA_maxairticks * 250 / 40;
                }
            }

            if (deltaY >= 0.0 && dp.airticks >= AA_maxairticks) {
                fail(p, FailType.AA, "dY: " + deltaY + " " + "aT: " + dp.airticks);
            }

            /** C: Fails, when X or Z changes while deltaY is 0. **/
            int C_maxairticks = 15;

            if (ConnectionUtils.getPing(p) >= 40) {
                if(ConnectionUtils.getPing(p) < 250) {
                    C_maxairticks = C_maxairticks * ConnectionUtils.getPing(p) / 40;
                } else {
                    C_maxairticks = C_maxairticks * 250 / 35;
                }
            }

            if(deltaY == 0 && dp.airticks > C_maxairticks) {
                if(to.getX() != from.getX() || to.getZ() != from.getZ()) {
                    fail(p, FailType.C, "dY: " + deltaY + " " + "aT: " + dp.airticks);
                }
            }

            /** A: Checks if player is moving while ddY is 0 (basically almost impossible to reproduce legit) */
            if(deltadeltaY == 0 && dp.airticks > 3) {
                fail(p, FailType.A, "aT: " + dp.airticks);
            }

        } else {
            dp.lastLocationOnGround = p.getLocation();
        }

        if(checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "oG: " + p.isOnGround());
            p.sendMessage(Eclipse.prefix + "aT: " + dp.airticks);
            p.sendMessage(Eclipse.prefix + "dY: " + deltaY);
            p.sendMessage(Eclipse.prefix + "ddY: " + deltadeltaY);
            p.sendMessage(Eclipse.prefix + "coG: " + dp.onGround());
        }
    }
}
