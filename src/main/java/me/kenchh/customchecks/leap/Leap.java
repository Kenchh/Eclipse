package me.kenchh.customchecks.leap;

import me.kenchh.checks.CheckManager;
import me.kenchh.checks.CheckMode;

public class Leap extends CheckMode {

    public Leap() {
        super("Leap");
        customCheck(CheckManager.getCheck("Fly"), new LeapFly());
        customCheck(CheckManager.getCheck("Motion"), new LeapMotion());
        customCheck(CheckManager.getCheck("Speed"), new LeapSpeed());
    }

}
