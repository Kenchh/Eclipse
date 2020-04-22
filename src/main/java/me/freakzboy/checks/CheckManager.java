package me.freakzboy.checks;

import me.freakzboy.checks.cheatchecks.Fly;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class CheckManager {

    public static ArrayList<Check> checks = new ArrayList<Check>();

    public static void initChecks() {
        checks.add(new Fly());
    }

    public static Check getCheck(String name) {
        for(Check c : checks) {
            if(c.name.equalsIgnoreCase(name)) {
                return c;
            } else {
                Bukkit.broadcastMessage(name);
                Bukkit.broadcastMessage(c.name);
            }
        }
        return null;
    }

}
