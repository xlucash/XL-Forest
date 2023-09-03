package me.xlucash.xlforest.listeners;

import me.xlucash.xlforest.XLForest;
import me.xlucash.xlforest.config.ConfigManager;
import me.xlucash.xlforest.regen.BlockRegenManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ForestListener implements Listener {
    private final XLForest plugin;
    private final BlockRegenManager blockRegenManager;
    private final ConfigManager configManager;

    public ForestListener(XLForest plugin, BlockRegenManager blockRegenManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.blockRegenManager = blockRegenManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (!block.getWorld().getName().equals(configManager.getWorldName())) {
            return;
        }

        if (!blockRegenManager.isLog(block.getType())) {
            event.setCancelled(true);
            return;
        }

        event.setDropItems(false);

        Player player = event.getPlayer();
        Material logType = block.getType();

        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(new ItemStack(logType));

        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
        }

        blockRegenManager.addBlockToRegen(block);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getBlock().getWorld().getName().equals(configManager.getWorldName())) {
            return;
        }
        event.setCancelled(true);
    }
}