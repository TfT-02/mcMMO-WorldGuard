package org.mcmmo.mcmmoworldguard;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.mcmmo.mcmmoworldguard.config.Config;
import org.mcmmo.mcmmoworldguard.listeners.AbilityListener;
import org.mcmmo.mcmmoworldguard.listeners.ExperienceListener;
import org.mcmmo.mcmmoworldguard.util.LogFilter;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class mcMMOWorldGuard extends JavaPlugin {
    public static mcMMOWorldGuard p;

    private boolean mcMMOEnabled = false;
    private boolean worldGuardEnabled = false;

    /**
     * Things to be run when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        try {
            p = this;
            getLogger().setFilter(new LogFilter(this));

            setupMcMMO();
            setupWorldGuard();

            if (!isMcMMOEnabled()) {
                this.getLogger().warning("mcMMO-WorldGuard requires mcMMO to run, please download mcMMO. http://dev.bukkit.org/server-mods/mcmmo/");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            if (!isWorldGuardEnabled()) {
                this.getLogger().warning("mcMMO-WorldGuard requires WorldGuard to run, please download WorldGuard. http://dev.bukkit.org/bukkit-plugins/worldguard/");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            Config.getInstance();

            registerEvents();
        }
        catch (Throwable t) {
            getLogger().severe("There was an error while enabling mcMMO-WorldGuard!");

            if (!(t instanceof ExceptionInInitializerError)) {
                t.printStackTrace();
            }
            else {
                getLogger().info("Please do not replace the mcMMO-WorldGuard jar while the server is running.");
            }

            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void setupMcMMO() {
        PluginManager pluginManager = getServer().getPluginManager();

        if (pluginManager.isPluginEnabled("mcMMO")) {
            // Check mcMMO version v1.5.01-b3605, which has cancellable SecondaryAbilityEvent
            String[] versionSplit = pluginManager.getPlugin("mcMMO").getDescription().getVersion().replace("SNAPSHOT-", "").split("-b");
            int version = Integer.parseInt(versionSplit[0].replaceAll("[.]", ""));
            debug("Detected mcMMO version " + versionSplit[0] + " build " + versionSplit[1]);

            if (version > 1501 || (!versionSplit[1].equals("${BUILD_NUMBER}") && Integer.parseInt(versionSplit[1]) >= 3605)) {
                mcMMOEnabled = true;
                debug("Hooked into mcMMO successfully!");
            }
            else {
                this.getLogger().warning("mcMMO-WorldGuard does not support this version of mcMMO!");
            }
        }
    }

    private void setupWorldGuard() {
        if (getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuardEnabled = true;
        }
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    /**
     * Things to be run when the plugin is disabled.
     */
    @Override
    public void onDisable() {
    }

    public void debug(String message) {
        getLogger().info("[Debug] " + message);
    }

    public boolean isMcMMOEnabled() {
        return mcMMOEnabled;
    }

    public boolean isWorldGuardEnabled() {
        return worldGuardEnabled;
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Register events
        pluginManager.registerEvents(new ExperienceListener(), this);
        pluginManager.registerEvents(new AbilityListener(), this);
    }
}
