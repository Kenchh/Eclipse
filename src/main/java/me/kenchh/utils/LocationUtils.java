package me.kenchh.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class LocationUtils {

    public static List<Material> stepBlocks = Arrays.asList(
            Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS,
            Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.RED_SANDSTONE_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS,
            Material.WOOD_STAIRS,
            Material.STONE_SLAB2, Material.STEP, Material.WOOD_STEP);

    /* Blocks, that lift you up outside the block Y coordinate */
    public static List<Material> liftingBlocks = Arrays.asList(
            Material.FENCE, Material.FENCE_GATE, Material.BIRCH_FENCE, Material.ACACIA_FENCE, Material.DARK_OAK_FENCE, Material.JUNGLE_FENCE, Material.NETHER_FENCE,
            Material.SPRUCE_FENCE, Material.SPRUCE_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.ACACIA_FENCE_GATE,
            Material.COBBLE_WALL);

    public static Block getBlockUnder(Player player, double minusy) {
        Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - minusy, player.getLocation().getZ());
        Block b = player.getWorld().getBlockAt(loc);

        return b;
    }

    public static Block[] getBlocksAround(Player player, double minusy) {

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

    public static boolean checkCustomOnGround(Player player) {
        if(isAir(getBlockUnder(player, 0.25).getLocation()) && isOnLiftBlocks(player) == false && blocksAroundAir(player) == true) {
            return false;
        }
        return true;
    }

    public static boolean blocksAroundAir(Player p) {

        for(Block b : LocationUtils.getBlocksAround(p, 0.25)) {
            if(LocationUtils.isAir(b.getLocation()) == false) {
                return false;
            }
        }
        return true;

    }

    public static boolean isAir(Location loc) {
        if(loc.getBlock().getType().isSolid() == false && loc.getBlock().isLiquid() == false && loc.getBlock().getType() != Material.LADDER && loc.getBlock().getType() != Material.VINE) {
            return true;
        }
        return false;
    }

    public static boolean isOnLiftBlocks(Player p) {
        for(Block b : LocationUtils.getBlocksAround(p, 1)) {
            if(LocationUtils.liftingBlocks.contains(b.getType())) {
                return true;
            }
        }
        return false;
    }

}
