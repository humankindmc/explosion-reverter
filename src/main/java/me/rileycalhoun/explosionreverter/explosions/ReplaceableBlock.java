package me.rileycalhoun.explosionreverter.explosions;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public class ReplaceableBlock {

    @NotNull
    private final Location location;

    @NotNull
    private final BlockData realBlockData;

    private ReplaceableBlock(
            @NotNull final Location location,
            @NotNull final BlockData realBlockData
    ) {
        this.location = location;
        this.realBlockData = realBlockData;
    }

    public static ReplaceableBlock newBlock(@NotNull final BlockState state) {
        return new ReplaceableBlock(state.getLocation(), state.getBlockData());
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    @NotNull
    public BlockData getRealBlockData() {
        return realBlockData;
    }
}
