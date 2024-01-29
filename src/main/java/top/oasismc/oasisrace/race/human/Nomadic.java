package top.oasismc.oasisrace.race.human;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.oasisrace.OasisRace;

public class Nomadic extends BukkitRunnable implements Listener {

    public static final Nomadic INSTANCE = new Nomadic();
    private final String raceName;
    private final String racePerm;

    private Nomadic() {
        raceName = OasisRace.getPluginConfig().getString("nomadic.team", "nomadic");
        racePerm = OasisRace.getPluginConfig().getString("nomadic.perm", "oasis.race.undead.nomadic");
    }

    @EventHandler
    public void onBreedEntity(EntityBreedEvent event) {
        if (!(event.getBreeder() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        event.setExperience((int) (event.getExperience() * (1 + OasisRace.getPluginConfig().getDouble("nomadic.extra_breed_exp", 0.5))));
    }

    @EventHandler
    public void onTraderSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.WANDERING_TRADER) && !entity.getType().equals(EntityType.TRADER_LLAMA))
            return;
        boolean canSpawn = false;
        int radius = OasisRace.getPluginConfig().getInt("nomadic.wandering_villager_radius", 50);
        for (Entity nearbyEntity : entity.getNearbyEntities(radius, radius, radius)) {
            if (nearbyEntity.hasPermission(racePerm)) {
                canSpawn = true;
                break;
            }
        }
        if (!canSpawn)
            event.setCancelled(true);
    }

    @EventHandler
    public void onKillAnimal(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null)
            return;
        if (killer.isOp())
            return;
        if (!killer.hasPermission(racePerm))
            return;
        if (!(event.getEntity() instanceof Animals))
            return;
        for (ItemStack drop : event.getDrops()) {
            drop.setAmount(drop.getAmount() + OasisRace.getPluginConfig().getInt("nomadic.extra_animal_drop", 1));
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp())
                continue;
            if (!player.hasPermission(racePerm))
                continue;
            Entity entity = player.getVehicle();
            if (!(entity instanceof LivingEntity livingEntity))
                return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30, 0, true, false));
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 0, true, false));
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 0, true, false));
        }
    }

}
