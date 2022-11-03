package me.thesnipe12;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utilities {
    /**
     * Gets a message from the main config, translates color codes, checks for null, then sends it to the player.
     *
     * @param path        the path to the message.
     * @param plugin      the plugin.
     * @param player      the player to send to.
     * @param replacement the replacement for the %player% placeholder.
     */
    public static void sendConfigMessage(String path, Plugin plugin, Player player, String replacement) {
        if (plugin.getConfig().getString(path, "").isEmpty()) return;

        if (replacement == null) {
            replacement = "%player%";
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix", "") + plugin.getConfig().getString(path)).replace("%player%", replacement));
    }

    /**
     * Gets a message from the main config, translates color codes, checks for null, then broadcasts it.
     *
     * @param path        the path to the message.
     * @param plugin      the plugin.
     * @param replacement the replacement for the %player% placeholder, put as null for no replacement.
     */
    public static void broadcastConfigMessage(String path, Plugin plugin, String replacement) {
        if (plugin.getConfig().getString(path, "").isEmpty()) return;

        if (replacement == null) {
            replacement = "%player%";
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix", "") + plugin.getConfig().getString(path)).replace("%player%", replacement));
    }

    /**
     * Checks if a string is numeric.
     *
     * @param str the string to check.
     * @return true if the string is numeric, false otherwise.
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Sends an action bar message to a player.
     *
     * @param player  the player to send the message to.
     * @param message the message to send.
     */
    public static void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }

    /**
     * Gets a players UUID from their name, even if they're offline (as long as they're in the servers offline players).
     *
     * @param name the name of the player.
     * @return the UUID of the player, null if the player wasn't found in the servers offline players.
     */
    public static UUID getUUIDFromName(String name) {
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

    /**
     * Gets all offline players as a list of strings.
     *
     * @return a list of all offline players.
     */
    public static List<String> getOfflinePlayersAsStringList() {
        List<String> offlinePlayers = new ArrayList<>();

        for (OfflinePlayer p : Bukkit.getServer().getOfflinePlayers()) {
            if (p.getName() == null) {
                continue;
            }

            offlinePlayers.add(p.getName());
        }

        return offlinePlayers;
    }

    /**
     * Gets all the available options that start with the given string's last index.
     *
     * @param availableOptions the available options.
     * @param args             the current arguments of the commands.
     * @return the list of the available options that start with the given string's last index.
     */
    public static List<String> getCommandOptions(List<String> availableOptions, String[] args) {
        final String input = args[args.length - 1].toLowerCase();
        List<String> completions = new ArrayList<>();

        for (String s : availableOptions) {
            if (s.toLowerCase().startsWith(input)) {
                completions.add(s);
            }
        }

        return completions;
    }

    /**
     * Checks if there's a registered listener that is an instance of the given class.
     *
     * @param plugin        the plugin.
     * @param listenerClass the class of the listener.
     * @return true if there's a registered listener that is an instance of the given class, false otherwise.
     */
    public static boolean isListenerRegistered(Plugin plugin, Class<?> listenerClass) {
        final boolean[] registered = {false};

        HandlerList.getRegisteredListeners(plugin).forEach(listener -> {
            if (listenerClass.isInstance(listener.getListener())) {
                registered[0] = true;
            }
        });

        return registered[0];
    }

    /**
     * Gets a custom config from a config type.
     *
     * @param configType the config type.
     * @return the custom config.
     */
    public static CustomConfig getCustomConfig(ConfigType configType) {
        return configType.customConfig;
    }

    /**
     * An enum for the different config types.
     */
    public enum ConfigType {
        NEWBIE_CONFIG(new CustomConfig("newbieConfig.yml"));

        final CustomConfig customConfig;

        ConfigType(CustomConfig customConfig) {
            this.customConfig = customConfig;
        }
    }

}
