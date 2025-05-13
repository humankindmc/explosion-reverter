package me.rileycalhoun.explosionreverter.listeners;

import me.rileycalhoun.explosionreverter.explosions.ExplodedBlockManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockExplosionListener implements Listener {

    private final ExplodedBlockManager explodedBlockManager;

    public BlockExplosionListener(ExplodedBlockManager explodedBlockManager) {
        this.explodedBlockManager = explodedBlockManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        explodedBlockManager.processExplosion(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockExplode(BlockExplodeEvent event) {
        explodedBlockManager.processExplosion(event);
    }

}
