package me.kenchh.checks;

import java.util.HashMap;

public abstract class CheckMode {

    public String name;

    private HashMap<Check, Check> customchecks = new HashMap<Check, Check>();

    public CheckMode(String name) {
        this.name = name;
    }

    public void customCheck(Check check, Check customcheck) {
        if(customchecks.containsKey(check) == false) {
            customchecks.put(check, customcheck);
        }
    }

    public Check getCustomCheck(Check check) {
        if(customchecks.containsKey(check)) {
            return customchecks.get(check);
        }
        return null;
    }

    public boolean isDefault(Check check) {
        if(customchecks.containsKey(check)) {
            return false;
        }
        return true;
    }
}
