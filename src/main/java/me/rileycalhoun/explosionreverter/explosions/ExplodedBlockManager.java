package me.rileycalhoun.explosionreverter.explosions;

import me.rileycalhoun.explosionreverter.ExplosionReverter;
import me.rileycalhoun.explosionreverter.events.ExplosionRevertedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ExplodedBlockManager {

    private final ExplosionReverter plugin;
    private List<Explosion> explosions;
    private BukkitTask task;

    public ExplodedBlockManager(ExplosionReverter plugin) {
        this.plugin = plugin;
        this.explosions = new ArrayList<>();
        scheduleTask();
    }


    public void processExplosion(EntityExplodeEvent event) {
        event.setYield(0.0f);
        ExplosionCause cause = ExplosionCause.getExplosionCause(event.getEntity());
        processExplosion(event.blockList(), cause);
    }

    public void processExplosion(BlockExplodeEvent event) {
        event.setYield(0.0f);
        processExplosion(event.blockList(), ExplosionCause.OTHER);
    }

    private void processExplosion(List<Block> blockList, ExplosionCause cause) {
        List<Block> sorted = new ArrayList<>(blockList);
        sorted.sort(Comparator.comparingInt(Block::getY));

        LinkedList<ReplaceableBlock> processList = new LinkedList<>();
        for (Block block : sorted) {
            if (block.getType() == Material.TNT)
                continue;

            ReplaceableBlock replaceable = ReplaceableBlock.newBlock(block.getState());
            processList.add(replaceable);
        }

        ExplosionRevertedEvent event = new ExplosionRevertedEvent(processList, cause);
        plugin.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        Explosion explosion = new Explosion(processList, cause);
        explosions.add(explosion);
    }

    private void eachReplaceOne() {
        // Iterator SHOULD take care of ConcurrentModiciatonException
        Iterator<Explosion> iterator = explosions.iterator();
        while (iterator.hasNext()) {
            Explosion explosion = iterator.next();

            if (explosion.isEmpty()) {
                iterator.remove();
                continue;
            }

            explosion.replaceOneBlock(false);
        }
    }

    public void eachReplaceAll() {
        Iterator<Explosion> iterator = explosions.iterator();
        while(iterator.hasNext()) {
            Explosion explosion = iterator.next();
            explosion.replaceAllBlocks();
            iterator.remove();
        }
    }

    private void scheduleTask() {
        task = Bukkit.getServer().getScheduler().runTaskTimer(
                plugin,
                this::eachReplaceOne,
                0,
                20L * ExplosionReverter.getLoopDelay()
        );
    }

    public void cancelTask() {
        task.cancel();
    }

    public void rescheduleTask() {
        cancelTask();
        scheduleTask();
    }

}
