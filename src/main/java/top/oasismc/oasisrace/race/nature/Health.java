package top.oasismc.oasisrace.race.nature;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import top.oasismc.oasisrace.OasisRace;
import top.oasismc.oasisrace.RaceEffectManager;

public enum Health implements Listener {

    INSTANCE;

    private final String raceName;
    private final String racePerm;

    Health() {
        raceName = OasisRace.getPluginConfig().getString("health.team");
        racePerm = OasisRace.getPluginConfig().getString("health.perm", "oasis.race.nature.health");
        RaceEffectManager.INSTANCE.regRaceEffect(racePerm, new PotionEffect(PotionEffectType.REGENERATION, 50, 1, true, false));
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
        if (!OasisRace.getPluginConfig().getStringList("health.allow_eat_food").contains(item.getType().name()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        event.setAmount(event.getAmount() * (1 + OasisRace.getPluginConfig().getDouble("health.extra_heal_scale", 0.25)));
    }


}
