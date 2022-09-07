package me.thesnipe12.commands;

import me.thesnipe12.NewbieConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

import static me.thesnipe12.Utils.isNumeric;

public class Sclnewbie implements CommandExecutor {
    private final Plugin plugin;

    public Sclnewbie(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sclnewbie")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWrong " +
                        "usage of command! Use /sclnewbie <get/remove/set/toggle> [player] [number]!"));
                return true;
            }
            if ((args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("toggle"))) {
                if (!sender.hasPermission("simplecl.newbie")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou do not have permission to use this command!"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (args.length < 3) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWrong" +
                                " usage of command! Use /sclnewbie <get/remove/set/toggle> [player] [number]!"));
                        return true;
                    }
                    if (inCache(args[1]) == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cPlayer not in cache!"));
                        return true;
                    }
                    if (!isNumeric(args[2])) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWrong" +
                                " usage of command! Use /sclnewbie <get/remove/set/toggle> [player] [number]!"));
                        return true;
                    }
                    NewbieConfig.getNewbieConfig().set("players." + inCache(args[1]), Integer.parseInt(args[2]));
                    NewbieConfig.saveNewbieConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aCommand successful!"));
                } else {
                    plugin.getConfig().set("newbieProtection.use", !plugin.getConfig()
                            .getBoolean("newbieProtection.use"));
                    plugin.saveConfig();
                    String state;
                    if (plugin.getConfig().getBoolean("newbieProtection.use")) {
                        state = "On";
                    } else {
                        state = "Off";
                    }
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aNewbie protection is now: &6%state%!").replace("%state%", state));
                }
                return true;
            }
            if (!plugin.getConfig().getBoolean("newbieProtection.use")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cNewbie protection is disabled!"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can " +
                        "only use this command in-game!"));
                return true;
            }
            if (!(args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("remove"))) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWrong usage " +
                        "of command! Use /sclnewbie <get/remove/set/toggle> [player] [number]!"));
                return true;
            }
            int seconds;
            int maxSeconds = plugin.getConfig().getInt("newbieProtection.seconds");
            UUID uuid = ((Player) sender).getUniqueId();
            if (args.length == 2 && sender.hasPermission("simplecl.newbie") && inCache(args[1]) != null) {
                seconds = NewbieConfig.getNewbieConfig().getInt("players." + inCache(args[1]));
                uuid = inCache(args[1]);
            } else {
                seconds = NewbieConfig.getNewbieConfig().getInt("players." + ((Player) sender).getUniqueId());
            }
            if (args[0].equalsIgnoreCase("get")) {
                if (maxSeconds == seconds) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cNot a newbie! Max newbie time is %maxSeconds% seconds!").replace(
                            "%maxSeconds%",
                            String.valueOf(maxSeconds)));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aNewbie time " +
                            "is %seconds% seconds!").replace("%seconds%", String.valueOf(seconds)));
                }
            } else {
                if (maxSeconds == seconds) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cAlready not a newbie!"));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aNow not a newbie!"));
                    NewbieConfig.getNewbieConfig().set("players." + uuid, maxSeconds);
                    NewbieConfig.saveNewbieConfig();
                }
            }
            return true;
        }
        return true;
    }
    private UUID inCache(String name) {
        for (OfflinePlayer p : Bukkit.getServer().getOfflinePlayers()) {
            if (p.getName() == null) {
                continue;
            }
            if (p.getName().equalsIgnoreCase(name)) {
                return p.getUniqueId();
            }
        }
        return null;
    }
}
