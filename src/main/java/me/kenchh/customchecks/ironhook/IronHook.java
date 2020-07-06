package me.kenchh.customchecks.ironhook;

import me.kenchh.checks.CheckManager;
import me.kenchh.checks.CheckMode;

public class IronHook extends CheckMode {
    public IronHook() {
        super("Iron Hook");
        this.customCheck(CheckManager.getCheck("Fly"), new IronHookFly());
        this.customCheck(CheckManager.getCheck("Motion"), new IronHookMotion());
    }
}
