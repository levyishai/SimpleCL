package me.thesnipe12.listeners;

import me.thesnipe12.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class CommandSendListener implements Listener {
    private final Plugin plugin;
    private final HashMap<Player, Integer> combatTimer;

    public CommandSendListener(Plugin plugin, HashMap<Player, Integer> combatTimer) {
        this.plugin = plugin;
        this.combatTimer = combatTimer;
    }

    @EventHandler
    public void on(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        combatTimer.putIfAbsent(player, 0);

        if (combatTimer.get(player) == 0) return;

        String[] message = event.getMessage().split(" ");

        if(!isBannedCommand(message[0])) return;

        Utilities.sendConfigMessage("Messages.NonAllowed", plugin, player, null);
        
        event.setCancelled(true);
    }

    private boolean isBannedCommand(String commandName) {
        for (String currentCommand : plugin.getConfig().getStringList("Timer.BannedCommands")) {
            if(currentCommand.equalsIgnoreCase("none")) return false;
            if (("/"+currentCommand).equalsIgnoreCase(commandName)) return true;
        }
        return false;
    }

}
