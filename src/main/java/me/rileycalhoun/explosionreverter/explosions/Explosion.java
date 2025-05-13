package me.rileycalhoun.explosionreverter.explosions;

import me.rileycalhoun.explosionreverter.ExplosionReverter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.LinkedList;

public class Explosion {

    public final long createTime;
    public final LinkedList<ReplaceableBlock> processList;
    public final ExplosionCause cause;

    public Explosion(LinkedList<ReplaceableBlock> processList, ExplosionCause cause) {
        this.createTime = System.nanoTime();
        this.processList = processList;
        this.cause = cause;
    }

    public void replaceOneBlock(boolean force) {
        long currentTime = System.nanoTime();
        long durationSeconds = (currentTime - createTime) / 1_000_000_000;
        if (durationSeconds >= ExplosionReverter.getInitialDelay() || force) {
            if (processList.isEmpty()) return;
            ReplaceableBlock replaceable = processList.remove();
            BlockData blockData = replaceable.getRealBlockData();

            Location location = replaceable.getLocation();
            location.getChunk().load(true);

            Block block = location.getBlock();
            block.setBlockData(blockData, true);
        }
    }

    public void replaceAllBlocks() {
        while (!processList.isEmpty()) {
            replaceOneBlock(true);
        }
    }

    public boolean isEmpty() {
        return processList.isEmpty();
    }

}
