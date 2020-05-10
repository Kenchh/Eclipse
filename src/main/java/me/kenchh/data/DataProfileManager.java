package me.kenchh.data;

import me.kenchh.main.Eclipse;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DataProfileManager {

    public static ArrayList<DataProfile> dataProfiles = new ArrayList<DataProfile>();

    public static void addDataProfile(Player player) {
        if(hasDataProfile(player) == false) {
            Eclipse.getInstance().getPacketInjector().addPlayer(player);
            dataProfiles.add(new DataProfile(player));
        }

    }

    public static void removeDataProfile(Player player) {
        if(hasDataProfile(player)) {
            Eclipse.getInstance().getPacketInjector().removePlayer(player);
            DataProfile dp = getDataProfile(player);
            dataProfiles.remove(dp);
        }
    }

    public static boolean hasDataProfile(Player player) {
        for(DataProfile dp : dataProfiles) {
            if(dp.player.getUniqueId().toString() == player.getUniqueId().toString()) {
                return true;
            }
        }
        return false;
    }

    public static DataProfile getDataProfile(Player player) {
        for(DataProfile dp : dataProfiles) {
            if(dp.player.getName() == player.getName()) {
                return dp;
            }
        }

        return null;
    }

}
