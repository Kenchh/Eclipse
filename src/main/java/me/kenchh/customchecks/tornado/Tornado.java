package me.kenchh.customchecks.tornado;

import me.kenchh.checks.CheckManager;
import me.kenchh.checks.CheckMode;

public class Tornado extends CheckMode {

    public Tornado() {
        super("Tornado");
        this.customCheck(CheckManager.getCheck("Fly"), new TornadoFly());
        this.customCheck(CheckManager.getCheck("Motion"), new TornadoMotion());
    }
}
