package nl.sugcube.crystalquest.events;

import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called when a player earns crystals ingame by
 * either killing players or by winning the game (currency).
 *
 * @author SugarCaney
 */
public class PlayerEarnCrystalsEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private Arena arena;
    private Player player;
    private int amount;
    private boolean showMessage = true;

    public PlayerEarnCrystalsEvent(Player player, Arena arena, int amount) {
        this.arena = arena;
        this.player = player;
        this.amount = amount;
    }

    /**
     * Hides the message that shows the amount of crystals
     * you earned.
     */
    public void hideMessage() {
        this.showMessage = false;
    }

    /**
     * Gets whether the crystalmessage has to be shown.
     */
    public boolean showMessage() {
        return this.showMessage;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Get the amount of crystals earned.
     *
     * @return (int)
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Set the amount of crystals earned.
     *
     * @param amount
     *         (int)
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Get the player who earned the crystals.
     *
     * @return (Player)
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the arenas in which the event occured.
     *
     * @return (Arena)
     */
    public Arena getArena() {
        return arena;
    }

}
