package top.oasismc.oasisrace.race.undead;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import top.oasismc.oasisrace.OasisRace;
import top.oasismc.oasisrace.RaceEffectManager;

public enum Vampire implements Listener {

    INSTANCE;
    
    private final String raceName;
    private final String racePerm;

    Vampire() {
        raceName = OasisRace.getPluginConfig().getString("vampire.team", "Vampire");
        racePerm = OasisRace.getPluginConfig().getString("vampire.perm", "oasis.race.undead.vampire");
        RaceEffectManager.INSTANCE.regRaceEffect(racePerm, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 50, 0, true, false));
    }

    @EventHandler
    public void onHurtEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        double scale = OasisRace.getPluginConfig().getDouble("vampire.suck_proportion", 0.3);
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + event.getFinalDamage() * scale));
    }

    @EventHandler
    public void onGetHungryEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        if (!event.getAction().equals(EntityPotionEffectEvent.Action.ADDED))
            return;
        if (!event.getModifiedType().equals(PotionEffectType.HUNGER))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        ItemStack item = event.getItem();
        if (item == null)
            return;
        if (!OasisRace.getPluginConfig().getStringList("vampire.allow_eat_food").contains(item.getType().name()))
            event.setCancelled(true);
    }

    public String getRaceName() {
        return raceName;
    }

}
