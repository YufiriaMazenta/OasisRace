package top.oasismc.oasisrace.race.nature;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.oasisrace.OasisRace;

import java.util.ArrayList;
import java.util.List;

public class Nature extends BukkitRunnable implements Listener {

    public static final Nature INSTANCE = new Nature();

    private final String racePerm;
    private final List<Biome> forestBiomes;

    private Nature() {
        racePerm = OasisRace.getPluginConfig().getString("nature.perm", "oasis.race.nature");
        forestBiomes = new ArrayList<>();
        loadBiomes();
    }

    @EventHandler
    public void onEatNatureFood(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        ItemStack item = event.getItem();
        if (item == null)
            return;
        if (OasisRace.getPluginConfig().getStringList("nature.heal_effect_food").contains(item.getType().name()))
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 60, 0, false, true));
    }


    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp())
                continue;
            if (!player.hasPermission(racePerm))
                continue;
            Biome biome = player.getLocation().getBlock().getBiome();
            if (forestBiomes.contains(biome)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50, 1, true, false));
            }
        }
    }

    private void loadBiomes() {
        forestBiomes.add(Biome.FOREST);
        forestBiomes.add(Biome.BIRCH_FOREST);
        forestBiomes.add(Biome.DARK_FOREST);
        forestBiomes.add(Biome.FLOWER_FOREST);
        forestBiomes.add(Biome.TAIGA);
        forestBiomes.add(Biome.SNOWY_TAIGA);
        forestBiomes.add(Biome.JUNGLE);
        forestBiomes.add(Biome.BAMBOO_JUNGLE);
        forestBiomes.add(Biome.SPARSE_JUNGLE);
        forestBiomes.add(Biome.OLD_GROWTH_BIRCH_FOREST);
        forestBiomes.add(Biome.OLD_GROWTH_PINE_TAIGA);
        forestBiomes.add(Biome.OLD_GROWTH_SPRUCE_TAIGA);
        forestBiomes.add(Biome.WINDSWEPT_FOREST);
        forestBiomes.add(Biome.MANGROVE_SWAMP);
        forestBiomes.add(Biome.CRIMSON_FOREST);
        forestBiomes.add(Biome.WARPED_FOREST);
    }

}
