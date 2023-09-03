package me.xlucash.xlforest.config;

import me.xlucash.xlforest.XLForest;

public class ConfigManager {
    private final XLForest plugin;
    private String worldName;
    private int regenTime;

    public ConfigManager(XLForest plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.reloadConfig();
        worldName = plugin.getConfig().getString("world-name");
        regenTime = plugin.getConfig().getInt("regen-time");
    }

    public String getWorldName() {
        return worldName;
    }

    public int getRegenTime() {
        return regenTime;
    }
}
