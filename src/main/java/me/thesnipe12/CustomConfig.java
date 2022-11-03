package me.thesnipe12;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * This class is used to create custom config files.
 *
 * @author TheSnipe12
 */
public class CustomConfig {
    private final String fileName;
    private File file;
    private FileConfiguration config;

    /**
     * Creates a new CustomConfig object.
     *
     * @param fileName The name of the file.
     */
    public CustomConfig(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Creates a new config file.
     */
    public void setup() {
        Plugin pl = Bukkit.getPluginManager().getPlugin("SimpleCL");

        if (pl != null) {
            file = new File(pl.getDataFolder(), fileName);
        }

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    Bukkit.getLogger().warning("Failed to create " + fileName);
                }
            } catch (IOException e) {
                e.getStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Gets the custom config file.
     *
     * @return the custom config file.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Saves the custom config file.
     */
    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Could not save " + fileName + "!");
        }
    }

    /**
     * Reloads the custom config file.
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

}
