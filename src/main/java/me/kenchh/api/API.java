package me.kenchh.api;

import me.kenchh.checks.CheckManager;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import org.bukkit.entity.Player;

public class API {

    public static void setCheckMode(Player p, String checkmode) {
        DataProfile dp = DataProfileManager.getDataProfile(p);
        dp.currentCheckMode = CheckManager.getCheckModeByName(checkmode);
    }

}
