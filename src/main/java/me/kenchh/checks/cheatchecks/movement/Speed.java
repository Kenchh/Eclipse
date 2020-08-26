package me.kenchh.checks.cheatchecks.movement;

import me.kenchh.checks.CheatCategory;
import me.kenchh.checks.Check;
import me.kenchh.checks.fails.FailType;
import me.kenchh.checks.interfaces.Movement;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.main.Eclipse;
import me.kenchh.packet.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class Speed extends Check implements Movement, PacketListener {
    public Speed() {
        super("Speed", CheatCategory.MOVEMENT);
    }

    @Override
    public void fail(Player player, FailType type, String debugmsg) {
        super.fail(player, type, debugmsg);

        if(type == FailType.D) {
            return;
        }

        DataProfile dp = DataProfileManager.getDataProfile(player);

        dp.speedticks = 20;
        if(dp.lastLocation != null) {
            player.teleport(dp.lastLocation);
        }

    }

    @Override
    public void move(PlayerMoveEvent e, DataProfile dp, Player p, double deltaY, double deltaH, double deltadeltaY, double deltadeltaH) {

        if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if(p.getAllowFlight()) {
            return;
        }

        double speedboost = 0;

        if(p.getWalkSpeed() > 0.2) {
            speedboost+=(p.getWalkSpeed()-0.2)/0.02;
            if(p.isSprinting()) {
                speedboost+=((p.getWalkSpeed()-0.2)/0.02) * 0.3;
            }
        }

        for(PotionEffect pe : p.getActivePotionEffects()) {
            if(pe.getType().getName() == PotionEffectType.SPEED.getName()) {
                speedboost += pe.getAmplifier() + 1;
            }
        }


        if(dp.hurtticks != 0) {
            return;
        }

        /** A & AA **/
        if(p.isOnGround() && dp.vanillaOnGroundTicks >= 3) {
            if (deltadeltaH >= 0.4 + 0.281*0.2*speedboost) {
                fail(p, FailType.A, "ddH: " + deltadeltaH + " voGT: " + dp.vanillaOnGroundTicks + " oG: " + p.isOnGround());
            }
        } else {
            if(deltadeltaH >= 0.4 + 0.35*0.2*speedboost) {
                fail(p, FailType.AA, "ddH: " + deltadeltaH + " voGT: " + dp.vanillaOnGroundTicks + " oG: " + p.isOnGround());
            }
        }


        /** B: */
        if(deltaH >= 0.62 + 0.325*0.2*speedboost) {
            fail(p, FailType.B, "dH: " + deltaH + " voGT: " + dp.vanillaOnGroundTicks + " oG: " + p.isOnGround());
        }


        if(checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "dH: " + deltaH);
            p.sendMessage(Eclipse.prefix + "ddH: " + deltadeltaH);
            p.sendMessage(Eclipse.prefix + "voGT: " + dp.vanillaOnGroundTicks);
        }
    }

    @Override
    public void readPacket(Player p, Object packet) {

        DataProfile dp = DataProfileManager.getDataProfile(p);

        if(System.currentTimeMillis() - dp.timerLastMillis >= 1000) {
            dp.speedPackets = 0;
        }

        String[] moves = {"PacketPlayInPosition", "PacketPlayInPositionLook"};

        boolean isPacket = false;

        for(String s : moves) {
            if(packet.getClass().getSimpleName().equalsIgnoreCase(s)) {
                isPacket = true;
                break;
            }
        }

        if(isPacket)
            dp.speedPackets++;

        if(dp.speedPackets > 26) {
            fail(p, FailType.D, "p: " + dp.speedPackets);
        }
    }
}
