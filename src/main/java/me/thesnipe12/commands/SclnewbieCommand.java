package me.thesnipe12.commands;

import me.thesnipe12.CustomConfig;
import me.thesnipe12.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.thesnipe12.Utilities.*;

public class SclnewbieCommand implements TabExecutor {
    private final Plugin plugin;

    public SclnewbieCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender,@NonNull Command command,@NonNull String label,@NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("sclnewbie")) return false;

        if (args.length == 0) {
            sender.sendMessage(CommandsConstants.NEWBIE_WRONG_USAGE);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "set" -> optionSet(sender, args);
            case "toggle" -> optionToggle(sender);
            case "remove" -> optionRemove(sender, args);
            case "get" -> optionGet(sender, args);
            default -> sender.sendMessage(CommandsConstants.NEWBIE_WRONG_USAGE);
        }

        return true;

    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender,@NonNull Command command,@NonNull String label,@NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("sclnewbie")) return null;

        final List<String> availableOptions = getAvailableOptions(sender, args);
        if (availableOptions.isEmpty()) return availableOptions;

        return getCommandOptions(availableOptions, args);
    }

    private List<String> getAvailableOptions(CommandSender sender, String[] args) {
        switch(args.length) {
            case 1:
                if (sender.hasPermission(CommandsConstants.NEWBIE_PERM)) {
                    return CommandsConstants.NEWBIE_PERMS_OPTIONS;
                }else{
                    return CommandsConstants.NEWBIE_OPTIONS;
                }
            case 2:
                if(sender.hasPermission(CommandsConstants.NEWBIE_PERM) && CommandsConstants.NEWBIE_PERMS_OPTIONS.contains(args[0].toLowerCase())
                        && !args[0].equalsIgnoreCase("toggle")){
                    return Utilities.getOfflinePlayersAsStringList();
                }else {
                    return new ArrayList<>();
                }
            default:
                return new ArrayList<>();
        }
    }

    private void optionGet(CommandSender sender, String[] args) {
        if (!plugin.getConfig().getBoolean("newbieProtection.use")) {
            sender.sendMessage(CommandsConstants.NEWBIE_DISABLED);
            return;
        }

        final int seconds;
        final int maxSeconds = plugin.getConfig().getInt("newbieProtection.seconds");
        final UUID uuid;
        CustomConfig newbieConfig = Utilities.getCustomConfig(Utilities.ConfigType.NEWBIE_CONFIG);

        if (args.length >= 2 && sender.hasPermission(CommandsConstants.NEWBIE_PERM) && getUUIDFromName(args[1]) != null) {
            uuid = getUUIDFromName(args[1]);
        } else if(sender instanceof Player){
            uuid = ((Player) sender).getUniqueId();
        } else {
            sender.sendMessage(CommandsConstants.ONLY_AVAILABLE_FOR_PLAYERS);
            return;
        }

        seconds = newbieConfig.getConfig().getInt("players."+uuid);

        if (maxSeconds == seconds) {
            sender.sendMessage(CommandsConstants.NOT_NEWBIE_WITH_MAX_TIME.replace(
                    "%maxSeconds%", String.valueOf(maxSeconds)));
        } else {
            sender.sendMessage(CommandsConstants.MAX_NEWBIE_TIME.replace("%seconds%", String.valueOf(seconds)));
        }

    }

    private void optionRemove(CommandSender sender, String[] args) {
        if (!plugin.getConfig().getBoolean("newbieProtection.use")) {
            sender.sendMessage(CommandsConstants.NEWBIE_DISABLED);
            return;
        }

        final int seconds;
        final int maxSeconds = plugin.getConfig().getInt("newbieProtection.seconds");
        final UUID uuid;
        CustomConfig newbieConfig = Utilities.getCustomConfig(Utilities.ConfigType.NEWBIE_CONFIG);

        if (args.length >= 2 && sender.hasPermission(CommandsConstants.NEWBIE_PERM) && getUUIDFromName(args[1]) != null) {
            uuid = getUUIDFromName(args[1]);
        } else if(sender instanceof Player){
            uuid = ((Player) sender).getUniqueId();
        } else {
            sender.sendMessage(CommandsConstants.ONLY_AVAILABLE_FOR_PLAYERS);
            return;
        }

        seconds = newbieConfig.getConfig().getInt("players."+uuid);

        if (maxSeconds == seconds) {
            sender.sendMessage(CommandsConstants.NOT_NEWBIE);
        } else {
            newbieConfig.getConfig().set("players." + uuid, maxSeconds);
            newbieConfig.saveConfig();

            sender.sendMessage(CommandsConstants.NOW_NOT_NEWBIE);
        }
    }

    private void optionToggle(CommandSender sender) {
        if(!sender.hasPermission(CommandsConstants.NEWBIE_PERM)) {
            sender.sendMessage(CommandsConstants.NO_PERMISSION);
            return;
        }

        plugin.getConfig().set("newbieProtection.use", !plugin.getConfig()
                .getBoolean("newbieProtection.use"));
        plugin.saveConfig();

        String state;
        if (plugin.getConfig().getBoolean("newbieProtection.use")) {
            state = "On";
        } else {
            state = "Off";
        }

        sender.sendMessage(CommandsConstants.NEWBIE_NEW_STATE.replace("%state%", state));
    }

    private void optionSet(CommandSender sender, String[] args) {
        if (!sender.hasPermission(CommandsConstants.NEWBIE_PERM)) {
            sender.sendMessage(CommandsConstants.NO_PERMISSION);
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(CommandsConstants.NEWBIE_WRONG_USAGE);
            return;
        }

        if (getUUIDFromName(args[1]) == null) {
            sender.sendMessage(CommandsConstants.NOT_IN_CACHE);
            return;
        }

        if (!isNumeric(args[2])) {
            sender.sendMessage(CommandsConstants.NEWBIE_WRONG_USAGE);
            return;
        }

        CustomConfig newbieConfig = Utilities.getCustomConfig(Utilities.ConfigType.NEWBIE_CONFIG);
        newbieConfig.getConfig().set("players." + Utilities.getUUIDFromName(args[1]), Integer.parseInt(args[2]));
        newbieConfig.saveConfig();

        sender.sendMessage(CommandsConstants.COMMAND_SUCCESSFUL);
    }

}
