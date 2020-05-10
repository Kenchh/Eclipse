package me.kenchh.checks.checkmodes;

import me.kenchh.checks.Check;
import me.kenchh.checks.CheckManager;
import me.kenchh.checks.CheckMode;
import me.kenchh.checks.fails.FailType;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.main.Eclipse;
import me.kenchh.utils.ConnectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Leap extends CheckMode {

    public Leap() {
        super("Leap");
        setIgnore(CheckManager.getCheck("Fly"), false);
        setIgnore(CheckManager.getCheck("GroundSpoof"), false);
    }

    @Override
    public void fly(Check check, PlayerMoveEvent e) {

        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());

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
            if (deltaY >= 0.9 + jumpboost/10) {
                if(check.failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY);
                check.fail(p, FailType.B);
            }

            /** BB: Fails, if players deltaY is above a specific value and has barely changed compared to last tick. */
            if (deltaY >= 0.25 && dp.airticks >= 10 && (deltadeltaY >= -0.1 && deltadeltaY <= 0.1)) {
                if(check.failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY + " | " + "airticks: " + dp.airticks);
                check.fail(p, FailType.BB); }

            /** A: Fails, if player is too long in the air and still has a positive change in y-coordinate */
            if (deltaY >= 0.0 && dp.airticks >= A_maxairticks) {
                if(check.failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY + " | " + "airticks: " + dp.airticks);
                check.fail(p, FailType.A);
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
                    if(check.failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY + " | " + "airticks: " + dp.airticks);
                    check.fail(p, FailType.AA);
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
            dp.airticks = 0;
            dp.customairticks = 0;
        }

        if(check.checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "onGround: " + p.isOnGround());
            //p.sendMessage(Eclipse.prefix + "blocksAroundAir: " + dp.debug_fly_blocksaroundair);
            p.sendMessage(Eclipse.prefix + "airTicks: " + dp.airticks);
            p.sendMessage(Eclipse.prefix + "customAirTicks: " + dp.customairticks);
            p.sendMessage(Eclipse.prefix + "deltaY: " + deltaY);
            p.sendMessage(Eclipse.prefix + "customOnGround: " + dp.onGround());
        }

    }

    @Override
    public void groundspoof(Check check, PlayerMoveEvent e) {

    }

    @Override
    public void motion(Check check, PlayerMoveEvent e) {
        DataProfile dp = DataProfileManager.getDataProfile(e.getPlayer());

        Player p = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();

        if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if(p.getAllowFlight()) {
            return;
        }

        double deltaY = to.getY() - from.getY();

        double deltadeltaY = deltaY - dp.lastDeltaY;

        /** A: Fails, if difference between current and last deltaY is too high.*/
        if(deltadeltaY <= -0.8) {
            if(check.failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY);
            check.fail(p, FailType.A);
        }

        /** B: Checks if this wrong deltaY occurs at a wrong time when the player is in the air **/
        if (deltaY > 0.7D && dp.airticks >= 5) {
            if(check.failDebugAllowed(p)) Bukkit.broadcastMessage(ChatColor.YELLOW + "deltaY: " + deltaY + " | " + "airticks: " + dp.airticks);
            check.fail(p, FailType.B);
        }

        if(check.checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "deltaY: " + deltaY);
            p.sendMessage(Eclipse.prefix + "airticks: " + dp.airticks);
            p.sendMessage(Eclipse.prefix + "deltadeltaY: " + deltadeltaY);
        }
    }

    @Override
    public void fabricatedmovement(Check check, PlayerMoveEvent e) {

    }
}
