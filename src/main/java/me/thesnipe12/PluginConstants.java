package me.thesnipe12;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;

import java.util.List;
import java.util.Objects;

public class PluginConstants {
    public static final int
            METRICS_ID = 17864,
            RESOURCE_ID = 101603;
    public static final List<PluginCommand> COMMANDS = List.of(
            Objects.requireNonNull(Bukkit.getPluginCommand("sclnewbie")),
            Objects.requireNonNull(Bukkit.getPluginCommand("sclreload")));
    public static final String
            BORDER_DISABLE_FAIL = "Border Hopping is not allowed but WorldGuardEvents is not found!" +
            " got to https://www.spigotmc.org/resources/worldguard-events.65176/ to install it",
            BORDER_DISABLE_SUCCESS = "WorldGuardEvents found! Border Hopping not allowed!";

}
