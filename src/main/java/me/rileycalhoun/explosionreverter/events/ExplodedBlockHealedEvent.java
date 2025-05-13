package me.rileycalhoun.explosionreverter.events;

import me.rileycalhoun.explosionreverter.explosions.ReplaceableBlock;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ExplodedBlockHealedEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final ReplaceableBlock replaceableBlock;
    private boolean cancelled;

    public ExplodedBlockHealedEvent(ReplaceableBlock replaceableBlock) {
        this.replaceableBlock = replaceableBlock;
    }

    public ReplaceableBlock getReplaceableBlock() {
        return replaceableBlock;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
