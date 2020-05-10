package me.kenchh.checks;

import me.kenchh.checks.cheatchecks.FabricatedMovement;
import me.kenchh.checks.cheatchecks.Fly;
import me.kenchh.checks.cheatchecks.GroundSpoof;
import me.kenchh.checks.cheatchecks.Motion;
import me.kenchh.checks.checkmodes.Leap;

import java.util.ArrayList;

public class CheckManager {

    public static ArrayList<CheckMode> checkModes = new ArrayList<CheckMode>();
    public static ArrayList<Check> checks = new ArrayList<Check>();

    public static void initChecks() {
        checks.add(new Fly());
        checks.add(new Motion());
        checks.add(new GroundSpoof());
        checks.add(new FabricatedMovement());
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

}
