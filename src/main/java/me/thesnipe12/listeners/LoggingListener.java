package me.thesnipe12.listeners;

import me.thesnipe12.utilities.CustomConfig;
import me.thesnipe12.PluginConstants;
import me.thesnipe12.utilities.UpdateChecker;
import me.thesnipe12.utilities.PluginUtilities;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoggingListener implements Listener {
    private final Plugin plugin;
    private final List<Player> kicked = new ArrayList<>();
    private final HashMap<Player, Integer> combatTimer;
    private final HashMap<Player, Player> lastHitter;

    public LoggingListener(Plugin plugin, HashMap<Player, Integer> combatTimer, HashMap<Player, Player> lastHitter) {
        this.plugin = plugin;
        this.combatTimer = combatTimer;
        this.lastHitter = lastHitter;
    }

    @EventHandler
    public void on(PlayerKickEvent event) {
        if (!plugin.getConfig().getBoolean("Timer.killOnKicks")) kicked.add(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        combatTimer.putIfAbsent(player, 0);

        if (kicked.contains(event.getPlayer()) || combatTimer.get(player) == 0) {
            kicked.remove(event.getPlayer());
            return;
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kill " + player.getName());
        PluginUtilities.broadcastConfigMessage("Messages.CombatLoggedMessage", plugin, player.getName());

        combatTimer.put(player, 0);
        if (!lastHitter.containsKey(player)) return;

        removeHeartIfEnabled(player);
        giveHeartIfEnabled(lastHitter.get(player));
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        combatTimer.putIfAbsent(player, 0);
        CustomConfig newbieConfig = PluginUtilities.getCustomConfig(PluginUtilities.ConfigType.NEWBIE_CONFIG);

        if (!newbieConfig.getConfig().isSet("players." + player.getUniqueId()) && plugin.getConfig().getBoolean("newbieProtection.use")) {
            newbieConfig.getConfig().set("players." + player.getUniqueId(), 0);
            newbieConfig.saveConfig();
        }

        sendNotUpToDateMessageIfOperator(player);
    }

    private void sendNotUpToDateMessageIfOperator(Player player) {
        if (!Bukkit.getServer().getOperators().contains(player)) return;

        new UpdateChecker(plugin, PluginConstants.RESOURCE_ID).getVersion(version -> {

            if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                final TextComponent prefix = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("prefix") + ""));

                player.spigot().sendMessage(prefix, ListenersConstants.NOT_UP_TO_DATE_MESSAGE_PART1,
                        ListenersConstants.NOT_UP_TO_DATE_MESSAGE_PART2, ListenersConstants.NOT_UP_TO_DATE_MESSAGE_PART3);
            }

        });
    }

    private void removeHeartIfEnabled(Player player) {
        AttributeInstance playerAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (plugin.getConfig().getBoolean("LifeSteal.RemoveHeart") && playerAttribute != null) {
            playerAttribute.setBaseValue(playerAttribute.getValue() - 2);
        }
    }

    private void giveHeartIfEnabled(Player player) {
        AttributeInstance playerAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (plugin.getConfig().getBoolean("LifeSteal.GiveHeart") && playerAttribute != null) {
            playerAttribute.setBaseValue(playerAttribute.getValue() + 2);
        }
    }

}
