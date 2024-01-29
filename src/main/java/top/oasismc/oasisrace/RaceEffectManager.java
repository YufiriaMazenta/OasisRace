package top.oasismc.oasisrace;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RaceEffectManager extends BukkitRunnable {

    public static RaceEffectManager INSTANCE = new RaceEffectManager();
    private final Map<String, List<PotionEffect>> raceEffectMap;

    private RaceEffectManager() {
        raceEffectMap = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp())
                continue;
            for (String perm : raceEffectMap.keySet()) {
                if (player.hasPermission(perm)) {
                    player.addPotionEffects(raceEffectMap.get(perm));
                }
            }
        }
    }

    public void regRaceEffect(String racePerm, PotionEffect effect) {
        if (!raceEffectMap.containsKey(racePerm))
            raceEffectMap.put(racePerm, new ArrayList<>());
        raceEffectMap.get(racePerm).add(effect);
    }

}
