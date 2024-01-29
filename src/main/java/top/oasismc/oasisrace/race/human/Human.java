package top.oasismc.oasisrace.race.human;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import top.oasismc.oasisrace.OasisRace;

public enum Human implements Listener {

    INSTANCE;

    @EventHandler
    public void onGetExp(PlayerExpChangeEvent event) {
        if (event.getPlayer().isOp())
            return;
        if (!event.getPlayer().hasPermission(OasisRace.getPluginConfig().getString("human.perm", "oasis.race.human")))
            return;
        if (event.getAmount() < 0)
            return;
        event.setAmount((int) (event.getAmount() * (1 + OasisRace.getPluginConfig().getDouble("human.extra_exp_scale", 0.1))));
    }

}
