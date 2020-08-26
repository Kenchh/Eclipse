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

    public static Block getBlockAbove(Player player, double plusy) {
        Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - plusy, player.getLocation().getZ());
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
        Material type = loc.getBlock().getType();
        if(type.isSolid() == false && loc.getBlock().isLiquid() == false &&
                type != Material.LADDER && type != Material.VINE && type != Material.WEB && type != Material.SNOW && !type.toString().contains("CARPET")) {
            return true;
        }
        return false;
    }

    public static boolean isOnLiftBlocks(Player p) {
        for(Block b : LocationUtils.getBlocksAround(p, 0.75)) {
            if(LocationUtils.liftingBlocks.contains(b.getType())) {
                return true;
            }
        }
        return false;
    }

    public static boolean couldBeOnGround(Player p) {

        double blockY = p.getLocation().getBlockY();
        double y = p.getLocation().getY() - blockY;

        for(double i=0; i<=1; i+=0.125) {
            if(y == i || y == 0.0625) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLadder(Location loc) {
        return loc.getBlock().getType() == Material.LADDER || loc.getBlock().getType() == Material.VINE;
    }

    public static Location highestLocation(Player player) {
        double blockY = player.getLocation().getBlockY();

        Location tplocation = null;
        for (int i = (int) blockY; i > 1; i--) {
            Location loc = new Location(player.getWorld(), player.getLocation().getX(), i, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

            if (loc.getBlock().getType().isSolid()) {
                tplocation = new Location(player.getWorld(), player.getLocation().getX(), i + 1.5, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                break;
            }
        }

        return tplocation;
    }

    public static Block[] getBlocksInHitbox(Player player, double minusy) {

        Block[] blocksaround = new Block[8];

        Block locXP = new Location(player.getWorld(), player.getLocation().getX() + 0.3, player.getLocation().getY() - minusy, player.getLocation().getZ()).getBlock();
        Block locXM = new Location(player.getWorld(), player.getLocation().getX() - 0.3, player.getLocation().getY() - minusy, player.getLocation().getZ()).getBlock();
        Block locZP = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - minusy, player.getLocation().getZ() + 0.3).getBlock();
        Block locZM = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - minusy, player.getLocation().getZ() - 0.3).getBlock();

        Block locXPZP = new Location(player.getWorld(), player.getLocation().getX() + 0.3, player.getLocation().getY() - minusy, player.getLocation().getZ() + 0.3).getBlock();
        Block locXPZM = new Location(player.getWorld(), player.getLocation().getX() + 0.3, player.getLocation().getY() - minusy, player.getLocation().getZ() - 0.3).getBlock();
        Block locXMZP = new Location(player.getWorld(), player.getLocation().getX() - 0.3, player.getLocation().getY() - minusy, player.getLocation().getZ() + 0.3).getBlock();
        Block locXMZM = new Location(player.getWorld(), player.getLocation().getX() - 0.3, player.getLocation().getY() - minusy, player.getLocation().getZ() - 0.3).getBlock();

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

    public static boolean inLiquid(Player p) {
        for(Block b : getBlocksInHitbox(p, 0)) {
            if (b.isLiquid()) {
                return true;
            }
        }
        for(Block b : getBlocksInHitbox(p, -1)) {
            if (b.isLiquid()) {
                return true;
            }
        }
        return false;
    }

    public static boolean inBlock(Player p, Material type) {
        for(Block bh : getBlocksInHitbox(p, 0)) {
            if (bh.getType() == type) {
                return true;
            }
        }
        for(Block bh : getBlocksInHitbox(p, -1)) {
            if (bh.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean inAir(Player p) {

        return !LocationUtils.isLadder(p.getLocation()) && !p.getLocation().getBlock().isLiquid() && !LocationUtils.getBlockAbove(p, 1).isLiquid()
                && !inLiquid(p) && !inBlock(p, Material.WEB);
    }

}
