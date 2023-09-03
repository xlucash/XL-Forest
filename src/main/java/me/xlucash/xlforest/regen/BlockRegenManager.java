package me.xlucash.xlforest.regen;

import me.xlucash.xlforest.XLForest;
import me.xlucash.xlforest.config.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BlockRegenManager {
    private final XLForest plugin;
    private final ConfigManager configManager;
    private final Map<Location, Material> blocksToRegen = new HashMap<>();

    public BlockRegenManager(XLForest plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void addBlockToRegen(Block block) {
        Material logType = block.getType();
        Location location = block.getLocation();
        blocksToRegen.put(location, logType);

        new BukkitRunnable() {
            @Override
            public void run() {
                blocksToRegen.remove(location);
                block.setType(logType);
            }
        }.runTaskLater(plugin, configManager.getRegenTime());
    }

    public void restoreAllBlocks() {
        for (Map.Entry<Location, Material> entry : blocksToRegen.entrySet()) {
            Location location = entry.getKey();
            Material logType = entry.getValue();
            location.getBlock().setType(logType);
        }
    }

    public boolean isLog(Material material) {
        return material == Material.OAK_LOG || material == Material.BIRCH_LOG
                || material == Material.SPRUCE_LOG || material == Material.MANGROVE_LOG
                || material == Material.JUNGLE_LOG || material == Material.ACACIA_LOG
                || material == Material.DARK_OAK_LOG;
    }
}