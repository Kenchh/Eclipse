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

    public String message = Eclipse.prefix + ChatColor.YELLOW + "%player%" + ChatColor.GRAY + " has failed " + ChatColor.YELLOW + "%check% (%type%) " + ChatColor.RED + "VL: %vl%";
    public String messageCM = ChatColor.GRAY + " while " + ChatColor.YELLOW + "%checkmode%";

    public HashMap<String, Long> delay = new HashMap<String, Long>();

    public final long alertDelay = 1;

    public void updateCooldown() {
        Set<String> pnames = delay.keySet();

        for(String pname : pnames) {
            Player p = Bukkit.getPlayer(pname);
            if(p == null) {
                delay.remove(pname);
                continue;
            }

            long then = delay.get(pname);
            long now = System.currentTimeMillis();

            long dt = now - then;

            long dtInSeconds = dt / 1000;

            if(dtInSeconds >= alertDelay) {
                delay.remove(pname);
            }
        }
    }

    /* Alerts, when a player fails a check. */
    public void alert(Player player, Fail fail, String debugmsg) {

        if(delay.containsKey(player.getName()))
            return;

        sendAlert(player, fail);
        sendDebugMessage("ping: " + ConnectionUtils.getPing(player) + "ms " + debugmsg);
        delay.put(player.getName(), System.currentTimeMillis());

    }

    /* Alerts, when a player has failed a check too many times. */
    public void alertPunish(Player player, Fail fail) {
        sendKickAlert(player, fail);
    }

    public void sendDebugMessage(String debugmsg) {

        for(Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eclipse.alerts")) {
                p.sendMessage(ChatColor.GRAY + debugmsg);
            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + debugmsg);
    }

    private void sendAlert(Player player, Fail fail) {
        DataProfile dp = DataProfileManager.getDataProfile(player);

        String msg = message;
        msg = msg.replace("%player%", player.getName());
        msg = msg.replace("%check%", fail.check.name);
        msg = msg.replace("%type%", fail.type.toString());
        msg = msg.replace("%vl%", "" + fail.VL);

        String msgCM = messageCM;

        /* Player Staff Message */
        for(Player staffmember : Bukkit.getOnlinePlayers()) {
            if(staffmember.hasPermission("eclipse.alerts")) {

                if(!DataProfileManager.getDataProfile(player).alerts)
                    continue;

                if(dp.currentCheckMode == null) {
                    staffmember.sendMessage(msg);
                } else {
                    msgCM = msgCM.replace("%checkmode%", dp.currentCheckMode.name);
                    staffmember.sendMessage(msg + msgCM);
                }
            }
        }

        /* Console Print */
        if(dp.currentCheckMode == null) {
            Bukkit.getConsoleSender().sendMessage(msg);
        } else {
            Bukkit.getConsoleSender().sendMessage(msg + msgCM);
        }
    }

    private void sendKickAlert(Player player, Fail fail) {

        /* Player Staff Message */
        for(Player staffmember : Bukkit.getOnlinePlayers()) {
            if(staffmember.hasPermission("eclipse.alerts")) {
                staffmember.sendMessage(ChatColor.RED + player.getName() + " has been removed for failing " + fail.check.name + " too many times!");
            }
        }

        /* Console Print */
        Bukkit.getConsoleSender().sendMessage(player.getName() + " has been removed for failing " + fail.check.name + " too many times!");

    }

}
