package org.mcmmo.mcmmoworldguard.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.nossr50.events.hardcore.McMMOPlayerDeathPenaltyEvent;

import org.mcmmo.mcmmoworldguard.util.RegionUtils;

public class DeathListener implements Listener {

    /**
     * Check McMMOPlayerDeathPenaltyEvent.
     *
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDeathPenaltyEvent(McMMOPlayerDeathPenaltyEvent event) {
        if (!RegionUtils.getDeathConsequencesEnabled(event.getPlayer().getLocation())) {
            event.setCancelled(true);
        }
    }
}
