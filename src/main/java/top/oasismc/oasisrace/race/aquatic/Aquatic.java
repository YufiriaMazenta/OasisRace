package top.oasismc.oasisrace.race.aquatic;

import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import top.oasismc.oasisrace.OasisRace;
import top.oasismc.oasisrace.RaceEffectManager;

public enum Aquatic implements Listener {

    INSTANCE;
    private final String racePerm;

    Aquatic() {
        racePerm = OasisRace.getPluginConfig().getString("aquatic.perm", "oasis.race.aquatic");
        RaceEffectManager.INSTANCE.regRaceEffect(racePerm, new PotionEffect(PotionEffectType.CONDUIT_POWER, 40, 0, true, false));
    }



}
