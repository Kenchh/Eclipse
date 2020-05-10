package me.kenchh.checks.cheatchecks;

import me.kenchh.checks.CheatCategory;
import me.kenchh.checks.Check;
import me.kenchh.checks.CheckManager;
import me.kenchh.checks.CheckMode;
import me.kenchh.checks.fails.FailType;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.main.Eclipse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Motion extends Check {
    public Motion() {
        super("Motion", CheatCategory.MOVEMENT);

    }

    @Override
    public void fail(Player player, FailType type) {
        super.fail(player, type);

        DataProfile dp = DataProfileManager.getDataProfile(player);

        if(dp.lastLocationOnGround != null) {
            if (dp.lastLocationOnGround.getY() >= 0) {
                player.teleport(dp.lastLocationOnGround);
            }
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());

        for(CheckMode cm : CheckManager.checkModes) {
            if(dp.currentCheckMode == cm) {
                cm.motion(this, e);
            }
        }

        if(dp.currentCheckMode != null && dp.currentCheckMode.isIgnored(this) == false) {
            return;
        }

        Player p = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();

        double deltaY = to.getY() - from.getY();

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

        double deltadeltaY = deltaY - dp.lastDeltaY;

        /** A: Fails, if difference between current and last deltaY is too high.*/
        if(deltadeltaY <= -0.8) {
            fail(p, FailType.A);
        }

        /** B: Checks if this wrong deltaY occurs at a wrong time when the player is in the air **/
        if (deltaY > 0.42D + jumpboost/10 && dp.airticks >= 3 + jumpboost) {
            fail(p, FailType.B);
            if(failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY + " | " + "airticks: " + dp.airticks);
        }

        if(checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "deltaY: " + deltaY);
            p.sendMessage(Eclipse.prefix + "airticks: " + dp.airticks);
            p.sendMessage(Eclipse.prefix + "deltadeltaY: " + deltadeltaY);
        }

    }

}
