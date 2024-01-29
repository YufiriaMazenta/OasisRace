package top.oasismc.oasisrace.race.undead;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.oasisrace.OasisRace;
import top.oasismc.oasisrace.RaceEffectManager;

public class UnDead extends BukkitRunnable implements Listener {

    public static final UnDead INSTANCE = new UnDead();
    private final String racePerm;
    
    private UnDead() {
        racePerm = OasisRace.getPluginConfig().getString("undead.perm", "oasis.race.undead");
        RaceEffectManager.INSTANCE.regRaceEffect(racePerm, new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0, true, false));
    }

    @EventHandler
    public void onPlayerAirChange(EntityAirChangeEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER))
            return;
        if (event.getEntity().isOp())
            return;
        if (!event.getEntity().hasPermission(racePerm))
            return;
        event.setAmount(3000);
    }

    @EventHandler
    public void onDamagedByPotion(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!event.getEntity().hasPermission(racePerm))
            return;
        if (event.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)) {
            event.setCancelled(true);
            double health = player.getHealth();
            health += event.getFinalDamage();
            player.setHealth(Math.min(player.getMaxHealth(), health));
        }
    }

    @EventHandler
    public void onHealByPotion(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!event.getEntity().hasPermission(racePerm))
            return;
        if (event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.MAGIC)) {
            event.setCancelled(true);
            double amount = event.getAmount();
            player.damage(amount);
        }
    }

    @EventHandler
    public void onGetPotion(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (event.getEntity().isOp())
            return;
        if (!event.getEntity().hasPermission(racePerm))
            return;
        if (event.getNewEffect() == null)
            return;
        var potionType = event.getModifiedType();
        if (potionType.equals(PotionEffectType.POISON) || potionType.equals(PotionEffectType.REGENERATION)) {
            event.setCancelled(true);
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp())
                continue;
            if (!player.hasPermission(racePerm)) {
                continue;
            }
            World world = player.getWorld();
            if (world.getTime() >= OasisRace.getPluginConfig().getLong("undead.night_time", 13000)) {
                continue;
            }
            Location eyeLoc = player.getEyeLocation();
            if (!eyeLoc.getWorld().isClearWeather())
                continue;
            int skyLight = eyeLoc.getBlock().getLightFromSky();
            if (skyLight == 15) {
                if (player.isInWater())
                    continue;
                player.setFireTicks(30);
            }
        }
    }

}
