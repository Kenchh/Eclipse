package me.kenchh.checks.cheatchecks.movement;

import me.kenchh.checks.CheatCategory;
import me.kenchh.checks.Check;
import me.kenchh.checks.fails.FailType;
import me.kenchh.checks.interfaces.Movement;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.main.Eclipse;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Speed extends Check implements Movement {
    public Speed() {
        super("Speed", CheatCategory.MOVEMENT);
    }

    @Override
    public void fail(Player player, FailType type, String debugmsg) {
        super.fail(player, type, debugmsg);

        DataProfile dp = DataProfileManager.getDataProfile(player);

        dp.speedticks = 20;
        if(dp.lastLocation != null) {
            player.teleport(dp.lastLocation);
        }

    }

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

        double speedboost = 0;
        for(PotionEffect pe : p.getActivePotionEffects()) {
            if(pe.getType().getName() == PotionEffectType.SPEED.getName()) {
                speedboost = pe.getAmplifier() + 1;
            }
        }

        /** A: */
        if(p.isOnGround() && dp.vanillaOnGroundTicks >= 3) {
            if (deltadeltaH >= 0.281 + 0.281*0.2*speedboost) {
                fail(p, FailType.A, "ddH: " + deltadeltaH + " voGT: " + dp.vanillaOnGroundTicks + " oG: " + p.isOnGround());
            }
        } else {
            if(deltadeltaH >= 0.35 + 0.35*0.2*speedboost) {
                fail(p, FailType.AA, "ddH: " + deltadeltaH + " voGT: " + dp.vanillaOnGroundTicks + " oG: " + p.isOnGround());
            }
        }

        /** B: */
        if(p.isOnGround() && dp.vanillaOnGroundTicks >= 3 && deltaY == 0) {
            if(deltaH >= 0.325 + 0.325*0.2*speedboost) {
                fail(p, FailType.B, "dH: " + deltaH + " voGT: " + dp.vanillaOnGroundTicks + " oG: " + p.isOnGround());
            }
        }

        /*
        if((dp.lastDeltaDeltaH >= -0.01 && dp.lastDeltaDeltaH <= 0.01) && deltadeltaH > dp.lastDeltaDeltaH && p.isSprinting()) {
            fail(p, FailType.B, "ddH: " + deltadeltaH + " lddH: " + dp.lastDeltaDeltaH);
        }
        */

        if(checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "dH: " + deltaH);
            p.sendMessage(Eclipse.prefix + "ddH: " + deltadeltaH);
            p.sendMessage(Eclipse.prefix + "voGT: " + dp.vanillaOnGroundTicks);
        }
    }
}
