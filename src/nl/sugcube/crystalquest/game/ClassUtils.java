package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author SugarCaney
 */
public class ClassUtils {

    public static CrystalQuest plugin;

    public ClassUtils(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Checks if a player has permission to pick a certain class.
     *
     * @param player
     *         The player to check for.
     * @param kit
     *         The technical (config-section) name of the class.
     * @return  True if they have permission to use the class, false if not.
     */
    public static boolean hasPermission(Player player, String kit) {
        //Checks if the class is Arena-bounded.
        if (plugin.getArenaManager().isInGame(player)) {
            Arena a = plugin.getArenaManager().getArena(player.getUniqueId());
            String arenaName = a.getName();
            if (plugin.getConfig().isSet("kit." + kit + ".arenas")) {
                List<String> arenas = plugin.getConfig().getStringList("kit." + kit + ".arenas");

                //Checks if the list does contain only of "negative"-arenas.
                boolean isOnlyIgnore = true;

                for (String str : arenas) {
                    if (!str.startsWith("-")) {
                        isOnlyIgnore = false;
                    }
                }

                if (!isOnlyIgnore) {
                    //Fires when there are normal nodes avaiable
                    for (String s : arenas) {
                        if (s.equalsIgnoreCase("-" + arenaName)) {
                            return false;
                        }
                        if (s.equalsIgnoreCase(arenaName)) {
                            return true;
                        }
                    }

                    return false;
                }
                else {
                    //Fires when only "negative"-arenas are avaiable for the class
                    for (String s : arenas) {
                        if (s.equalsIgnoreCase("-" + arenaName)) {
                            return false;
                        }
                    }
                }
            }
        }

        //Checks if a player has the permission node for the class.
        if (player.hasPermission("crystalquest.kit." + kit) || player.hasPermission("crystalquest.admin") ||
                player.hasPermission("crystalquest.staff") || player.hasPermission("crystalquest.kit.*")) {
            return true;
        }

        return plugin.economy.getClasses().hasClass(player, kit);
    }
}