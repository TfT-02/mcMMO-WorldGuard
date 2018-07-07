package org.mcmmo.mcmmoworldguard.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;

import org.mcmmo.mcmmoworldguard.config.Config;
import org.mcmmo.mcmmoworldguard.util.RegionUtils;
import org.mcmmo.mcmmoworldguard.mcMMOWorldGuard;

public class ExperienceListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerXpGainEvent(McMMOPlayerXpGainEvent event) {
        Player player = event.getPlayer();
        float experienceGained = event.getRawXpGained();
        String skillName = event.getSkill().toString();
        String xpGainReason = event.getXpGainReason().toString();
        String region = RegionUtils.getRegion(player.getLocation());

        if (region == null || !isAffectedSkill(skillName) || !isAffectedReason(xpGainReason)) {
            return;
        }

        mcMMOWorldGuard.p.debug("experienceGained pre modifiers = " + experienceGained);

        double globalMod = Config.getInstance().getExperienceModifierGlobal();
        experienceGained *= globalMod;

        mcMMOWorldGuard.p.debug("experienceGained globally modified by " + globalMod + " = " + experienceGained);

        double regionMod = Config.getInstance().getExperienceModifierTown(region);
        experienceGained *= regionMod;

        mcMMOWorldGuard.p.debug("experienceGained region " + region + " modified by " + regionMod + " = " + experienceGained);

        event.setRawXpGained(experienceGained);

    }

    private boolean isAffectedSkill(String skillName) {
        return Config.getInstance().getAffectedSkills().contains(skillName);
    }

    private boolean isAffectedReason(String xpGainReason) {
        return Config.getInstance().getAffectedXpGainReasons().contains(xpGainReason);
    }
}
