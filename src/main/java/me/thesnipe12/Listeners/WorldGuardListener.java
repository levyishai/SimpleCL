package me.thesnipe12.Listeners;

import de.netzkronehd.wgregionevents.events.RegionEnterEvent;
import me.thesnipe12.SimpleCL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.thesnipe12.Listeners.Combat.combatTimer;
import static me.thesnipe12.Utils.getConfigMessage;

public class WorldGuardListener implements Listener {
    SimpleCL plugin;

    public WorldGuardListener(SimpleCL plugin) {
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
