package me.xlucash.xlforest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public final class XLForest extends JavaPlugin implements Listener, CommandExecutor {
    private String worldName;
    private int regenTime;
    private final Map<Location, Material> blocksToRegen = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("xlforest").setExecutor(this);
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        worldName = config.getString("world-name");
        regenTime = config.getInt("regen-time");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (!block.getWorld().getName().equals(worldName)) {
            return;
        }

        if (!isLog(block.getType())) {
            event.setCancelled(true);
            return;
        }

        event.setDropItems(false);

        Material logType = block.getType();
        Location location = block.getLocation();
        Player player = event.getPlayer();

        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(new ItemStack(logType));

        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
        }

        blocksToRegen.put(location, logType);

        new BukkitRunnable() {
            @Override
            public void run() {
                blocksToRegen.remove(location);
                block.setType(logType);
            }
        }.runTaskLater(this, regenTime);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getBlock().getWorld().getName().equals(worldName)) {
            return;
        }
        event.setCancelled(true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("forest") && args.length > 0 && args[0].equalsIgnoreCase("reload") && sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("xlforest.reload")) {
                reloadConfig();
                loadConfig();
                player.sendMessage("Plugin configuration reloaded.");
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
            return true;
        }

        return false;
    }

    @Override
    public void onDisable() {
        for (Map.Entry<Location, Material> entry : blocksToRegen.entrySet()) {
            Location location = entry.getKey();
            Material logType = entry.getValue();
            location.getBlock().setType(logType);
        }
    }

    private boolean isLog(Material material) {
        return material == Material.OAK_LOG || material == Material.BIRCH_LOG || material == Material.SPRUCE_LOG
                || material == Material.JUNGLE_LOG || material == Material.ACACIA_LOG || material == Material.DARK_OAK_LOG;
    }
}
