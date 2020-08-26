package me.kenchh.events;

import me.kenchh.data.DataProfile;
import me.kenchh.data.DataProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        if(e.isCancelled()) return;

        Player p = (Player) e.getEntity();

        DataProfile dp = DataProfileManager.getDataProfile(p);
        dp.hurtticks = 5;
    }

}
