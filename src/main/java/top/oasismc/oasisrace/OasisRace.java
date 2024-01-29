package top.oasismc.oasisrace;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import top.oasismc.oasisrace.race.aquatic.Aquatic;
import top.oasismc.oasisrace.race.aquatic.LakeFairy;
import top.oasismc.oasisrace.race.aquatic.Naga;
import top.oasismc.oasisrace.race.human.Farming;
import top.oasismc.oasisrace.race.human.Human;
import top.oasismc.oasisrace.race.human.Nomadic;
import top.oasismc.oasisrace.race.nature.Death;
import top.oasismc.oasisrace.race.nature.Health;
import top.oasismc.oasisrace.race.nature.Nature;
import top.oasismc.oasisrace.race.undead.Dominator;
import top.oasismc.oasisrace.race.undead.UnDead;
import top.oasismc.oasisrace.race.undead.Vampire;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class OasisRace extends JavaPlugin {

    private final Map<String, BukkitRunnable> runnableMap;
    public static JavaPlugin INSTANCE;

    public OasisRace() {
        this.runnableMap = new ConcurrentHashMap<>();
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadRaces();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void loadRaces() {
        regRunnable("system", RaceEffectManager.INSTANCE, 20);

        regListener(UnDead.INSTANCE);
        regRunnable("undead", UnDead.INSTANCE, 25);
        regListener(Vampire.INSTANCE);
        regListener(Dominator.INSTANCE);

        regListener(Human.INSTANCE);
        regListener(Farming.INSTANCE);
        regRunnable("farming", Farming.INSTANCE, 20);
        regListener(Nomadic.INSTANCE);
        regRunnable("nomadic", Nomadic.INSTANCE, 22);

        regListener(Nature.INSTANCE);
        regRunnable("nature", Nature.INSTANCE, 45);
        regListener(Health.INSTANCE);
        regListener(Death.INSTANCE);

        regListener(Aquatic.INSTANCE);
        regListener(Naga.INSTANCE);
        regRunnable("lake_fairy", LakeFairy.INSTANCE, 30);
    }

    public void regListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void regCommand(String command, TabExecutor tabExecutor) {
        PluginCommand pluginCommand = Bukkit.getPluginCommand(command);
        if (pluginCommand == null)
            return;
        pluginCommand.setExecutor(tabExecutor);
        pluginCommand.setTabCompleter(tabExecutor);
    }

    public void regRunnable(String race, BukkitRunnable runnable, long period) {
        if (runnableMap.containsKey(race))
            throw new IllegalArgumentException("Race " + race + " has already registered a resident thread");
        runnable.runTaskTimer(this, 0, period);
        runnableMap.put(race, runnable);
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        reloadConfig();
        return super.getConfig();
    }

    public static FileConfiguration getPluginConfig() {
        return INSTANCE.getConfig();
    }

}
