package nl.sugcube.crystalquest.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class BukkitUtil {

    /**
     * Get the online player with the given player name.
     *
     * @param name
     *         The name of the player to find.
     * @return The online player with the given name, or {@code null} when there is no player
     * online with the given name.
     */
    public static Player getPlayerByName(String name) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> ((Player)player).getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private BukkitUtil() {
    }
}
