package me.thesnipe12.Listeners;

import me.thesnipe12.NewbieConfig;
import me.thesnipe12.SimpleCL;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;

import static me.thesnipe12.Utils.getConfigMessage;

public class Combat implements Listener {
    SimpleCL plugin;
    public Combat(SimpleCL plugin) {
        this.plugin = plugin;
    }
    public static HashMap<String, Integer> combatTimer = new HashMap<>();
    public static HashMap<Player, Player> lastHitter = new HashMap<>();
    @EventHandler(ignoreCancelled = true)
    public void on2(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        if(projectileToEntity(damager) != null){
            damager = projectileToEntity(damager);
        }
        Entity damaged = event.getEntity();
        if (!((damager instanceof Player || isEndCrystal(damager, true)) &&
                damaged instanceof Player && damager != damaged)) return;
        if (plugin.getConfig().getBoolean("!newbieProtection.use")) return;
        int maxSeconds = plugin.getConfig().getInt("newbieProtection.seconds");
        int secondsD = NewbieConfig.getNewbieConfig().getInt("players." + damaged.getUniqueId());
        int secondsR = NewbieConfig.getNewbieConfig().getInt("players." + damager.getUniqueId());
        if (!(secondsD < maxSeconds || secondsR < maxSeconds)) return;
        event.setCancelled(true);
        if (secondsR < maxSeconds) {
            if(getConfigMessage("Messages.whileNewbie", plugin).equals("")) return;
            damager.sendMessage(getConfigMessage("Messages.whileNewbie", plugin).replace("%player%", damaged.getName()));
        } else {
            if(getConfigMessage("Messages.hasNewbie", plugin).equals("")) return;
            damager.sendMessage(getConfigMessage("Messages.hasNewbie", plugin).replace("%player%", damaged.getName()));
        }
    }
    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void on(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if(projectileToEntity(damager) != null){
            damager = projectileToEntity(damager);
        }
        Entity damaged = event.getEntity();
        if (!((damager instanceof Player || isEndCrystal(damager, false)) &&
                damaged instanceof Player && damager != damaged)) return;
        combatTimer.putIfAbsent(damager.getName(), 0);
        combatTimer.putIfAbsent(damaged.getName(), 0);
        if(damager instanceof Player){
            lastHitter.put(((Player) damaged), (Player) damager);
            lastHitter.put(((Player) damager), (Player) damaged);
        }
        if (combatTimer.get(damager.getName()) == 0) {
            if(!getConfigMessage("Messages.TaggerMessage", plugin).equals("")){
                damager.sendMessage(getConfigMessage("Messages.TaggerMessage", plugin).replace("%player", damaged.getName()));
            }
        }
        if (combatTimer.get(damaged.getName()) == 0) {
            if(!getConfigMessage("Messages.TaggedMessage", plugin).equals("")){
                damager.sendMessage(getConfigMessage("Messages.TaggerMessage", plugin).replace("%player", damager.getName()));
            }
        }
        combatTimer.put(damager.getName(), plugin.getConfig().getInt("Timer.CombatTime"));
        combatTimer.put(damaged.getName(), plugin.getConfig().getInt("Timer.CombatTime"));

    }
    @EventHandler
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();
        combatTimer.put(player.getName(), 0);
        if(event.getEntity().getKiller() != null) return;
        combatTimer.put(event.getEntity().getKiller().getName(), 0);
    }
    private Entity projectileToEntity(Entity entity){
        if (entity instanceof Projectile && ((Projectile) entity).getShooter() instanceof Entity){
            ProjectileSource projectileSource = ((Projectile) entity).getShooter();
            return (Entity) projectileSource;
        }
        return null;
    }
    private boolean isEndCrystal(Entity entity, boolean forNewbie){
        return (forNewbie ? plugin.getConfig().getBoolean("newbieProtection.crystalsDamage") :
                plugin.getConfig().getBoolean("Timer.CrystalTag"))  && (entity instanceof EnderCrystal);
    }
}
