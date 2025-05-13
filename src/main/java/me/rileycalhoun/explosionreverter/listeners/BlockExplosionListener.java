package me.rileycalhoun.explosionreverter.listeners;

import me.rileycalhoun.explosionreverter.explosions.ExplosionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockExplosionListener implements Listener {

    private final ExplosionManager explosionManager;

    public BlockExplosionListener(ExplosionManager explosionManager) {
        this.explosionManager = explosionManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        explosionManager.processExplosion(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockExplode(BlockExplodeEvent event) {
        explosionManager.processExplosion(event);
    }

}
