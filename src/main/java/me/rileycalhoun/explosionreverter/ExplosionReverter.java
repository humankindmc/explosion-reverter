package me.rileycalhoun.explosionreverter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ExplosionReverter extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        long startTime = System.nanoTime();
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
                        , 20L * 30
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
