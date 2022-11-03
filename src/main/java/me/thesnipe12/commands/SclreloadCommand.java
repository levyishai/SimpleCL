package me.thesnipe12.commands;

import me.thesnipe12.CustomConfig;
import me.thesnipe12.PluginConstants;
import me.thesnipe12.Utilities;
import me.thesnipe12.listeners.WorldGuardListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SclreloadCommand implements TabExecutor {
    private final Plugin plugin;
    private final HashMap<Player, Integer> combatTimer;

    public SclreloadCommand(Plugin plugin, HashMap<Player, Integer> combatTimer) {
        this.plugin = plugin;
        this.combatTimer = combatTimer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("sclreload")) return false;

        if (!sender.hasPermission(CommandsConstants.RELOAD_PERM)) {
            sender.sendMessage(CommandsConstants.NO_PERMISSION);
            return true;
        }

        sender.sendMessage(CommandsConstants.RELOADING_CONFIG);
        reloadAllConfigs();
        reloadWorldGuard(sender);

        sender.sendMessage(CommandsConstants.RELOAD_SUCCESSFUL);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("sclreload")) return null;

        return new ArrayList<>();
    }

    private void reloadWorldGuard(CommandSender sender) {
        if (plugin.getConfig().getBoolean("allowBorderHopping")) return;

        if (Bukkit.getPluginManager().getPlugin("WorldGuardEvents") == null) {
            sender.sendMessage(PluginConstants.BORDER_DISABLE_FAIL);
            return;
        }

        sender.sendMessage(PluginConstants.BORDER_DISABLE_SUCCESS);
        if (!Utilities.isListenerRegistered(plugin, WorldGuardListener.class)) {
            Bukkit.getPluginManager().registerEvents(new WorldGuardListener(plugin, combatTimer), plugin);
        }
    }

    private void reloadAllConfigs() {
        plugin.reloadConfig();
        plugin.saveConfig();

        for (Utilities.ConfigType configType : Utilities.ConfigType.values()) {
            CustomConfig customConfig = Utilities.getCustomConfig(configType);

            customConfig.reloadConfig();
            customConfig.saveConfig();
        }
    }

}
