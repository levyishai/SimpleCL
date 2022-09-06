package me.thesnipe12;

import org.bukkit.ChatColor;

public class Utils {
    public static String getConfigMessage(String path, SimpleCL plugin) {
        if("".equals(plugin.getConfig().getString(path))) return "";
        return ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("prefix") + plugin.getConfig().getString(path));
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
