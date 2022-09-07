package me.thesnipe12;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static me.thesnipe12.Constants.combatTimer;
import static me.thesnipe12.Utils.getConfigMessage;
import static org.bukkit.Bukkit.getPlayer;

public class Timer extends BukkitRunnable {

    private final Plugin plugin;

    public Timer(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public void run() {
        for (String s : combatTimer.keySet()) {
            Player player = getPlayer(s);
            if (combatTimer.get(s) > 0) {
                if (player != null) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.
                            translateAlternateColorCodes('&', plugin.getConfig().
                                    getString("Timer.ActionBarMessage") + plugin.getConfig().
                                    getString("Timer.NumberColor") + combatTimer.get(s))));
                    combatTimer.put(s, (combatTimer.get(s) - 1));
                    if (combatTimer.get(s) == 1) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if(combatTimer.get(player.getName()) > 0) return;
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
                            if(getConfigMessage("Messages.LeftCombatMessage", plugin).equals("")) return;
                            player.sendMessage(getConfigMessage(
                                    "Messages.LeftCombatMessage", plugin).replace("%player%", s));
                        }, 40L);
                    }
                }
            }
        }
        if (plugin.getConfig().getBoolean("newbieProtection.use")){
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (!NewbieConfig.getNewbieConfig().isSet("players." + p.getUniqueId())) {
                    NewbieConfig.getNewbieConfig().set("players." + p.getUniqueId(), 0);
                    NewbieConfig.saveNewbieConfig();
                } else {
                    int maxSeconds = plugin.getConfig().getInt("newbieProtection.seconds");
                    int seconds = NewbieConfig.getNewbieConfig().getInt("players." + p.getUniqueId());
                    if (seconds < maxSeconds) {
                        NewbieConfig.getNewbieConfig().set("players." + p.getUniqueId(), seconds + 1);
                        NewbieConfig.saveNewbieConfig();
                    }
                }
            }
        }
    }
}
