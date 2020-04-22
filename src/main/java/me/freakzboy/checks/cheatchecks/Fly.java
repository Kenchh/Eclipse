package me.freakzboy.checks.cheatchecks;

import mc.zan.moevents.events.PlayerJumpEvent;
import me.freakzboy.alerts.AlertManager;
import me.freakzboy.checks.CheatCategory;
import me.freakzboy.checks.Check;
import me.freakzboy.checks.fails.FailType;
import me.freakzboy.data.DataProfile;
import me.freakzboy.data.DataProfileManager;
import me.freakzboy.main.Eclipse;
import me.freakzboy.utils.ChatUtils;
import me.freakzboy.utils.ConnectionUtils;
import me.freakzboy.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class Fly extends Check {

    public Fly() {
        super("Fly", CheatCategory.MOVEMENT);
    }

    @Override
    public void fail(Player player, FailType type) {
        super.fail(player, type);

        double blockY = player.getLocation().getBlockY();

        Location tplocation = null;
        for(int i= (int) blockY; i>1; i--) {
            Location loc = new Location(player.getWorld(), player.getLocation().getX(), i, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

            if(loc.getBlock().getType().isSolid()) {
                tplocation = new Location(player.getWorld(), player.getLocation().getX(), i + 1.5, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()  );
                break;
            }
        }
        
        if(tplocation != null) {
            player.teleport(tplocation);
        }

    }

    @EventHandler
    public void onJump(PlayerJumpEvent e) {
        e.getPlayer().sendMessage("You jumped!");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());

        Player p = e.getPlayer();

        if(ConnectionUtils.getPing(p) >= 40) {
            dp.ping_maxairticks = dp.maxairticks * ConnectionUtils.getPing(p) / 40;
        } else {
            dp.ping_maxairticks = dp.maxairticks;
        }

        Location from = e.getFrom();
        Location to = e.getTo();

        double deltaY = to.getY() - from.getY();

        if(p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR) {
            if(p.getAllowFlight() == false) {

                if (isAir(LocationUtils.getBlockUnder(p, 1).getLocation())) {


                    /** A: Fails, if player is too long in the air and still has a positive change in y-coordinate */
                    if (deltaY >= 0.0 && dp.airticks >= dp.ping_maxairticks) {
                        fail(p, FailType.A);
                    }

                    /** B: Non complex code, just flags if movement is very rapid and unusual. */
                    if (deltaY >= 1.0) {
                        fail(p, FailType.B);
                    }

                    /** C: Custom Flight Detection -> Used for fake onGround Packets (Ground Spoofing) */
                    if(deltaY >= -0.5) {
                        if (blocksAroundAir(p)) {

                            dp.debug_fly_blocksaroundair = true;

                            if ((int) (deltaY / 100) >= ((int) (dp.deltaY / 100))) {
                                dp.customairticks++;
                                if (dp.customairticks >= dp.ping_maxairticks) {
                                    fail(p, FailType.C);
                                    dp.customairticks = 0;
                                }
                            } else {
                                dp.customairticks = 0;
                            }
                        } else {
                            reset(dp);
                        }
                    } else {
                        reset(dp);
                    }


                } else {
                    reset(dp);
                }
            }
        }

        if(debugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "onGround: " + p.isOnGround());
            //p.sendMessage(Eclipse.prefix + "blocksAroundAir: " + dp.debug_fly_blocksaroundair);
            p.sendMessage(Eclipse.prefix + "airTicks: " + dp.airticks);
            p.sendMessage(Eclipse.prefix + "customAirTicks: " + dp.customairticks);
            p.sendMessage(Eclipse.prefix + "ping_maxAirTicks: " + dp.ping_maxairticks);
            p.sendMessage(Eclipse.prefix + "deltaY: " + deltaY);
            //p.sendMessage(Eclipse.prefix + "allowFlight: " + p.getAllowFlight());
        }

        dp.deltaY = deltaY;
    }

    public void reset(DataProfile dp) {
        dp.customairticks = 0;
        dp.debug_fly_blocksaroundair = false;
    }

    public boolean isAir(Location loc) {
        if(loc.getBlock().getType().isSolid() == false && loc.getBlock().isLiquid() == false && loc.getBlock().getType() != Material.LADDER && loc.getBlock().getType() != Material.VINE) {
            return true;
        }
        return false;
    }

    public boolean blocksAroundAir(Player p) {

        for(Block b : LocationUtils.getBlocksAround(p, 1)) {
            if(isAir(b.getLocation()) == false) {
                return false;
            }
        }
        return true;

    }

}
