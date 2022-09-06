package me.thesnipe12.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("sclnewbie")) {
            if (args.length == 1) {
                if (sender.hasPermission("simplecl.newbie")) {
                    list = Arrays.asList("remove", "get", "set", "toggle");
                } else {
                    list = Arrays.asList("remove", "get");
                }
            } else if (args.length == 2 && sender.hasPermission("simplecl.newbie") && (args[0]
                    .equalsIgnoreCase("set")
                    || args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("remove"))) {
                for (OfflinePlayer p : Bukkit.getServer().getOfflinePlayers()) {
                    list.add(p.getName());
                }
            } else {
                return new ArrayList<>();
            }
        }
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        String input = args[args.length - 1].toLowerCase();
        List<String> completions = null;
        for (String s : list) {
            if (s.toLowerCase().startsWith(input)) {
                if (completions == null) {
                    completions = new ArrayList<>();
                }
                completions.add(s);
            }
        }
        if (completions == null) {
            return new ArrayList<>();
        }
        return completions;
    }

}
