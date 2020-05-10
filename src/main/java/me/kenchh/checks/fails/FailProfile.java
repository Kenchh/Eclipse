package me.kenchh.checks.fails;

import me.kenchh.checks.Check;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class FailProfile {

    public Player player;

    public ArrayList<Fail> fails = new ArrayList<Fail>();

    public FailProfile(Player player) {
        this.player = player;
    }

    public void addFail(Fail fail) {
        fails.add(fail);
    }

    public int getVL(Check check) {
        for(Fail f : fails) {
            if(f.check == check) {
                return f.VL;
            }
        }
        return 0;
    }

}
