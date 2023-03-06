package me.thesnipe12.listeners;

import me.thesnipe12.utilities.PluginUtilities;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;

import static me.thesnipe12.utilities.PluginUtilities.getCustomConfig;
import static me.thesnipe12.utilities.PluginUtilities.sendConfigMessage;

public class CombatListener implements Listener {
    private final Plugin plugin;
    private final HashMap<Player, Integer> combatTimer;
    private final HashMap<Player, Player> lastHitter;

    public CombatListener(Plugin plugin, HashMap<Player, Integer> combatTimer, HashMap<Player, Player> lastHitter) {
        this.plugin = plugin;
        this.combatTimer = combatTimer;
        this.lastHitter = lastHitter;
    }

    @EventHandler(ignoreCancelled = true)
    public void onNewbie(EntityDamageByEntityEvent event) {
        if (!plugin.getConfig().getBoolean("newbieProtection.use")) return;

        Entity damager = event.getDamager();
        if (projectileToEntity(damager) != null) {
            damager = projectileToEntity(damager);
        }
        final Entity damaged = event.getEntity();

        if (!areUniquePlayers(damager, damaged, true)) return;

        final Entity newbie = getNewbie(damager, damaged);
        if (newbie == null) return;

        event.setCancelled(true);

        if (newbie == damager) {
            sendConfigMessage("Messages.whileNewbie", plugin, (Player) damager, damaged.getName());
        } else if (damager instanceof Player) {
            sendConfigMessage("Messages.hasNewbie", plugin, (Player) damager, damaged.getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (projectileToEntity(damager) != null) {
            damager = projectileToEntity(damager);
        }
        final Entity damaged = event.getEntity();

        if (!areUniquePlayers(damager, damaged, false)) return;

        combatTimer.putIfAbsent((Player) damaged, 0);
        if (damager instanceof Player) {
            combatTimer.putIfAbsent((Player) damager, 0);
            lastHitter.put(((Player) damaged), (Player) damager);
            lastHitter.put(((Player) damager), (Player) damaged);

            if (combatTimer.get(damager) == 0) {
                sendConfigMessage("Messages.TaggerMessage", plugin, (Player) damager, damaged.getName());
            }

            combatTimer.put((Player) damager, plugin.getConfig().getInt("Timer.CombatTime"));
        }

        if (combatTimer.get(damaged) == 0) {
            sendConfigMessage("Messages.TaggedMessage", plugin, (Player) damaged, damager.getName());
        }

        combatTimer.put((Player) damaged, plugin.getConfig().getInt("Timer.CombatTime"));
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();

        combatTimer.put(player, 0);

        if (event.getEntity().getKiller() != null) return;
        combatTimer.put(event.getEntity().getKiller(), 0);
    }

    private Entity getNewbie(Entity damager, Entity damaged) {
        int maxSeconds = plugin.getConfig().getInt("newbieProtection.seconds");
        int damagedSeconds = getCustomConfig(PluginUtilities.ConfigType.NEWBIE_CONFIG).getConfig()
                .getInt("players." + damaged.getUniqueId());
        int damagerSeconds = getCustomConfig(PluginUtilities.ConfigType.NEWBIE_CONFIG).getConfig()
                .getInt("players." + damager.getUniqueId());

        if (damagerSeconds < maxSeconds) return damaged;
        if (damagedSeconds < maxSeconds) return damager;

        return null;
    }

    private Entity projectileToEntity(Entity entity) {
        if (entity instanceof Projectile && ((Projectile) entity).getShooter() instanceof Entity) {
            ProjectileSource projectileSource = ((Projectile) entity).getShooter();
            return (Entity) projectileSource;
        }

        return null;
    }

    private boolean isEndCrystal(Entity entity, boolean forNewbie) {
        return (forNewbie ? !plugin.getConfig().getBoolean("newbieProtection.crystalsDamage") :
                plugin.getConfig().getBoolean("Timer.CrystalTag")) && (entity instanceof EnderCrystal);
    }

    private boolean areUniquePlayers(Entity damager, Entity damaged, boolean forNewbie) {
        return ((damager instanceof Player || isEndCrystal(damager, forNewbie)) &&
                damaged instanceof Player && damager != damaged);
    }

}
