package me.kenchh.checks.cheatchecks;

import me.kenchh.checks.CheatCategory;
import me.kenchh.checks.Check;
import me.kenchh.checks.CheckManager;
import me.kenchh.checks.CheckMode;
import me.kenchh.checks.fails.FailType;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.main.Eclipse;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class GroundSpoof extends Check {
    public GroundSpoof() {
        super("GroundSpoof", CheatCategory.MOVEMENT);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());

        for(CheckMode cm : CheckManager.checkModes) {
            if(dp.currentCheckMode == cm) {
                cm.groundspoof(this, e);
            }
        }

        if(dp.currentCheckMode != null && dp.currentCheckMode.isIgnored(this) == false) {
            return;
        }

        Player p = e.getPlayer();

        if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if(p.getAllowFlight()) {
            return;
        }

        if(p.isOnGround() != dp.onGround() && dp.airticks >= 3) {
            fail(p, FailType.A);
        }

        if(checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "onGround: " + p.isOnGround());
            p.sendMessage(Eclipse.prefix + "customOnGround: " + dp.onGround());
        }
    }
}
