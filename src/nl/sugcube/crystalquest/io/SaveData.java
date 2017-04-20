package nl.sugcube.crystalquest.io;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import nl.sugcube.crystalquest.sba.SMeth;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SugarCaney
 */
public class SaveData {

    public static CrystalQuest plugin;

    public SaveData(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Saves all the "arenas-join-signs" to the data.yml
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
     * Saves all the arenas-data to the data.yml
     */
    public static void saveArenas() {
        FileConfiguration data = plugin.getData();

        for (Arena arena : plugin.am.arenas) {
            String pfx = "arena." + arena.getId() + ".";
            data.set(pfx + "name", arena.getName());
            data.set(pfx + "teams", arena.getTeams().stream()
                    .map(CrystalQuestTeam::getName)
                    .collect(Collectors.toList()));
            data.set(pfx + "min-players", arena.getMinPlayers());
            data.set(pfx + "max-players", arena.getMaxPlayers());

            data.set(pfx + "team-lobby", null);
            for (CrystalQuestTeam team : arena.getTeams()) {
                Location lobby = arena.getLobbySpawn(team);
                data.set(pfx + "team-lobby." + team.getName(), SMeth.toLocationString(lobby));
            }

            data.set(pfx + "state", arena.isEnabled());
            data.set(pfx + "player-spawns", toStringList(arena.getPlayerSpawns()));
            data.set(pfx + "crystal-spawns", toStringList(arena.getCrystalSpawns()));
            data.set(pfx + "item-spawns", toStringList(arena.getItemSpawns()));
            data.set(pfx + "double-jump", arena.canDoubleJump());

            if (arena.getProtection() != null) {
                if (arena.getProtection()[0] != null && arena.getProtection()[1] != null) {
                    List<String> list = new ArrayList<>();
                    list.add(SMeth.toLocationString(arena.getProtection()[0]));
                    list.add(SMeth.toLocationString(arena.getProtection()[1]));
                    data.set(pfx + "protection", list);
                }
                else {
                    data.set(pfx + "protection", null);
                }
            }
            else {
                data.set(pfx + "protection", null);
            }

            for (CrystalQuestTeam team : CrystalQuestTeam.getTeams()) {
                List<Location> teamSpawns = arena.getTeamSpawns(team);
                if (teamSpawns == null) {
                    data.set(pfx + "team-spawns." + team.getName(), null);
                    continue;
                }

                List<String> teamSpawnsStrings = teamSpawns.stream()
                        .map(SMeth::toLocationString)
                        .collect(Collectors.toList());

                data.set(pfx + "team-spawns." + team.getName(), teamSpawnsStrings);
            }

            // Backwards compatibility cleanup (v1.3 and below).
            compat_cleanup_v1_3(pfx);
        }

        plugin.saveData();
    }

    /**
     * Cleans up old team-spawns storage format.
     * <p>
     * Pre v1.3: Team spawn lists were stored by their numerical id.<br>
     * Post v1.3: Team spawn lists are stored by their name {@link CrystalQuestTeam#name}.
     */
    private static void compat_cleanup_v1_3(String prefix) {
        for (int i = 0; i < 8; i++) {
            plugin.getData().set(prefix + "team-spawns." + i, null);
        }
    }

    /**
     * Converts a Location-array into a Stringarray to prepare it for data-storage.
     *
     * @param list
     *         (Location[]) Array of Locations
     * @return (StringList) The list containing the location-strings
     */
    public static List<String> toStringList(Location[] list) {
        List<String> stringList = new ArrayList<>();
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
        List<String> stringList = new ArrayList<>();
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
        List<String> stringList = new ArrayList<>();
        for (Sign s : list) {
            if (s != null) {
                Location loc = s.getLocation();
                stringList.add(SMeth.toLocationStringSign(loc));
            }
        }
        return stringList;
    }

}