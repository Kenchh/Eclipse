package me.kenchh.main;

import me.kenchh.alerts.AlertManager;
import me.kenchh.api.API;
import me.kenchh.checks.Check;
import me.kenchh.checks.CheckManager;
import me.kenchh.commands.EclipseCMD;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.events.JoinEvent;
import me.kenchh.events.LeaveEvent;
import me.kenchh.events.MoveEvent;
import me.kenchh.packet.PacketInjector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Eclipse extends JavaPlugin {

    public static String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "ECLIPSE > " + ChatColor.RESET + ChatColor.GRAY;
    private static Eclipse instance;

    private static PacketInjector injector;
    public API api;

    @Override
    public void onEnable() {
        System.out.println("Eclipse >> Anticheat Started!");
        instance = this;

        this.injector = new PacketInjector();
        this.api = new API();

        CheckManager.initChecks();
        CheckManager.initCheckModes();

        /* Registering Events in Checks */
        for(Check c : CheckManager.checks) {
            Bukkit.getPluginManager().registerEvents(c, this);
        }

        /* Registering Global Events */
        Bukkit.getPluginManager().registerEvents(new MoveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new LeaveEvent(), this);

        Bukkit.getPluginCommand("eclipse").setExecutor(new EclipseCMD());

        for(Player p : Bukkit.getOnlinePlayers()) {
            DataProfileManager.addDataProfile(p);
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {

                AlertManager.updateCooldown();
                for(DataProfile dp : DataProfileManager.dataProfiles) {
                    dp.updateCheckModeResetCoolDown();
                }
            }
        }, 1L, 1L);

    }

    public static Eclipse getInstance() {
        return instance;
    }

    public static PacketInjector getPacketInjector() {
        return injector;
    }

}
