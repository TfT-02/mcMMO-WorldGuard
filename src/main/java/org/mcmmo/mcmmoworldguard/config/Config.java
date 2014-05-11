package org.mcmmo.mcmmoworldguard.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Config extends ConfigLoader {
    private static Config instance;

    private Config() {
        super("config.yml");
        validate();
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }

        return instance;
    }

    @Override
    protected boolean validateKeys() {
        return true;
    }

    @Override
    protected void loadKeys() {}

    /* GENERAL */
    public boolean getStatsTrackingEnabled() { return config.getBoolean("General.Stats_Tracking", true); }
    public boolean getUpdateCheckEnabled() { return config.getBoolean("General.Update_Check", true); }
    public boolean getPreferBeta() { return config.getBoolean("General.Prefer_Beta", false); }

    /* WORLDGUARD */
    public boolean getWGUseAsWhitelist() { return config.getBoolean("Abilities.Use_As_Whitelist", false); }

    public Set<String> getWGRegionList() {
        return config.getConfigurationSection("Abilities.Regions").getKeys(false);
    }

    public List<String> getAbilitiesRegion(String region) {
        return config.getStringList("Abilities.Regions." + region);
    }

    public double getExperienceModifierGlobal() { return config.getDouble("Experience.Global_Modifier", 1.0); }
    public double getExperienceModifierTown(String townName) { return config.getDouble("Experience.Towns." + townName, 1.0); }

    public List<String> getAffectedSkills() {
        List<String> list = new ArrayList<String>();
        for (String skillName : config.getStringList("Experience.Affected_Skills")) {
            list.add(skillName.toUpperCase());
        }

        return list;
    }

    public List<String> getAffectedXpGainReasons() {
        List<String> list = new ArrayList<String>();
        for (String xpGainReason : config.getStringList("Experience.Affected_XP_Gains")) {
            list.add(xpGainReason.toUpperCase());
        }

        return list;
    }
}
