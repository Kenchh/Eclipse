package me.kenchh.checks.cheatchecks;

import me.kenchh.checks.CheatCategory;
import me.kenchh.checks.Check;
import me.kenchh.checks.CheckManager;
import me.kenchh.checks.CheckMode;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class FabricatedMovement extends Check {

    public FabricatedMovement() {
        super("FabricatedMovement", CheatCategory.MOVEMENT);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());

        for(CheckMode cm : CheckManager.checkModes) {
            if(dp.currentCheckMode == cm) {
                cm.fabricatedmovement(this, e);
            }
        }

        if(dp.currentCheckMode != null && dp.currentCheckMode.isIgnored(this) == false) {
            return;
        }
    }
}
