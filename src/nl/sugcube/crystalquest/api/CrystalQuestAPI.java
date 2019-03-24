package nl.sugcube.crystalquest.api;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.economy.Economy;
import nl.sugcube.crystalquest.game.ArenaManager;
import nl.sugcube.crystalquest.items.ItemHandler;
import org.bukkit.Location;

/**
 * @author SugarCaney
 */
public class CrystalQuestAPI {

    public static CrystalQuest plugin;

    public static void setPlugin(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Gets the class managing all what has to do with the
     * shop-system. Get the balances of the players, open menus
     * and do what you want :)
     *
     * @return (Economy)
     */
    public static Economy getEconomy() {
        return plugin.economy;
    }

    /**
     * Checks if the given location is protected
     *
     * @param loc
     *         (Location) The location to check for
     * @return (boolean) True if within, false if not
     */
    public static boolean isInProtectedArena(Location loc) {
        return plugin.protection.isInProtectedArena(loc);
    }

    /**
     * Gets the class managing the items
     *
     * @return (ItemHandler)
     */
    public static ItemHandler getItemHandler() {
        return plugin.itemHandler;
    }

    /**
     * Gets the class handling the arenas
     *
     * @return (ArenaManager)
     */
    public static ArenaManager getArenaManager() {
        return plugin.getArenaManager();
    }

}