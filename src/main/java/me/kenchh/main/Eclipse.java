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
import me.kenchh.events.PlayerDamage;
import me.kenchh.packet.PacketInjector;
import me.kenchh.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Eclipse extends JavaPlugin {

    public static String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "ECLIPSE > " + ChatColor.RESET + ChatColor.GRAY;
    private static Eclipse instance;
    private static AlertManager alertmanager;
    private static PacketInjector injector;
    public API api;

    public boolean debug;

    @Override
    public void onEnable() {

        System.out.println("Eclipse >> Anticheat Started!");
        instance = this;

        this.injector = new PacketInjector();
        this.alertmanager = new AlertManager();
        this.api = new API();

        new LocationUtils();

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
        Bukkit.getPluginManager().registerEvents(new PlayerDamage(), this);

        Bukkit.getPluginCommand("eclipse").setExecutor(new EclipseCMD());

        for(Player p : Bukkit.getOnlinePlayers()) {
            DataProfileManager.addDataProfile(p);
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {

                alertmanager.updateCooldown();
                for(DataProfile dp : DataProfileManager.dataProfiles) {
                    dp.updatecheckModeDuration();
                    dp.updateIgnoreCheckDuration();
                    dp.updateHurtTicks();
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

    public static AlertManager getAlertManager() {
        return alertmanager;
    }

}
