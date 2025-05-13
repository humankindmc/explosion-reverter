package me.rileycalhoun.explosionreverter;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ExplosionReverter extends JavaPlugin implements Listener {

    private int revertAfter;

    @Override
    public void onEnable() {
        long startTime = System.nanoTime();
        getLogger().info("Initializing config files...");

        YamlDocument configFile;
        try {
            configFile = YamlDocument.create(
                    new File(getDataFolder(), ""),
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
        } catch (IOException e) {
            getLogger().severe("Shutting down plugin. Could not load config: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        revertAfter = configFile.getInt("revert-after");

        getLogger().info("Registering listeners...");
        getServer().getPluginManager().registerEvents(this, this);

        long duration = (System.nanoTime() - startTime) / 1_000_000;
        getLogger().info("Done! Enabled in " + duration + "ms.");
    }

    @EventHandler
    public void onEntityExplosion(EntityExplodeEvent event) {
        scheduleRebuild(event.blockList());
    }

    @EventHandler
    public void onBlockExplosion(BlockExplodeEvent event) {
        scheduleRebuild(event.blockList());
    }

    private void scheduleRebuild(List<Block> blockList) {
        getServer()
                .getScheduler()
                .runTaskLaterAsynchronously(
                        this,
                        () -> rebuildExplosion(blockList)
                        , 20L * revertAfter
                );
    }

    private void rebuildExplosion(List<Block> blockList) {
        for (Block oldBlock : blockList) {
            Location location = oldBlock.getLocation();
            Block currentBlock = location.getBlock();
            if (currentBlock.getType() == Material.AIR) {
                currentBlock.setType(oldBlock.getType());
            }
        }
    }

}
