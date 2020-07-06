package me.kenchh.customchecks.tornado;

import me.kenchh.checks.CheatCategory;
import me.kenchh.checks.Check;
import me.kenchh.checks.cheatchecks.movement.Motion;
import me.kenchh.checks.fails.FailType;
import me.kenchh.checks.interfaces.Movement;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.main.Eclipse;
import me.kenchh.utils.ConnectionUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TornadoMotion extends Motion {

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

        double jumpboost = 0;
        for(PotionEffect pe : p.getActivePotionEffects()) {
            if(pe.getType().getName() == PotionEffectType.JUMP.getName()) {
                jumpboost = pe.getAmplifier() + 1;
            }
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

        if (deltaY > 0.9D + jumpboost/10 && dp.airticks > B_maxairticks + jumpboost) {
            fail(p, FailType.B, "dY: " + deltaY + " " + "aT: " + dp.airticks);
        }

        if(checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "dY: " + deltaY);
            p.sendMessage(Eclipse.prefix + "dH: " + deltaH);
            p.sendMessage(Eclipse.prefix + "aT: " + dp.airticks);
            p.sendMessage(Eclipse.prefix + "ddY: " + deltadeltaY);
            p.sendMessage(Eclipse.prefix + "ddH: " + deltadeltaH);
        }
    }

}
