package me.xlucash.xlforest.commands;

import me.xlucash.xlforest.XLForest;
import me.xlucash.xlforest.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForestCommandExecutor implements CommandExecutor {
    private final XLForest plugin;
    private final ConfigManager configManager;

    public ForestCommandExecutor(XLForest plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("forest") && args.length > 0 && args[0].equalsIgnoreCase("reload") && sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("xlforest.reload")) {
                configManager.loadConfig();
                player.sendMessage("Plugin configuration reloaded.");
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
            return true;
        }

        return false;
    }
}