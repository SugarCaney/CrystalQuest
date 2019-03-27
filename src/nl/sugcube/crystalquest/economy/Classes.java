package nl.sugcube.crystalquest.economy;

import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author SugarCaney
 */
public interface Classes {

    /**
     * Checks whether a given player has the class with the given class id.
     *
     * @return true if the player has the class, false otherwise.
     */
    boolean hasClass(Player player, String classId);

    /**
     * Marks the given class id as being purchased by the player.
     */
    void registerClass(Player player, String classId);

    /**
     * Get all the class ids of the classes that the player has bought.
     */
    Set<String> getAllClasses(Player player);
}
