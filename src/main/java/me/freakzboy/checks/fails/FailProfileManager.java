package me.freakzboy.checks.fails;

import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FailProfileManager {

    public static ArrayList<FailProfile> failProfiles = new ArrayList<FailProfile>();

    public static void addFailProfile(Player player) {
        if(failProfiles.contains(player) == false) {
            failProfiles.add(new FailProfile(player));
        }
    }

    public static FailProfile getFailProfile(Player player) {
        for(FailProfile fp : failProfiles) {
            if(fp.player == player) {
                return fp;
            }
        }
        return null;
    }

}
