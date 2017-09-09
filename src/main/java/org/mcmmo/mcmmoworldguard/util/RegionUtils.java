package org.mcmmo.mcmmoworldguard.util;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;

import org.mcmmo.mcmmoworldguard.config.Config;
import org.mcmmo.mcmmoworldguard.mcMMOWorldGuard;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionUtils {

    public static boolean getDeathConsequencesEnabled(Location location) {
        boolean isWhitelist = Config.getInstance().getDeathPenaltyUseAsWhitelist();

        if (isListedRegion(getRegion(location), Config.getInstance().getDeathPenaltyRegionList())) {
            return isWhitelist;
        }

        return !isWhitelist;
    }

    private static boolean isListedRegion(String region, List<String> list) {
        for (String name : list) {
            if (region.equalsIgnoreCase("[" + name + "]")) {
                return true;
            }
        }
        return false;
    }

    public static String getRegion(Location location) {
        RegionManager regionManager = mcMMOWorldGuard.p.getWorldGuard().getRegionManager(location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(location);
        LinkedList<String> parentNames = new LinkedList<String>();
        LinkedList<String> regions = new LinkedList<String>();

        for (ProtectedRegion region : set) {
            String id = region.getId();
            regions.add(id);
            ProtectedRegion parent = region.getParent();
            while (parent != null) {
                parentNames.add(parent.getId());
                parent = parent.getParent();
            }
        }

        for (String name : parentNames) {
            regions.remove(name);
        }

        String regionName = regions.toString();

        if (regionName.startsWith("[")) {
            regionName = regionName.substring(1, regionName.length());
        }

        if (regionName.endsWith("]")) {
            regionName = regionName.substring(0, regionName.length() - 1);
        }

        return regionName;
    }
}
