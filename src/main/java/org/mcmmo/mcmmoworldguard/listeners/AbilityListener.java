package org.mcmmo.mcmmoworldguard.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.secondaryabilities.SecondaryAbilityEvent;

import org.mcmmo.mcmmoworldguard.config.Config;
import org.mcmmo.mcmmoworldguard.mcMMOWorldGuard;
import org.mcmmo.mcmmoworldguard.util.RegionUtils;

public class AbilityListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onSecondaryAbility(SecondaryAbilityEvent event) {
        Player player = event.getPlayer();
        String secondaryAbility = event.getSecondaryAbility().toString();
        String region = RegionUtils.getRegion(player.getLocation());

        if (region == null || canUseAbility(region, secondaryAbility)) {
            return;
        }

        mcMMOWorldGuard.p.debug(player.getName() + " cannot use " + secondaryAbility + " in region " + region);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onMcMMOPlayerAbilityActivate(McMMOPlayerAbilityActivateEvent event) {
        Player player = event.getPlayer();
        String ability = event.getAbility().toString();
        String region = RegionUtils.getRegion(player.getLocation());

        if (region == null || canUseAbility(region, ability)) {
            return;
        }

        mcMMOWorldGuard.p.debug(player.getName() + " cannot use " + ability + " in region " + region);
        event.setCancelled(true);
    }

    public boolean canUseAbility(String region, String ability) {
        mcMMOWorldGuard.p.debug("Checking if a player can use " + ability + " in region " + region);
        boolean whitelist = Config.getInstance().getAbilitiesUseAsWhitelist();

        if (Config.getInstance().getAbilitiesRegion(region).contains(ability)) {
            return whitelist;
        }

        return !whitelist;
    }
}
