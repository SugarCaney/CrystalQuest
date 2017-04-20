package nl.sugcube.crystalquest.events;


import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called when the arenas changes from pre-game to in-game.
 *
 * @author SugarCaney
 */
public class ArenaStartEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    public boolean cancelled;

    public Arena arena;

    /**
     * @return (Arena) The arenas which started
     */
    public Arena getArena() {
        return this.arena;
    }

    /**
     * Constructor.
     *
     * @param a
     *         (Arena) The current arenas
     */
    public ArenaStartEvent(Arena a) {
        this.arena = a;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
