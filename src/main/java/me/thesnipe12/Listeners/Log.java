package me.thesnipe12.Listeners;

import me.thesnipe12.Constants;
import me.thesnipe12.NewbieConfig;
import me.thesnipe12.SimpleCL;
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

import java.util.ArrayList;
import java.util.List;

import static me.thesnipe12.Listeners.Combat.combatTimer;
import static me.thesnipe12.SimpleCL.isUpToDate;
import static me.thesnipe12.Utils.getConfigMessage;

public class Log implements Listener {
    SimpleCL plugin;
    public Log(SimpleCL plugin) {
        this.plugin = plugin;
    }
    private final List<Player> kicked = new ArrayList<>();
    @EventHandler
    public void on(PlayerKickEvent event){kicked.add(event.getPlayer());}
    @EventHandler(priority = EventPriority.LOW)
    public void on(PlayerQuitEvent event) {
        if(kicked.contains(event.getPlayer())) return;
        Player player = event.getPlayer();
        combatTimer.putIfAbsent(player.getName(), 0);
        if (combatTimer.get(player.getName()) != 0) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kill " + player.getName());
            if(!getConfigMessage("Messages.CombatLoggedMessage", plugin).equals("")){
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        getConfigMessage("Messages.CombatLoggedMessage", plugin).replace("%player%", player.getName())));
            }
            combatTimer.put(player.getName(), 0);
            if(!Combat.lastHitter.containsKey(player)) return;
            AttributeInstance health = Combat.lastHitter.get(player).getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (plugin.getConfig().getBoolean("LifeSteal.GiveHeart") && health != null) {
                health.setBaseValue(health.getValue() + 2);
            }
            health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (plugin.getConfig().getBoolean("LifeSteal.RemoveHeart") && health != null) {
                health.setBaseValue(health.getValue() - 2);
            }
        }
    }
    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        combatTimer.putIfAbsent(player.getName(), 0);
        if(!NewbieConfig.getNewbieConfig().isSet("players."+player.getUniqueId()) && plugin.getConfig().
                getBoolean("newbieProtection.use")){
            NewbieConfig.getNewbieConfig().set("players."+player.getUniqueId(), 0);
            NewbieConfig.saveNewbieConfig();
        }
        if(Bukkit.getServer().getOperators().contains(event.getPlayer()) && !isUpToDate){
            final TextComponent prefix = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("prefix")+""));
            player.spigot().sendMessage(prefix,Constants.UPD_MESSAGE_PART1,
                    Constants.UPD_MESSAGE_PART2, Constants.UPD_MESSAGE_PART3);
        }
    }
}
