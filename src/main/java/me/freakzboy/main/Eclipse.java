package me.freakzboy.main;

import mc.zan.moevents.MoEvents;
import me.freakzboy.alerts.AlertManager;
import me.freakzboy.checks.Check;
import me.freakzboy.checks.CheckManager;
import me.freakzboy.commands.EclipseCMD;
import me.freakzboy.data.DataProfile;
import me.freakzboy.data.DataProfileManager;
import me.freakzboy.events.JoinEvent;
import me.freakzboy.events.LeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class Eclipse extends JavaPlugin {

    public static String prefix = new String(ChatColor.YELLOW + "[" + ChatColor.GOLD + ChatColor.BOLD + "Eclipse" + ChatColor.YELLOW + "] " + ChatColor.YELLOW);

    @Override
    public void onEnable() {
        System.out.println("Eclipse >> Anticheat Started!");

        MoEvents.init(this);

        CheckManager.initChecks();

        /* Registering Global Events */
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new LeaveEvent(), this);

        /* Registering Events in Checks */
        for(Check c : CheckManager.checks) {
            Bukkit.getPluginManager().registerEvents(c, this);
        }

        Bukkit.getPluginCommand("eclipse").setExecutor(new EclipseCMD());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {

                AlertManager.updateCooldown();

                for(DataProfile dp : DataProfileManager.dataProfiles) {
                    dp.updateAirTicks();
                }

            }
        }, 1L, 1L);

    }



}
