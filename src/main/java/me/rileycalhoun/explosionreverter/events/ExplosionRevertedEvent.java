package me.rileycalhoun.explosionreverter.events;

import me.rileycalhoun.explosionreverter.explosions.ExplosionCause;
import me.rileycalhoun.explosionreverter.explosions.ReplaceableBlock;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class ExplosionRevertedEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NotNull
    private final LinkedList<ReplaceableBlock> processList;

    @NotNull
    private final ExplosionCause cause;

    private boolean cancelled;

    public ExplosionRevertedEvent(@NotNull LinkedList<ReplaceableBlock> processList, @NotNull ExplosionCause cause) {
        this.processList = processList;
        this.cause = cause;
        this.cancelled = false;
    }

    @NotNull
    public LinkedList<ReplaceableBlock> getProcessList() {
        return processList;
    }

    @NotNull
    public ExplosionCause getCause() {
        return cause;
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
