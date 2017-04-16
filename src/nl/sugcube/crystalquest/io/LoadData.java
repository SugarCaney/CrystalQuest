package nl.sugcube.crystalquest.io;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.ArenaManager;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import nl.sugcube.crystalquest.sba.SMeth;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author SugarCaney
 */
public class LoadData {

    public static CrystalQuest plugin;

    public LoadData(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Loads the lobby-signs
     */
    public static void loadSigns() {
        plugin.signHandler.getSigns().clear();
        for (String s : plugin.getData().getStringList("signs")) {
            Location loc = SMeth.toLocation(s);
            if (loc.getBlock().getType() == Material.WALL_SIGN || loc.getBlock().getType() == Material.SIGN_POST) {
                plugin.signHandler.getSigns().add(loc);
            }
        }
        plugin.signHandler.updateSigns();
    }

    /**
     * Loads the lobby-spawn
     */
    public static void loadLobbySpawn() {
        if (plugin.getData().getString("lobby-spawn") != null) {
            plugin.am.setLobby(SMeth.toLocation(plugin.getData().getString("lobby-spawn")));
        }
    }

    /**
     * Loads all the arenas to the ArenaManager
     */
    public static void loadArenas() {
        FileConfiguration data = plugin.getData();

        for (String s : data.getConfigurationSection("arena").getKeys(false)) {
            ArenaManager am = plugin.getArenaManager();
            int id = am.createArena();
            Arena arena = am.getArena(id);
            String pfx = "arena." + id + ".";

            arena.setName(data.getString(pfx + "name"));
            arena.setTeams(parseTeams(data, pfx + "teams"));
            arena.setMinPlayers(data.getInt(pfx + "min-players"));
            arena.setMaxPlayers(data.getInt(pfx + "max-players"));
            arena.setDoubleJump(data.getBoolean(pfx + "double-jump"));

            int count = 0;
            Location[] loc = new Location[8];
            for (String str : data.getStringList(pfx + "team-lobby")) {
                loc[count] = SMeth.toLocation(str);
                count++;
            }
            // Temporary workaround for team conversion.
            for (int i = 0; i < loc.length; i++) {
                if (loc[i] == null) {
                    continue;
                }

                arena.setLobbySpawn(CrystalQuestTeam.valueOf(i), loc[i]);
            }

            arena.setEnabled(data.getBoolean(pfx + "state"));

            for (String str : data.getStringList(pfx + "player-spawns")) {
                arena.addPlayerSpawn(SMeth.toLocation(str));
            }
            for (String str : data.getStringList(pfx + "crystal-spawns")) {
                arena.addCrystalSpawn(SMeth.toLocation(str));
            }
            for (String str : data.getStringList(pfx + "item-spawns")) {
                arena.addItemSpawn(SMeth.toLocation(str));
            }

            int i = 0;
            Location[] locs = new Location[2];
            for (String str : data.getStringList(pfx + "protection")) {
                locs[i] = SMeth.toLocation(str);
                i++;
            }
            arena.setProtection(locs);

            ConfigurationSection teamSpawns = data.getConfigurationSection(pfx + "team-spawns");
            if (teamSpawns == null) {
                return;
            }

            for (String key : teamSpawns.getKeys(false)) {
                CrystalQuestTeam team;

                // Compatibility to v1.3 and below.
                try {
                    int teamId = Integer.parseInt(key);
                    team = CrystalQuestTeam.valueOf(teamId);
                }
                // Otherwise, use team names.
                catch (NumberFormatException exc) {
                    team = CrystalQuestTeam.valueOfName(key);
                }

                List<Location> spawnList = data.getStringList(pfx + "team-spawns." + key)
                        .stream()
                        .map(SMeth::toLocation)
                        .collect(Collectors.toList());
                arena.getTeamSpawns().put(team, spawnList);
            }
        }
    }

    /**
     * Parses the team list, also takes legacy formats (before v1.3) into account.
     */
    private static List<CrystalQuestTeam> parseTeams(FileConfiguration data, String node) {
        int integerValue = data.getInt(node);

        // When there is no integer value: parse as if it's a list.
        if (integerValue == 0) {
            return data.getStringList(node).stream()
                    .map(CrystalQuestTeam::valueOfTeamName)
                    .collect(Collectors.toList());
        }

        // Pre v1.3
        // Otherwise do legacy stuff.
        return IntStream.range(0, integerValue)
                .mapToObj(CrystalQuestTeam::valueOf)
                .collect(Collectors.toList());
    }
}