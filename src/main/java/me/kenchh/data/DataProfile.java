package me.kenchh.data;

import me.kenchh.checks.CheckMode;
import me.kenchh.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DataProfile {

    public Player player;
    public CheckMode currentCheckMode = null;

    public int airticks = 0; /** Time in air */
    public int customairticks = 0; /** Time in air, customly calculated */

    public double lastDeltaY = 0;

    public Location lastLocationOnGround;

    public DataProfile(Player player) {
        this.player = player;
    }

    public boolean onGround() {
        return LocationUtils.checkCustomOnGround(player);
    }

    public void updateAirTicks() {
        if(onGround() == false) {
            airticks++;
        } else {
            airticks = 0;
        }
    }

}
