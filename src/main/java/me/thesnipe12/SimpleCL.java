package me.thesnipe12;

import me.thesnipe12.commands.SclnewbieCommand;
import me.thesnipe12.commands.SclreloadCommand;
import me.thesnipe12.listeners.CombatListener;
import me.thesnipe12.listeners.CommandSendListener;
import me.thesnipe12.listeners.LoggingListener;
import me.thesnipe12.listeners.WorldGuardListener;
import me.thesnipe12.utilities.CustomConfig;
import me.thesnipe12.utilities.Metrics;
import me.thesnipe12.utilities.PluginUtilities;
import me.thesnipe12.utilities.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public final class SimpleCL extends JavaPlugin {
    private final HashMap<Player, Integer> combatTimer = new HashMap<>();
    private final HashMap<Player, Player> lastHitter = new HashMap<>();

    @Override
    public void onEnable() {
        configsSetup();
        classesSetup();
        worldGuardSetup();
        commandsSetup();
        checkForUpdates();
        new Metrics(this, PluginConstants.METRICS_ID);
    }

    private void configsSetup() {
        for (PluginUtilities.ConfigType configType : PluginUtilities.ConfigType.values()) {
            CustomConfig currentConfig = PluginUtilities.getCustomConfig(configType);

            currentConfig.setup();
            currentConfig.getConfig().options().copyDefaults(true);
            currentConfig.saveConfig();
        }

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    private void classesSetup() {
        new Timer(this, combatTimer).runTaskTimer(this, 0L, 20L);

        final List<Listener> listeners = List.of(new CommandSendListener(this, combatTimer),
                new CombatListener(this, combatTimer, lastHitter), new LoggingListener(this, combatTimer, lastHitter));

        for (Listener l : listeners) {
            Bukkit.getPluginManager().registerEvents(l, this);
        }
    }

    private void worldGuardSetup() {
        if (!getConfig().getBoolean("allowBorderHopping")) {
            if (Bukkit.getPluginManager().getPlugin("WorldGuardEvents") != null) {
                getLogger().info(PluginConstants.BORDER_DISABLE_SUCCESS);
                Bukkit.getPluginManager().registerEvents(new WorldGuardListener(this, combatTimer), this);
            } else {
                getLogger().warning(PluginConstants.BORDER_DISABLE_FAIL);
            }
        }
    }

    private void commandsSetup() {
        final List<TabExecutor> commandClasses = List.of(new SclnewbieCommand(this), new SclreloadCommand(this, combatTimer));

        for (int i = commandClasses.size(); i > 0; i--) {
            PluginConstants.COMMANDS.get(i - 1).setExecutor(commandClasses.get(i - 1));
        }
    }

    private void checkForUpdates() {
        new UpdateChecker(this, PluginConstants.RESOURCE_ID).getVersion(version -> {
            if (!version.equalsIgnoreCase(getDescription().getVersion())) {
                getLogger().warning("There is a new version of the plugin available! Go to \"" + PluginConstants.PLUGIN_LINK + "\" to download it.");
            } else {
                getLogger().info("You are running the latest version of the plugin!");
            }
        });
    }

}
