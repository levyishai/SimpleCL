package me.thesnipe12;

import me.thesnipe12.Listeners.Combat;
import me.thesnipe12.Listeners.CommandSend;
import me.thesnipe12.Listeners.Log;
import me.thesnipe12.Listeners.WorldGuardListener;
import me.thesnipe12.commands.Sclnewbie;
import me.thesnipe12.commands.Sclreload;
import me.thesnipe12.commands.TabCompletion;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SimpleCL extends JavaPlugin {
    public static boolean isUpToDate;

    @Override
    public void onEnable() {
        configsSetup();

        classesSetup();

        WorldGuardSetup();

        commandsSetup();

        checkForUpdate();
    }

    private void checkForUpdate() {
        new UpdateChecker(this, 101603).getVersion(version -> {
            if (!version.equalsIgnoreCase(getDescription().getVersion())) {
                getLogger().warning("There is a new version of the plugin available! Go to " +
                        "\"https://www.spigotmc.org/resources/simplecl.101603/\" to download it.");
                isUpToDate = false;
            } else {
                getLogger().info("You are running the latest version of the plugin!");
                isUpToDate = true;
            }
        });
    }

    private void commandsSetup() {
        final List<String> commands = List.of("sclreload", "sclnewbie");
        for(String command : commands){
            setCommandExecutor(getCommand(command));
        }
    }

    private void WorldGuardSetup() {
        if (!getConfig().getBoolean("borderHopping")) {
            if (Bukkit.getPluginManager().getPlugin("WGRegionEvents") != null) {
                getLogger().info(Constants.BORDER_DISABLED_SUCCESS);
                Bukkit.getPluginManager().registerEvents(new WorldGuardListener(this), this);
            } else {
                getLogger().warning(Constants.BORDER_DISABLED_FAIL);
            }
        }
    }

    private void configsSetup() {
        NewbieConfig.setup();
        NewbieConfig.getNewbieConfig().addDefault("players", null);
        NewbieConfig.getNewbieConfig().options().copyDefaults(true);
        NewbieConfig.saveNewbieConfig();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    private void classesSetup() {
        new Timer(this).runTaskTimer(this, 0L, 20L);

        new Sclnewbie(this);
        new Sclreload(this);
        new TabCompletion();

        List<Listener> listeners = List.of(new CommandSend(this), new Combat(this), new Log(this));
        for (Listener l : listeners) {
            Bukkit.getPluginManager().registerEvents(l, this);
        }
    }
    private void setCommandExecutor(PluginCommand cmd) {
        if(cmd != null){
            switch (cmd.getName()) {
                case "sclreload" -> cmd.setExecutor(new Sclreload(this));
                case "sclnewbie" -> cmd.setExecutor(new Sclnewbie(this));
            }
            cmd.setTabCompleter(new TabCompletion());
        }
    }
}