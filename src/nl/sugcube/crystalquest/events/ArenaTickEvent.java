package nl.sugcube.crystalquest.events;

import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called when the counter counts 1 second down.
 *
 * @author SugarCaney
 */
public class ArenaTickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public Arena arena;
    public int oldCount;
    public int newCount;
    public boolean inGame;

    /**
     * @return (boolean) True if it's in-game, False if it's pre-game
     */
    public boolean isInGameTick() {
        return this.inGame;
    }

    /**
     * @return (int) The clock after the tick
     */
    public int getNewCount() {
        return this.newCount;
    }

    /**
     * @return (int) The clock before the tick
     */
    public int getOldCount() {
        return this.oldCount;
    }

    /**
     * @return (Arena) The arena which is counting down
     */
    public Arena getArena() {
        return this.arena;
    }

    /**
     * Constructor.
     *
     * @param a
     *         (Arena) The current arena
     * @param o
     *         (int) The clock before the tick
     * @param n
     *         (int) The clock after the tick
     * @param ig
     *         (boolean) True if it is a result in-game, false if the arena is pre-game.
     */
    public ArenaTickEvent(Arena a, int o, int n, boolean ig) {
        this.arena = a;
        this.oldCount = o;
        this.newCount = n;
        this.inGame = ig;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
