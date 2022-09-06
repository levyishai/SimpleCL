package me.thesnipe12.commands;

import me.thesnipe12.Constants;
import me.thesnipe12.NewbieConfig;
import me.thesnipe12.listeners.WorldGuardListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class Sclreload implements CommandExecutor {

    Plugin plugin;

    public Sclreload(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sclreload")) {
            if (sender.hasPermission("simplecl.reload")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReloading config..."));
                plugin.reloadConfig();
                plugin.saveConfig();
                NewbieConfig.reloadNewbieConfig();
                if (!plugin.getConfig().getBoolean("borderHopping")) {
                    if (Bukkit.getPluginManager().getPlugin("WGRegionEvents") != null) {
                        sender.sendMessage(Constants.BORDER_DISABLED_SUCCESS);
                        Bukkit.getPluginManager().registerEvents(new WorldGuardListener(plugin), plugin);
                    } else {
                        sender.sendMessage(Constants.BORDER_DISABLED_FAIL);
                    }
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aConfig reloaded!"));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou" +
                        " do not have permission to use this command!"));
            }
            return true;
        }
        return true;
    }
}
