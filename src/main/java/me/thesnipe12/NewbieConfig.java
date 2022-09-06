package me.thesnipe12;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class NewbieConfig {
    static SimpleCL plugin;
    public NewbieConfig(SimpleCL plugin) {
        NewbieConfig.plugin = plugin;
    }
    private static File file;
    private static FileConfiguration newbieConfig;
    public static void setup() {
        Plugin pl = Bukkit.getPluginManager().getPlugin("SimpleCL");
        if(pl != null) {
            file = new File(pl.getDataFolder(),
                    "newbieconfig.yml");
        }

        if(!file.exists()) {
            try{
                if(!file.createNewFile()){
                    plugin.getLogger().warning("Failed to create newbieconfig.yml");
                }
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
        newbieConfig = YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration getNewbieConfig() {
        return newbieConfig;
    }
    public static void saveNewbieConfig() {
        try {
            newbieConfig.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save newbieconfig.yml!");
        }
    }
    public static void reloadNewbieConfig() {
        newbieConfig = YamlConfiguration.loadConfiguration(file);
    }
}
