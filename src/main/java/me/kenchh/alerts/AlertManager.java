package me.kenchh.alerts;

import me.kenchh.checks.fails.Fail;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.main.Eclipse;
import me.kenchh.utils.ConnectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

public class AlertManager {

    public static String message = Eclipse.prefix + ChatColor.YELLOW + "%player%" + ChatColor.GRAY + " has failed " + ChatColor.YELLOW + "%check% (%type%) " + ChatColor.RED + "VL: %vl%";
    public static String messageCM = ChatColor.GRAY + " while " + ChatColor.YELLOW + "%checkmode%";

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

            long dtInSeconds = dt / 300;

            if(dtInSeconds >= 1) {
                AlertManager.cooldown.remove(pname);
            }
        }
    }

    public static void alert(Player player, Fail fail, String debugmsg) {

        if(cooldown.containsKey(player.getName()))
            return;

        sendAlert(player, fail);
        sendDebugMessage("ping: " + ConnectionUtils.getPing(player) + "ms " + debugmsg);
        cooldown.put(player.getName(), System.currentTimeMillis());

    }

    public static void alertKick(Player player, Fail fail) {

        if(cooldown.containsKey(player.getName()))
            return;

        sendKickAlert(player, fail);
        cooldown.put(player.getName(), System.currentTimeMillis());

    }

    public static void sendDebugMessage(String debugmsg) {

        for(Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eclipse.alerts")) {
                p.sendMessage(ChatColor.GRAY + debugmsg);
            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + debugmsg);
    }

    public static void sendAlert(Player player, Fail fail) {
        DataProfile dp = DataProfileManager.getDataProfile(player);

        String msg = AlertManager.message;
        msg = msg.replace("%player%", player.getName());
        msg = msg.replace("%check%", fail.check.name);
        msg = msg.replace("%type%", fail.type.toString());
        msg = msg.replace("%vl%", "" + fail.VL);

        String messageCM = AlertManager.messageCM;

        /* Player Staff Message */
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.hasPermission("eclipse.alerts")) {

                if(dp.currentCheckMode == null) {
                    p.sendMessage(msg);
                } else {
                    messageCM = messageCM.replace("%checkmode%", dp.currentCheckMode.name);
                    p.sendMessage(msg + messageCM);
                }
            }
        }

        /* Console Print */
        if(dp.currentCheckMode == null) {
            Bukkit.getConsoleSender().sendMessage(msg);
        } else {
            Bukkit.getConsoleSender().sendMessage(msg + messageCM);
        }
    }

    public static void sendKickAlert(Player player, Fail fail) {

        /* Player Staff Message */
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.hasPermission("eclipse.alerts")) {
                p.sendMessage(ChatColor.RED + player.getName() + " has been removed for failing " + fail.check.name + " too many times!");
            }
        }

        /* Console Print */
        Bukkit.getConsoleSender().sendMessage(player.getName() + " has been removed for failing " + fail.check.name + " too many times!");

    }

}
