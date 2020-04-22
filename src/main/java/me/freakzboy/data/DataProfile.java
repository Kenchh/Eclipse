package me.freakzboy.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DataProfile {

    public Player player;

    public int airticks = 0; /** Time in air */
    public int customairticks = 0; /** Time in air, customly calculated */

    public int maxairticks = 22; /** Max time in air for a fail-condition to be true */
    public int ping_maxairticks; /** Max time in air for a fail condition to be true, dependent on the ping the players has. */

    public double deltaY;

    public DataProfile(Player player) {
        this.player = player;
    }

    public void updateAirTicks() {
        if(player.isOnGround() == false && player.getWorld().getBlockAt(new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getY() - 0.5, player.getLocation().getZ())).isLiquid() == false) {
            airticks++;
        } else {
            airticks = 0;
        }
    }

    /** Debug Info **/
    public boolean debug_fly_blocksaroundair;

}
