package me.rileycalhoun.explosionreverter;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.rileycalhoun.explosionreverter.command.ExplosionReverterCommand;
import me.rileycalhoun.explosionreverter.explosions.ExplodedBlockManager;
import me.rileycalhoun.explosionreverter.listeners.BlockExplosionListener;
import me.rileycalhoun.explosionreverter.util.ConfigPath;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class ExplosionReverter extends JavaPlugin {

    private YamlDocument configFile;
    private ExplodedBlockManager explodedBlockManager;

    private static long initialDelay, loopDelay;

    @Override
    public void onEnable() {
        long startTime = System.nanoTime();
        getLogger().info("Initializing config files...");

        try {
            this.configFile = YamlDocument.create(
                    new File(getDataFolder(), "config.yml"),
                    Objects.requireNonNull(getClass().getResourceAsStream("/config/config.yml")),
                    GeneralSettings.DEFAULT,
                    UpdaterSettings.builder()
                            .setVersioning(new BasicVersioning("version"))
                            .build(),
                    LoaderSettings.builder()
                            .setAutoUpdate(true)
                            .build(),
                    DumperSettings.DEFAULT
            );

            configFile.save();
            configFile.update();
        } catch (IOException e) {
            getLogger().severe("Shutting down plugin. Could not load config: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        initialDelay = configFile.getLong(ConfigPath.REVERSION_INITIAL_DELAY);
        loopDelay = configFile.getLong(ConfigPath.REVERSION_LOOP_DELAY);

        getLogger().info("Initializing managers...");
        this.explodedBlockManager = new ExplodedBlockManager(this);

        getLogger().info("Registering listeners...");
        getServer().getPluginManager().registerEvents(new BlockExplosionListener(explodedBlockManager), this);

        getLogger().info("Registering commands...");
        PluginCommand command = getCommand("explosionreverter");
        assert command != null;
        command.setExecutor(new ExplosionReverterCommand(this));


        long duration = (System.nanoTime() - startTime) / 1_000_000;
        getLogger().info("Done! Enabled in " + duration + "ms.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving config.yml...");
        try {
            configFile.save();
        } catch (IOException e) {
            getLogger().severe("Could not save config.yml: " + e.getMessage());
        }

        getLogger().info("Replacing all exploded blocks...");
        getExplodedBlockManager().cancelTask();
        getExplodedBlockManager().eachReplaceAll();

        getLogger().info("ExplosionReverter has been disabled!");
    }

    public ExplodedBlockManager getExplodedBlockManager() {
        return explodedBlockManager;
    }

    public static long getInitialDelay() {
        return initialDelay;
    }

    public static long getLoopDelay() {
        return loopDelay;
    }

    @Override
    public void reloadConfig() {
        try {
            configFile.reload();
        } catch (IOException e) {
            getLogger().severe("Issue while reloading config: " + e.getMessage());
            return;
        }

        initialDelay = configFile.getLong(ConfigPath.REVERSION_INITIAL_DELAY);
        loopDelay = configFile.getLong(ConfigPath.REVERSION_LOOP_DELAY);

        getExplodedBlockManager().rescheduleTask();
    }

}
