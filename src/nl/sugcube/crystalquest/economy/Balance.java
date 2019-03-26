package nl.sugcube.crystalquest.economy;

import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public interface Balance {

    /**
     * Sets the balance of a certain player.
     *
     * @param player
     *         The player you want to set the balance for.
     * @param newBalance
     *         The new balance (greather than zero).
     */
    void setBalance(Player player, int newBalance);

    /**
     * Adds or removes an amount of crystals to/from someone's balance.
     *
     * @param player
     *         The player to edit the balance.
     * @param amount
     *         The amount of crystals to add (negative number to remove).
     * @param showMessage
     *         When true, a notification will be sent to the player.
     */
    void addBalance(Player player, int amount, boolean showMessage);

    /**
     * Checks if the player can afford something.
     *
     * @param player
     *         The player you want to check for.
     * @param amount
     *         The amount of money to spend.
     * @return True if so, false if not.
     */
    default boolean canAfford(Player player, int amount) {
        return getBalance(player, false) >= amount;
    }

    /**
     * Gets the amount of crystals of the player.
     *
     * @param player
     *         The player you want to check for.
     * @return The amount of crystals a player got.
     */
    int getBalance(Player player, boolean createAccount);
}