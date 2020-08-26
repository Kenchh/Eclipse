package me.kenchh.checks.cheatchecks.player;

import me.kenchh.checks.CheatCategory;
import me.kenchh.checks.Check;
import me.kenchh.checks.fails.FailType;
import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import me.kenchh.packet.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TooManyPackets extends Check implements PacketListener {

    public TooManyPackets() {
        super("TooManyPackets", CheatCategory.PLAYER);
    }

    @Override
    public void readPacket(Player p, Object packet) {
        /*  */

        DataProfile dp = DataProfileManager.getDataProfile(p);

        if(System.currentTimeMillis() - dp.timerLastMillis >= 1000) {
            dp.timerLastMillis = System.currentTimeMillis();
            dp.timerPackets = 0;
        }

        dp.timerPackets++;

        if(dp.timerPackets >= 100) {
            fail(p, FailType.A, "p: " + dp.timerPackets);
        }

    }

}
