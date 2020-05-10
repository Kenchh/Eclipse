package me.kenchh.checks;

import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public abstract class CheckMode {

    public String name;

    private HashMap<Check, Boolean> ignores = new HashMap<Check, Boolean>();

    public CheckMode(String name) {
        this.name = name;

        for(Check c : CheckManager.checks) {
            ignores.put(c, true);
        }

    }

    public void setIgnore(Check check, boolean ignore) {
        if(ignores.containsKey(check)) {
            ignores.replace(check, ignore);
        }
    }

    public boolean isIgnored(Check check) {
        if(ignores.containsKey(check)) {
            return ignores.get(check);
        }
        return true;
    }

    public abstract void fly(Check check, PlayerMoveEvent e);
    public abstract void groundspoof(Check check, PlayerMoveEvent e);
    public abstract void motion(Check check, PlayerMoveEvent e);
    public abstract void fabricatedmovement(Check check, PlayerMoveEvent e);

}
