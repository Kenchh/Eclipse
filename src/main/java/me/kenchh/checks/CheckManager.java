package me.kenchh.checks;

import me.kenchh.checks.cheatchecks.movement.Fly;
import me.kenchh.checks.cheatchecks.movement.GroundSpoof;
import me.kenchh.checks.cheatchecks.movement.Motion;
import me.kenchh.checks.cheatchecks.movement.Speed;
import me.kenchh.customchecks.leap.Leap;

import java.util.ArrayList;

public class CheckManager {

    public static ArrayList<CheckMode> checkModes = new ArrayList<CheckMode>();
    public static ArrayList<Check> checks = new ArrayList<Check>();

    public static void initChecks() {
        checks.add(new Fly());
        checks.add(new Motion());
        checks.add(new GroundSpoof());
        checks.add(new Speed());
    }

    public static void initCheckModes() {
        checkModes.add(new Leap());
    }

    public static Check getCheck(String name) {
        for(Check c : checks) {
            if(c.name.equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public static CheckMode getCheckModeByName(String name) {
        for(CheckMode cm : checkModes) {
            if(name.equalsIgnoreCase(cm.name)) {
                return cm;
            }
        }
        return null;
    }

    public static Check getParentCheck(Check customcheck) {

        for(Check c : checks) {
            String cname = c.getClass().getSimpleName();

            if(customcheck.getClass().getSuperclass().getSimpleName().equalsIgnoreCase(cname)) {
                return c;
            }
        }
        return null;
    }

    public static boolean isCustom(Check customcheck) {
        if(getParentCheck(customcheck) != null) {
            return true;
        }
        return false;
    }

}
