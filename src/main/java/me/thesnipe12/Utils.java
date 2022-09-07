package me.thesnipe12;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class Utils {
    public static String getConfigMessage(String path, Plugin plugin) {
        if(plugin.getConfig().getString(path, "").isEmpty()) return "";
        return ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("prefix", "") + plugin.getConfig().getString(path));
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
