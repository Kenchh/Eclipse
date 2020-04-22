package me.freakzboy.checks.fails;

import me.freakzboy.checks.Check;

public class Fail {

    public Check check;
    public int VL;
    public FailType type;

    public Fail(Check check, int violationlevel, FailType type) {
        this.check = check;
        this.VL = violationlevel;
        this.type = type;
    }

}
