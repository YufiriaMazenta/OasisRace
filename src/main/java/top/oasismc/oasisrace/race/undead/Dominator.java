package top.oasismc.oasisrace.race.undead;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import top.oasismc.oasisrace.OasisRace;
import top.oasismc.oasisrace.RaceEffectManager;

import java.util.ArrayList;
import java.util.List;

public enum Dominator implements Listener {

    INSTANCE;

    private final String raceName;
    private final String racePerm;
    private List<EntityType> undeadEntities;

    Dominator() {
        raceName = OasisRace.getPluginConfig().getString("dominator.team", "Dominator");
        racePerm = OasisRace.getPluginConfig().getString("dominator.perm", "oasis.race.undead.dominator");
        RaceEffectManager.INSTANCE.regRaceEffect(racePerm, new PotionEffect(PotionEffectType.FAST_DIGGING, 50, 0, true, false));
        loadUndeadEntities();
    }

    @EventHandler
    public void onEntityTargetDominator(EntityTargetLivingEntityEvent event) {
        if (!OasisRace.getPluginConfig().getBoolean("dominator.undead_ignore", true))
            return;
        EntityType type = event.getEntityType();
        if (!undeadEntities.contains(type))
            return;
        if (!(event.getTarget() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onHurtByEntity(EntityDamageByEntityEvent event) {
        if (!OasisRace.getPluginConfig().getBoolean("dominator.undead_help", true))
            return;
        Entity damager = event.getDamager();
        if (damager instanceof Projectile) {
            if (((Projectile) damager).getShooter() instanceof Entity)
                damager = (Entity) ((Projectile) damager).getShooter();
        }
        if (!(event.getEntity() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        int radius = OasisRace.getPluginConfig().getInt("dominator.convene_radius", 25);
        for (Entity entity1 : player.getNearbyEntities(radius, radius, radius)) {
            if (!undeadEntities.contains(entity1.getType()))
                continue;
            try {
                Mob mob = (Mob) entity1;
                mob.setTarget((LivingEntity) damager);
            } catch (Exception ignore) {}
        }
    }

    @EventHandler
    public void onGetBlindnessEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        if (!event.getAction().equals(EntityPotionEffectEvent.Action.ADDED))
            return;
        if (!event.getModifiedType().equals(PotionEffectType.BLINDNESS))
            return;
        event.setCancelled(true);
    }

    private void loadUndeadEntities() {
        undeadEntities = new ArrayList<>();
        undeadEntities.add(EntityType.ZOMBIE);
        undeadEntities.add(EntityType.SKELETON);
        undeadEntities.add(EntityType.STRAY);
        undeadEntities.add(EntityType.WITHER_SKELETON);
        undeadEntities.add(EntityType.HUSK);
        undeadEntities.add(EntityType.ZOMBIFIED_PIGLIN);
        undeadEntities.add(EntityType.ZOMBIE_VILLAGER);
        undeadEntities.add(EntityType.DROWNED);
        undeadEntities.add(EntityType.ZOGLIN);
        undeadEntities.add(EntityType.ZOMBIE_HORSE);
        undeadEntities.add(EntityType.SKELETON_HORSE);
        undeadEntities.add(EntityType.PHANTOM);
    }

}
