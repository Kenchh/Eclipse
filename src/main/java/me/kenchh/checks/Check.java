package me.kenchh.checks;

import me.kenchh.alerts.AlertManager;
import me.kenchh.checks.fails.Fail;
import me.kenchh.checks.fails.FailProfile;
import me.kenchh.checks.fails.FailProfileManager;
import me.kenchh.checks.fails.FailType;
import me.kenchh.data.DataProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class Check implements Listener {

    public String name;
    public CheatCategory cheat;
    public boolean checkdebug = false;
    public boolean faildebug = false;

    public Check(String name, CheatCategory cheat) {
        this.name = name;
        this.cheat = cheat;
    }

    public void toggleCheckDebug() {
        checkdebug = !checkdebug;
    }
    public void toggleFailDebug() {
        faildebug = !faildebug;
    }

    /** Used to add +1 VL to a player fail. */
    public void fail(Player player, FailType type, String debugmsg)  {
        fail(player, 1, type, debugmsg);
    }

    /** Used to add a specific amount of VL */
    public void fail(Player player, int VL, FailType type, String debugmsg) {

        if(DataProfileManager.getDataProfile(player).graceperiod) {
            return;
        }

        Check c = this;

        if(CheckManager.isCustom(this)) {
            c = CheckManager.getParentCheck(this);
        }

        FailProfileManager.addFailProfile(player);
        FailProfile fp = FailProfileManager.getFailProfile(player);

        int fVL = 0;

        Fail failtoremove = null;

        for(Fail f : fp.fails) {
            if(f.check == c) {
                fVL = f.VL;
                failtoremove = f;
            }
        }

        if(failtoremove != null) {
            fp.fails.remove(failtoremove);
        }

        Fail newfail = new Fail(c, fVL + VL, type);

        fp.addFail(newfail);
        AlertManager.alert(player, newfail, debugmsg);

    }

    /** Checks, if a player is allowed to retrieve debug info about checks. */
    public boolean checkDebugAllowed(Player player) {
        if(checkdebug == true) {
            if (player.hasPermission("eclipse.debug")) {
                return true;
            }
        }
        return false;
    }

}
