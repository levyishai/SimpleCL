package me.thesnipe12;

import me.thesnipe12.utilities.CustomConfig;
import me.thesnipe12.utilities.PluginUtilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;

public class Timer extends BukkitRunnable {
    private final Plugin plugin;
    private final HashMap<Player, Integer> combatTimer;

    public Timer(Plugin plugin, HashMap<Player, Integer> combatTimer) {
        this.plugin = plugin;
        this.combatTimer = combatTimer;
    }

    @Override
    public void run() {
        for (Player player : combatTimer.keySet()) {
            if (combatTimer.get(player) <= 0 || player == null) continue;

            PluginUtilities.sendActionbar(player, plugin.getConfig().getString("Timer.ActionBarMessage")
                    + plugin.getConfig().getString("Timer.NumberColor") + combatTimer.get(player));

            combatTimer.put(player, combatTimer.get(player) - 1);
            sendLastActionbarIfNeeded(player);
        }

        lowerNewbieTimerIfNeededForOnlinePlayers();
    }

    private void lowerNewbieTimerIfNeededForOnlinePlayers() {
        if (!plugin.getConfig().getBoolean("newbieProtection.use")) return;

        CustomConfig newbieConfig = PluginUtilities.getCustomConfig(PluginUtilities.ConfigType.NEWBIE_CONFIG);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!newbieConfig.getConfig().isSet("players." + p.getUniqueId())) {
                newbieConfig.getConfig().set("players." + p.getUniqueId(), 0);
                newbieConfig.saveConfig();
            } else {
                int maxSeconds = plugin.getConfig().getInt("newbieProtection.seconds");
                int seconds = newbieConfig.getConfig().getInt("players." + p.getUniqueId());

                if (seconds < maxSeconds) {
                    newbieConfig.getConfig().set("players." + p.getUniqueId(), seconds + 1);
                    newbieConfig.saveConfig();
                }
            }
        }
    }

    private void sendLastActionbarIfNeeded(Player player) {
        if (combatTimer.get(player) == 1) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (combatTimer.get(player) > 0) return;

                PluginUtilities.sendActionbar(player, plugin.getConfig().getString("Timer.ActionBarMessage")
                        + plugin.getConfig().getString("Timer.NumberColor") + combatTimer.get(player));
            }, 40L);
        }
    }

}
