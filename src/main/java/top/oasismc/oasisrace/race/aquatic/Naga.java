package top.oasismc.oasisrace.race.aquatic;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import top.oasismc.oasisrace.OasisRace;
import top.oasismc.oasisrace.RaceEffectManager;

public enum Naga implements Listener {

    INSTANCE;
    private final String raceName;
    private final String racePerm;

    Naga() {
        raceName = OasisRace.getPluginConfig().getString("naga.team", "naga");
        racePerm = OasisRace.getPluginConfig().getString("naga.perm", "oasis.race.aquatic.naga");
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
        if (potionType.equals(PotionEffectType.POISON)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHurtEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager))
            return;
        if (damager.isOp())
            return;
        if (!damager.hasPermission(racePerm))
            return;
        if (!(event.getEntity() instanceof LivingEntity entity))
            return;
        entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 120, 1, false, true));
    }

    @EventHandler
    public void onUseHorn(PlayerInteractEvent event) {
        if (event.getPlayer().isOp())
            return;
        if (!event.getPlayer().hasPermission(racePerm))
            return;
        EquipmentSlot slot = event.getHand();
        if (!EquipmentSlot.HAND.equals(slot))
            return;
        ItemStack item = event.getItem();
        if (item == null)
            return;
        if (!Material.GOAT_HORN.equals(item.getType()))
            return;
        if (event.getPlayer().hasCooldown(item.getType()))
            return;
        int cd = OasisRace.getPluginConfig().getInt("naga.horn_cd_tick", 600);
        event.getPlayer().setCooldown(item.getType(), cd);
        int radius = OasisRace.getPluginConfig().getInt("naga.control_radius", 16);
        for (Entity entity : event.getPlayer().getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof LivingEntity livingEntity))
                continue;
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3, true, false));
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, true, false));
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 0, true, false));
        }
    }

}
