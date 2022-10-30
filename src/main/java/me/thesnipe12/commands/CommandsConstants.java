package me.thesnipe12.commands;

import org.bukkit.ChatColor;

import java.util.List;

public class CommandsConstants {
    protected static final String
            NEWBIE_PERM = "simplecl.newbie",
            RELOAD_PERM = "simplecl.reload";
    protected static final String
            NO_PERMISSION = ChatColor.RED + "You don't have permission to do that!",
            ONLY_AVAILABLE_FOR_PLAYERS = ChatColor.RED + "Only players can use this command!",
            NOT_IN_CACHE = ChatColor.RED + "This player is not in the cache!",
            COMMAND_SUCCESSFUL = ChatColor.GREEN + "Command successful!";
    protected static final List<String>
            NEWBIE_OPTIONS = List.of("remove", "get"),
            NEWBIE_PERMS_OPTIONS = List.of("remove", "get", "set", "toggle");

    protected static final String
            NEWBIE_WRONG_USAGE = ChatColor.RED + "Wrong usage of command! Use /sclnewbie <get/remove/set/toggle> [player] [number]!",
            NEWBIE_DISABLED = ChatColor.RED + "Newbie protection is disabled!",
            NOT_NEWBIE_WITH_MAX_TIME = ChatColor.RED + "Not a newbie! Max newbie time is %maxSeconds% seconds!",
            MAX_NEWBIE_TIME = ChatColor.GREEN + "Newbie time is %seconds% seconds!",
            NOT_NEWBIE = ChatColor.RED + "Already not a newbie!",
            NOW_NOT_NEWBIE = ChatColor.GREEN + "Now not a newbie!",
            NEWBIE_NEW_STATE = ChatColor.GREEN + "Newbie protection is now: " + ChatColor.GOLD +"%state%!";
    protected static final String
            RELOADING_CONFIG = ChatColor.GREEN + "Reloading configs...",
            RELOAD_SUCCESSFUL = ChatColor.GREEN + "Reload successful!";

}
