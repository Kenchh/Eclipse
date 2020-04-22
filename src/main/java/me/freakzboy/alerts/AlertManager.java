package me.freakzboy.alerts;

import me.freakzboy.checks.Check;
import me.freakzboy.checks.fails.Fail;
import me.freakzboy.main.Eclipse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AlertManager {

    public static HashMap<String, Long> cooldown = new HashMap<String, Long>();

    public static void updateCooldown() {
        Set<String> pnames = AlertManager.cooldown.keySet();

        for(String pname : pnames) {
            Player p = Bukkit.getPlayer(pname);
            if(p == null) {
                AlertManager.cooldown.remove(pname);
            }

            long then = AlertManager.cooldown.get(pname);
            long now = System.currentTimeMillis();

            long dt = now - then;

            long dtInSeconds = dt / 1000;

            if(dtInSeconds >= 1) {
                AlertManager.cooldown.remove(pname);
            }
        }
    }

    public static void alert(Player player, Fail fail) {

        if(cooldown.containsKey(player.getName())) {
            return;
        }

        sendAlert(player, fail);
        cooldown.put(player.getName(), System.currentTimeMillis());

    }

    public static void sendAlert(Player player, Fail fail) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.hasPermission("eclipse.alerts")) {
                p.sendMessage(Eclipse.prefix + ">> " + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has failed " + ChatColor.GOLD + fail.check.name + " (" + fail.type + ") " + ChatColor.RED + fail.VL);
            }
        }
    }

}
