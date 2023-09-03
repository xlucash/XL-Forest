package me.xlucash.xlforest;

import me.xlucash.xlforest.commands.ForestCommandExecutor;
import me.xlucash.xlforest.config.ConfigManager;
import me.xlucash.xlforest.listeners.ForestListener;
import me.xlucash.xlforest.regen.BlockRegenManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class XLForest extends JavaPlugin {
    private ConfigManager configManager;
    private BlockRegenManager blockRegenManager;
    private ForestCommandExecutor commandExecutor;
    private ForestListener listener;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        blockRegenManager = new BlockRegenManager(this, configManager);
        commandExecutor = new ForestCommandExecutor(this, configManager);
        listener = new ForestListener(this, blockRegenManager, configManager);

        getServer().getPluginManager().registerEvents(listener, this);
        getCommand("forest").setExecutor(commandExecutor);
    }

    @Override
    public void onDisable() {
        blockRegenManager.restoreAllBlocks();
    }
}
