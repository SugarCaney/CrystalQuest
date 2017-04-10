package nl.sugcube.crystalquest.io;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.sba.SMeth;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SugarCaney
 */
public class SaveData {

    public static CrystalQuest plugin;

    public SaveData(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Saves all the "arena-join-signs" to the data.yml
     */
    public static void saveSigns() {
        FileConfiguration data = plugin.getData();

        data.set("signs", toStringList(plugin.signHandler.getSigns()));

        plugin.saveData();
    }

    /**
     * Saves the lobby-spawn
     */
    public static void saveLobbySpawn() {
        try {
            plugin.getData().set("lobby-spawn", SMeth.toLocationString(plugin.am.getLobby()));
            plugin.saveData();
        }
        catch (Exception ignored) {
        }
    }

    /**
     * Saves all the arena-data to the data.yml
     */
    public static void saveArenas() {
        FileConfiguration data = plugin.getData();

        for (Arena a : plugin.am.arena) {
            String pfx = "arena." + a.getId() + ".";
            data.set(pfx + "name", a.getName());
            data.set(pfx + "teams", a.getTeamCount());
            data.set(pfx + "min-players", a.getMinPlayers());
            data.set(pfx + "max-players", a.getMaxPlayers());
            data.set(pfx + "team-lobby", toStringList(a.getLobbySpawns()));
            data.set(pfx + "state", a.isEnabled());
            data.set(pfx + "player-spawns", toStringList(a.getPlayerSpawns()));
            data.set(pfx + "crystal-spawns", toStringList(a.getCrystalSpawns()));
            data.set(pfx + "item-spawns", toStringList(a.getItemSpawns()));
            data.set(pfx + "double-jump", a.canDoubleJump());

            if (a.getProtection() != null) {
                if (a.getProtection()[0] != null && a.getProtection()[1] != null) {
                    List<String> list = new ArrayList<String>();
                    list.add(SMeth.toLocationString(a.getProtection()[0]));
                    list.add(SMeth.toLocationString(a.getProtection()[1]));
                    data.set(pfx + "protection", list);
                }
                else {
                    data.set(pfx + "protection", null);
                }
            }
            else {
                data.set(pfx + "protection", null);
            }

            for (int i = 0; i < 8; i++) {
                if (a.getTeamSpawns().get(i).size() > 0) {
                    List<String> list = new ArrayList<String>();
                    for (Location loc : a.getTeamSpawns().get(i)) {
                        list.add(SMeth.toLocationString(loc));
                    }
                    data.set(pfx + "team-spawns." + i, list);
                }
                else {
                    data.set(pfx + "team-spawns." + i, null);
                }
            }
        }

        plugin.saveData();
    }

    /**
     * Converts a Location-array into a Stringarray to prepare it for data-storage.
     *
     * @param list
     *         (Location[]) Array of Locations
     * @return (StringList) The list containing the location-strings
     */
    public static List<String> toStringList(Location[] list) {
        List<String> stringList = new ArrayList<String>();
        if (list != null) {
            for (Location loc : list) {
                if (loc != null) {
                    stringList.add(SMeth.toLocationString(loc));
                }
            }
        }
        return stringList;
    }

    /**
     * Converts a Location-list into a Stringarray to prepare it for data-storage.
     *
     * @param list
     *         (LocationList) List of Locations
     * @return (StringList) The list containing the location-strings
     */
    public static List<String> toStringList(List<Location> list) {
        List<String> stringList = new ArrayList<String>();
        for (Location loc : list) {
            stringList.add(SMeth.toLocationString(loc));
        }
        return stringList;
    }

    /**
     * Converts a Sign-list into a Stringarray to prepare it for data-storage.
     *
     * @param list
     *         (SignList) List of Locations
     * @return (StringList) The list containing the location-strings
     */
    public static List<String> toStringListSign(List<Sign> list) {
        List<String> stringList = new ArrayList<String>();
        for (Sign s : list) {
            if (s != null) {
                Location loc = s.getLocation();
                stringList.add(SMeth.toLocationStringSign(loc));
            }
        }
        return stringList;
    }

}