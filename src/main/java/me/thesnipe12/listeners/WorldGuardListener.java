package me.thesnipe12.listeners;

import de.netzkronehd.wgregionevents.events.RegionEnterEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import static me.thesnipe12.Utils.getConfigMessage;
import static me.thesnipe12.listeners.Combat.combatTimer;

public class WorldGuardListener implements Listener {
    private final Plugin plugin;

    public WorldGuardListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(RegionEnterEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("borderHopping") || !(combatTimer.containsKey(player.getName())
                && combatTimer.get(player.getName()) != 0)) return;
        event.setCancelled(true);
        if(getConfigMessage("Messages.borderHop", plugin).equals("")) return;
        player.sendMessage(getConfigMessage("Messages.borderHop", plugin));
    }
}
