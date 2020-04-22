package me.freakzboy.checks;

import me.freakzboy.alerts.AlertManager;
import me.freakzboy.checks.fails.Fail;
import me.freakzboy.checks.fails.FailProfile;
import me.freakzboy.checks.fails.FailProfileManager;
import me.freakzboy.checks.fails.FailType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Check implements Listener {

    public String name;
    public CheatCategory cheat;
    public boolean debug = false;

    public Check(String name, CheatCategory cheat) {
        this.name = name;
        this.cheat = cheat;
    }

    public void toggleDebug() {
        debug = !debug;
    }

    public void fail(Player player, FailType type) {

        FailProfileManager.addFailProfile(player);
        FailProfile fp = FailProfileManager.getFailProfile(player);

        int VL = 0;

        Fail failtoremove = null;

        for(Fail f : fp.fails) {
            if(f.check == this) {
                VL = f.VL;
                failtoremove = f;
            }
        }

        if(failtoremove != null) {
            fp.fails.remove(failtoremove);
        }

        Fail newfail = new Fail(this, VL + 1, type);

        fp.addFail(newfail);
        AlertManager.alert(player, newfail);

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

    public boolean debugAllowed(Player player) {
        if(debug == true) {
            if (player.hasPermission("eclipse.debug")) {
                return true;
            }
        }
        return false;
    }

}
