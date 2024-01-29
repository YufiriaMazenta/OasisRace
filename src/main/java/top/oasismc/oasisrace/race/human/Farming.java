package top.oasismc.oasisrace.race.human;

import org.bukkit.Bukkit;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.oasisrace.OasisRace;

public class Farming extends BukkitRunnable implements Listener {

    public static final Farming INSTANCE = new Farming();
    private final String raceName;
    private final String racePerm;

    private Farming() {
        raceName = OasisRace.getPluginConfig().getString("farming.team");
        racePerm = OasisRace.getPluginConfig().getString("farming.perm", "oasis.race.undead.farming");
    }

    @EventHandler
    public void onHarvestCrops(BlockDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.isOp())
            return;
        if (!player.hasPermission(racePerm))
            return;
        BlockData blockData = event.getBlockState().getBlockData();
        if (!(blockData instanceof Ageable crops))
            return;
        if (crops.getAge() >= crops.getMaximumAge()) {
            for (Item item : event.getItems()) {
                ItemStack itemStack = item.getItemStack();
                itemStack.setAmount(itemStack.getAmount() + OasisRace.getPluginConfig().getInt("farming.extra_crops", 1));
                item.setItemStack(itemStack);
            }
            player.giveExp(OasisRace.getPluginConfig().getInt("farming.crop_exp", 1));
        }
    }

    @EventHandler
    public void onCrafting(PrepareItemCraftEvent event) {
        if (event.getViewers().size() > 1)
            return;
        if (event.getViewers().get(0).isOp())
            return;
        if (!event.getViewers().get(0).hasPermission(racePerm))
            return;
        if (!(event.getRecipe() instanceof ShapelessRecipe recipe))
            return;
        String key = recipe.getKey().getKey();
        if (key.contains("planks")) {
            ItemStack result = event.getInventory().getResult();
            if (result == null)
                return;
            result.setAmount(result.getAmount() + OasisRace.getPluginConfig().getInt("farming.extra_planks", 1));
            event.getInventory().setResult(result);
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp())
                continue;
            if (!player.hasPermission(racePerm))
                continue;
            int humanNum = 0;
            int radius = OasisRace.getPluginConfig().getInt("farming.friend_radius", 25);
            for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                if (entity.getType().equals(EntityType.PLAYER) || entity.getType().equals(EntityType.VILLAGER)) {
                    humanNum ++;
                }
            }
            if (humanNum >= 3) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 0, true, false));
            }
            if (humanNum >= 5) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 0, true, false));
            }
            if (humanNum >= 7) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 0, true, false));
            }
        }
    }

}
