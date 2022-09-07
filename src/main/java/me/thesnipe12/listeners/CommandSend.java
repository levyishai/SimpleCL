package me.thesnipe12.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import static me.thesnipe12.Constants.combatTimer;
import static me.thesnipe12.Utils.getConfigMessage;

public class CommandSend implements Listener {
    private final Plugin plugin;

    public CommandSend(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!(combatTimer.containsKey(player.getName()) &&
                combatTimer.get(player.getName()) != 0)) return;
        for (String i : plugin.getConfig().getStringList("Timer.BannedCommands")) {
            if (i.equals("none")) return;
            String[] message = event.getMessage().split(" ");
            if (message[0].equalsIgnoreCase("/" + i)) {
                if(!getConfigMessage("Messages.NonAllowed", plugin).equals("")) {
                    player.sendMessage(getConfigMessage("Messages.NonAllowed", plugin));
                }
                event.setCancelled(true);
                break;
            }
        }
    }
}
