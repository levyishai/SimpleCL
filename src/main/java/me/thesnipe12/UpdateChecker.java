package me.thesnipe12;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * This class is used to check for updates.
 *
 * @author TheSnipe12
 */
public class UpdateChecker {
    private final Plugin plugin;
    private final int resourceId;

    /**
     * Creates a new UpdateChecker object.
     *
     * @param plugin     the plugin.
     * @param resourceId the resource id.
     */
    public UpdateChecker(Plugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    /**
     * Gets the latest version from the spigot page of the resource ID,
     * and accepts the version to the consumer when the version is reached.
     *
     * @param consumer the consumer to be accepted once the version is reached.
     */
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" +
                    this.resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

}
