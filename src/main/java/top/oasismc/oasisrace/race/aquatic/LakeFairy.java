package top.oasismc.oasisrace.race.aquatic;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.oasisrace.OasisRace;
import top.oasismc.oasisrace.RaceEffectManager;

public class LakeFairy extends BukkitRunnable implements Listener {

    public static final LakeFairy INSTANCE = new LakeFairy();

    private final String racePerm, raceName;

    private LakeFairy() {
        raceName = OasisRace.getPluginConfig().getString("lake_fairy.team", "lake_fairy");
        racePerm = OasisRace.getPluginConfig().getString("lake_fairy.perm", "oasis.race.aquatic.fairy");
        RaceEffectManager.INSTANCE.regRaceEffect(racePerm, new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 40, 0, true, false));
        RaceEffectManager.INSTANCE.regRaceEffect(racePerm, new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0, true, false));
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp())
                continue;
            if (!player.hasPermission(racePerm))
                continue;
            if (player.isInWater()) {
                giveWaterEffect(player);
                continue;
            }
            World world = player.getWorld();
            if (!world.isClearWeather()) {
                if (player.getLocation().getBlock().getLightFromSky() == 15)
                    giveWaterEffect(player);
                else
                    giveLandEffect(player);
            } else
                giveLandEffect(player);
        }
    }

    private void giveWaterEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 0, true, false));
    }

    private void giveLandEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 0, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 0, true, false));
    }

}
