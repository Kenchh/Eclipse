package me.kenchh.api;

import me.kenchh.checks.CheckManager;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import org.bukkit.entity.Player;

public class API {

    public void setCheckMode(Player p, String checkmode, double seconds) {
        DataProfile dp = DataProfileManager.getDataProfile(p);
        dp.currentCheckMode = CheckManager.getCheckModeByName(checkmode);
        dp.airticks = 0;
        dp.checkModeResetCooldown = (int) (seconds * 20);
    }

    public void setAirTicks(Player p, int value) {
        DataProfile dp = DataProfileManager.getDataProfile(p);
        if(value < 0) return;
        dp.airticks = value;
    }

    public int getAirTicks(Player p) {
        DataProfile dp = DataProfileManager.getDataProfile(p);
        return dp.airticks;
    }

}
