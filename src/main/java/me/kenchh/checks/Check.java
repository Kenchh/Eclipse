package me.kenchh.checks;

import me.kenchh.alerts.AlertManager;
import me.kenchh.checks.fails.Fail;
import me.kenchh.checks.fails.FailProfile;
import me.kenchh.checks.fails.FailProfileManager;
import me.kenchh.checks.fails.FailType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Check implements Listener {

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

    public void fail(Player player, FailType type) {
        fail(player, 1, type);
    }

    public void fail(Player player, int VL, FailType type) {

        FailProfileManager.addFailProfile(player);
        FailProfile fp = FailProfileManager.getFailProfile(player);

        int fVL = 0;

        Fail failtoremove = null;

        for(Fail f : fp.fails) {
            if(f.check == this) {
                fVL = f.VL;
                failtoremove = f;
            }
        }

        if(failtoremove != null) {
            fp.fails.remove(failtoremove);
        }

        Fail newfail = new Fail(this, fVL + VL, type);

        fp.addFail(newfail);
        AlertManager.alert(player, newfail);

    }

    public boolean checkDebugAllowed(Player player) {
        if(checkdebug == true) {
            if (player.hasPermission("eclipse.debug")) {
                return true;
            }
        }
        return false;
    }

    public boolean failDebugAllowed(Player player) {
        if(faildebug == true) {
            if (player.hasPermission("eclipse.debug")) {
                return true;
            }
        }
        return false;
    }

}
