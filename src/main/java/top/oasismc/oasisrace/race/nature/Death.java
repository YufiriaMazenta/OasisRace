package top.oasismc.oasisrace.race.nature;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import top.oasismc.oasisrace.OasisRace;
import top.oasismc.oasisrace.RaceEffectManager;

public enum Death implements Listener {

    INSTANCE;

    private final String racePerm;

    Death() {
        racePerm = OasisRace.getPluginConfig().getString("death.perm", "oasis.race.nature.death");
        RaceEffectManager.INSTANCE.regRaceEffect(racePerm, new PotionEffect(PotionEffectType.FAST_DIGGING, 40, 0, true, false));
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
        if (potionType.equals(PotionEffectType.POISON) || potionType.equals(PotionEffectType.WITHER)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCriticalHurtEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager))
            return;
        if (damager.isOp())
            return;
        if (!damager.hasPermission(racePerm))
            return;
        if (!event.isCritical())
            return;
        if (!(event.getEntity() instanceof LivingEntity entity))
            return;
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 100, 0, false, true));
    }

    @EventHandler
    public void onHurtEntityByPotion(PotionSplashEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        for (LivingEntity entity : event.getAffectedEntities()) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1, false, true));
        }
    }

}
