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
        String secondaryAbility = event.getSecondaryAbility().toString().toUpperCase();
        String region = RegionUtils.getRegion(player.getLocation());

        if (region == null || canUseAbility(player, region, secondaryAbility)) {
            mcMMOWorldGuard.p.debug(player.getName() + " can use " + secondaryAbility + " in region " + region);
            return;
        }

        mcMMOWorldGuard.p.debug(player.getName() + " cannot use " + secondaryAbility + " in region " + region);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onMcMMOPlayerAbilityActivate(McMMOPlayerAbilityActivateEvent event) {
        Player player = event.getPlayer();
        String ability = event.getAbility().toString().toUpperCase();
        String region = RegionUtils.getRegion(player.getLocation());

        if (region == null || canUseAbility(player, region, ability)) {
            mcMMOWorldGuard.p.debug(player.getName() + " can use " + ability + " in region " + region);
            return;
        }

        mcMMOWorldGuard.p.debug(player.getName() + " cannot use " + ability + " in region " + region);
        event.setCancelled(true);
    }

    public boolean canUseAbility(Player player, String region, String ability) {
        mcMMOWorldGuard.p.debug("Checking if " + player.getName() + " can use " + ability + " in region " + region);
        boolean whitelist = Config.getInstance().getAbilitiesUseAsWhitelist();
        mcMMOWorldGuard.p.debug("getAbilitiesUseAsWhiteList = " + whitelist);

        if (Config.getInstance().getAbilitiesRegion(region).contains(ability)) {
            mcMMOWorldGuard.p.debug("Config for region " + region + " contains ability " + ability);
            return whitelist;
        }

        mcMMOWorldGuard.p.debug("Config for region " + region + " does not contain ability " + ability);
        return !whitelist;
    }
}
