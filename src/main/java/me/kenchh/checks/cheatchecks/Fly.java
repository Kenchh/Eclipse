package me.kenchh.checks.cheatchecks;

import me.kenchh.checks.CheatCategory;
import me.kenchh.checks.Check;
import me.kenchh.checks.CheckManager;
import me.kenchh.checks.CheckMode;
import me.kenchh.checks.fails.FailType;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.main.Eclipse;
import me.kenchh.utils.ConnectionUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Fly extends Check {

    public Fly() {
        super("Fly", CheatCategory.MOVEMENT);
    }

    @Override
    public void fail(Player player, FailType type) {
        super.fail(player, type);

        DataProfile dp = DataProfileManager.getDataProfile(player);

        if(type == FailType.C) {
            double blockY = player.getLocation().getBlockY();

            Location tplocation = null;
            for (int i = (int) blockY; i > 1; i--) {
                Location loc = new Location(player.getWorld(), player.getLocation().getX(), i, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

                if (loc.getBlock().getType().isSolid()) {
                    tplocation = new Location(player.getWorld(), player.getLocation().getX(), i + 1.5, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                    break;
                }
            }

            if (tplocation != null) {
                player.teleport(tplocation);
            }

        } else {

            if(dp.lastLocationOnGround != null) {
                if (dp.lastLocationOnGround.getY() >= 0) {
                    player.teleport(dp.lastLocationOnGround);
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());

        for(CheckMode cm : CheckManager.checkModes) {
            if(dp.currentCheckMode == cm) {
                cm.fly(this, e);
            }
        }

        if(dp.currentCheckMode != null && dp.currentCheckMode.isIgnored(this) == false) {
            return;
        }

        Player p = e.getPlayer();

        Location from = e.getFrom();
        Location to = e.getTo();

        double deltaY = to.getY() - from.getY();
        double deltadeltaY = deltaY - dp.lastDeltaY;

        if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if(p.getAllowFlight()) {
            return;
        }

        double jumpboost = 0;
        for(PotionEffect pe : p.getActivePotionEffects()) {
            if(pe.getType().getName() == PotionEffectType.JUMP.getName()) {
                jumpboost = pe.getAmplifier() + 1;
            }
        }

        int A_maxairticks = 25;
        if (ConnectionUtils.getPing(p) >= 40) {
            if(ConnectionUtils.getPing(p) < 250) {
                A_maxairticks = 25 * ConnectionUtils.getPing(p) / 40;
            } else {
                A_maxairticks = 25 * 250 / 40;
            }
        }

        if(dp.onGround() == false) {

            /** B: Non complex code, just flags if movement is very rapid and unusual. */
            if (deltaY >= 0.5 + jumpboost/10) {
                fail(p, FailType.B);
                if(failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY);
            }

            /** BB: Fails, if players deltaY is above a specific value and has barely changed compared to last tick. */
            if (deltaY >= 0.25 && dp.airticks >= 10 && (deltadeltaY >= -0.1 && deltadeltaY <= 0.1)) {
                fail(p, FailType.BB);
                if(failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY + " | " + "airticks: " + dp.airticks);
            }

            /** A: Fails, if player is too long in the air and still has a positive change in y-coordinate */
            if (deltaY >= 0.0 && dp.airticks >= A_maxairticks) {
                fail(p, FailType.A);
                if(failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY + " | " + "airticks: " + dp.airticks);
            }

            /** AA: Fails, when X or Z changes while deltaY is 0. **/
            int AA_maxairticks = 15;

            if (ConnectionUtils.getPing(p) >= 40) {
                if(ConnectionUtils.getPing(p) < 250) {
                    AA_maxairticks = 15 * ConnectionUtils.getPing(p) / 40;
                } else {
                    AA_maxairticks = 15 * 250 / 35;
                }
            }

            if(deltaY == 0 && dp.airticks > AA_maxairticks) {
                if(to.getX() != from.getX() || to.getZ() != from.getZ()) {
                    if(failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY + " | " + "airticks: " + dp.airticks);
                    fail(p, FailType.AA);
                }
            }

            /*
            C: Custom Flight Detection -> Used for fake onGround Packets (Ground Spoofing)
            if(deltaY >= -0.5) {
                if (LocationUtils.blocksAroundAir(p)) {

                    dp.debug_fly_blocksaroundair = true;

                    if ((int) (deltaY / 100) >= ((int) (dp.deltaY / 100))) {
                        dp.customairticks++;
                        if (dp.customairticks >= A_C_maxairticks) {
                            fail(p, FailType.C);
                            if(failDebugAllowed(p)) p.sendMessage(ChatColor.YELLOW + "deltaY: " + deltaY + " | " + "customairticks: " + dp.customairticks);
                            dp.customairticks = 0;
                        }
                    }
                }
            }
            */

        } else {
            dp.lastLocationOnGround = p.getLocation();
            resetAirTicks(dp);
        }

        if(checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "onGround: " + p.isOnGround());
            //p.sendMessage(Eclipse.prefix + "blocksAroundAir: " + dp.debug_fly_blocksaroundair);
            p.sendMessage(Eclipse.prefix + "airTicks: " + dp.airticks);
            p.sendMessage(Eclipse.prefix + "customAirTicks: " + dp.customairticks);
            p.sendMessage(Eclipse.prefix + "deltaY: " + deltaY);
            p.sendMessage(Eclipse.prefix + "customOnGround: " + dp.onGround());
        }

    }

    public void resetAirTicks(DataProfile dp) {
        dp.airticks = 0;
        dp.customairticks = 0;
    }

}
