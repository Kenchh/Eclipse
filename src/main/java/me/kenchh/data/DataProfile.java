package me.kenchh.data;

import me.kenchh.checks.Check;
import me.kenchh.checks.CheckMode;
import me.kenchh.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DataProfile {

    /**
     *  Basically a "player-cache", where all data is stored and accessed for checks.
     */

    public Player player;

    public boolean graceperiod = true;

    public CheckMode currentCheckMode = null;
    public int checkModeDuration = 20;

    public int airticks = 0; /** Time in air */
    public int cAirticks = 0; /** Time in air - Custom */

    public int AAairticks = 0;

    public int vanillaOnGroundTicks = 0;
    public boolean lastVanillaOnGround;

    public int groundspoofticks;

    public double lastDeltaY = 0;
    public double lastDeltaH = 0;

    public double lastDeltaDeltaY = 0;
    public double lastDeltaDeltaH = 0;

    public Location lastLocation;
    public long lastLocationMillis = 0;
    public Location lastLocationOnGround;

    public int speedticks = 0;

    public long timerLastMillis = System.currentTimeMillis();
    public int timerPackets;
    public int speedPackets;

    public int hurtticks;

    public HashMap<Check, Double> ignoredChecks = new HashMap<>();

    public boolean alerts = true;

    public DataProfile(Player player) {
        this.player = player;
    }

    /** Custom-made onGround check */
    public boolean onGround() {
        if(LocationUtils.checkCustomOnGround(player)) {
            cAirticks = 0;
        }
        return LocationUtils.checkCustomOnGround(player);
    }

    public void updateAirTicks() {
        if(!player.isOnGround()) {
            airticks++;
        } else {
            airticks = 0;
        }
    }

    public void updateCustomAirTicks() {
        if(!LocationUtils.checkCustomOnGround(player)) {
            cAirticks++;
        } else {
            cAirticks = 0;
        }
    }

    public void updateVanillaOnGroundTicks() {
        if(player.isOnGround() && vanillaOnGroundTicks <= 200) {
            vanillaOnGroundTicks++;
        } else {
            vanillaOnGroundTicks = 0;
        }
    }

    public void updatecheckModeDuration() {
        if(checkModeDuration <= 0) {
            checkModeDuration = 0;
            currentCheckMode = null;
        } else {
            checkModeDuration--;
        }
    }

    public void updateIgnoreCheckDuration() {
        for(Check c : ignoredChecks.keySet()) {
            double seconds = ignoredChecks.get(c);
            if(seconds > 0) {
                ignoredChecks.replace(c, seconds - 1);
            } else {
                ignoredChecks.remove(c);
            }
        }
    }

    public void updateHurtTicks() {
        if(hurtticks > 0) hurtticks--;
    }

}
