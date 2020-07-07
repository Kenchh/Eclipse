package me.kenchh.checks.cheatchecks.movement;

import me.kenchh.checks.CheatCategory;
import me.kenchh.checks.Check;
import me.kenchh.checks.fails.FailType;
import me.kenchh.checks.interfaces.Movement;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.main.Eclipse;
import me.kenchh.packet.PacketListener;
import me.kenchh.utils.LocationUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GroundSpoof extends Check implements Movement {
    public GroundSpoof() {
        super("Ground-Spoof", CheatCategory.MOVEMENT);
    }

    @Override
    public void fail(Player player, FailType type, String debugmsg) {
        super.fail(player, type, debugmsg);

        player.teleport(LocationUtils.highestLocation(player));

    }

    public HashMap<UUID, Integer> listenForWrongAlgorithm = new HashMap<>();

    @Override
    public void move(PlayerMoveEvent e, DataProfile dp, Player p, double deltaY, double deltaH, double deltadeltaY, double deltadeltaH) {
        if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR || p.getAllowFlight()) {
            return;
        }

        /** A: When custom on Ground check and isOnGround doesnt match for a period of time */
        if(p.isOnGround() != dp.onGround() && LocationUtils.blocksAroundAir(p) && dp.cAirticks > 7 && LocationUtils.couldBeOnGround(p) == false) {
            fail(p, FailType.A, "oG: " + p.isOnGround() + " coG: " + dp.onGround());
        }

        /** B: When isOnGround has been changed 3 times in a row every tick */

        if(dp.lastVanillaOnGround != p.isOnGround() && LocationUtils.getBlockUnder(p, 1).getType() != Material.SLIME_BLOCK) {
            if(listenForWrongAlgorithm.containsKey(p.getUniqueId()) == false) {
                listenForWrongAlgorithm.put(p.getUniqueId(), 0);
            } else {
                if(listenForWrongAlgorithm.get(p.getUniqueId()) < 5) {
                    listenForWrongAlgorithm.replace(p.getUniqueId(), listenForWrongAlgorithm.get(p.getUniqueId()) + 1);
                } else {
                    listenForWrongAlgorithm.remove(p.getUniqueId());
                    fail(p, FailType.B, "");
                }
            }
        } else {
            if(listenForWrongAlgorithm.containsKey(p.getUniqueId())) {
                listenForWrongAlgorithm.remove(p.getUniqueId());
            }
        }

        /** When the player seems to be sending onGround packets but still has deltaY -> almost impossible to reproduce
         * Exceptions: Steps/Stairs, Teleportations & Ladders/Vines
         * */

        if(p.isOnGround() && deltadeltaY != 0 && p.getLocation().getBlock().getType() != Material.LADDER && p.getLocation().getBlock().getType() != Material.VINE) {
            if(dp.groundspoofticks >= 15) {
                fail(p, FailType.C, "gsT: " + dp.groundspoofticks);
                dp.groundspoofticks = 0;
            } else {
                dp.groundspoofticks++;
            }
        } else {
            dp.groundspoofticks = 0;
        }

        if(checkDebugAllowed(p)) {
            p.sendMessage(Eclipse.prefix + "oG: " + p.isOnGround());
            p.sendMessage(Eclipse.prefix + "wA: " + listenForWrongAlgorithm.get(p.getUniqueId()));
        }
    }

}
