package nl.sugcube.crystalquest.economy;

import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public interface Upgrades {

    /**
     * Gets the level a player has for a certain upgrade.
     *
     * @param player
     *         The player to check for.
     * @param upgrade
     *         The upgrade's name.
     * @return The level of the player for a certain upgrade.
     */
    int getLevel(Player player, ShopUpgrade upgrade);

    /**
     * Sets the level a player has for a certain upgrade.
     *
     * @param player
     *          The player to set the upgrade level for.
     * @param upgrade
     *          The upgrade to update.
     * @param newLevel
     *          The new level the upgrade has for the player.
     */
    void setLevel(Player player, ShopUpgrade upgrade, int newLevel);

    /**
     * Adds 1 level to the current level `player` has for a given upgrade.
     */
    default void incrementLevel(Player player, ShopUpgrade upgrade) {
        int newLevel = getLevel(player, upgrade);
        setLevel(player, upgrade, newLevel);
    }
}