package me.thesnipe12.listeners;

import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

import static me.thesnipe12.utilities.PluginUtilities.sendConfigMessage;

public class WorldGuardListener implements Listener {
    private final Plugin plugin;
    private final HashMap<Player, Integer> combatTimer;

    public WorldGuardListener(Plugin plugin, HashMap<Player, Integer> combatTimer) {
        this.plugin = plugin;
        this.combatTimer = combatTimer;
    }

    @EventHandler
    public void on(RegionEnteredEvent event) {
        Player player = event.getPlayer();
        combatTimer.putIfAbsent(player, 0);

        if (plugin.getConfig().getBoolean("allowBorderHopping") || combatTimer.get(player) == 0) return;

        event.setCancelled(true);
        sendConfigMessage("Messages.borderHop", plugin, player, null);
    }

}
