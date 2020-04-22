package me.freakzboy.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LocationUtils {

    public static Block getBlockUnder(Player player, int minusy) {
        Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - minusy, player.getLocation().getZ());
        Block b = player.getWorld().getBlockAt(loc);

        return b;
    }

    public static Block[] getBlocksAround(Player player, int minusy) {

        Block[] blocksaround = new Block[8];

        Block locXP = new Location(player.getWorld(), player.getLocation().getX() + 0.5, player.getLocation().getY() - minusy, player.getLocation().getZ()).getBlock();
        Block locXM = new Location(player.getWorld(), player.getLocation().getX() - 0.5, player.getLocation().getY() - minusy, player.getLocation().getZ()).getBlock();
        Block locZP = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - minusy, player.getLocation().getZ() + 0.5).getBlock();
        Block locZM = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - minusy, player.getLocation().getZ() - 0.5).getBlock();

        Block locXPZP = new Location(player.getWorld(), player.getLocation().getX() + 0.5, player.getLocation().getY() - minusy, player.getLocation().getZ() + 0.5).getBlock();
        Block locXPZM = new Location(player.getWorld(), player.getLocation().getX() + 0.5, player.getLocation().getY() - minusy, player.getLocation().getZ() - 0.5).getBlock();
        Block locXMZP = new Location(player.getWorld(), player.getLocation().getX() - 0.5, player.getLocation().getY() - minusy, player.getLocation().getZ() + 0.5).getBlock();
        Block locXMZM = new Location(player.getWorld(), player.getLocation().getX() - 0.5, player.getLocation().getY() - minusy, player.getLocation().getZ() - 0.5).getBlock();

        blocksaround[0] = locXP;
        blocksaround[1] = locXM;
        blocksaround[2] = locZP;
        blocksaround[3] = locZM;

        blocksaround[4] = locXPZP;
        blocksaround[5] = locXPZM;
        blocksaround[6] = locXMZP;
        blocksaround[7] = locXMZM;

        return blocksaround;
    }

}
