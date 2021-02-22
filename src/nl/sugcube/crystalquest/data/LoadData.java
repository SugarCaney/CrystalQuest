package nl.sugcube.crystalquest.data;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.ArenaManager;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import nl.sugcube.crystalquest.sba.SMethods;
import nl.sugcube.crystalquest.util.Materials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.List;
import java.util.Set;
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
        plugin.signHandler.getSignLocations().clear();
        for (String s : plugin.getData().getStringList("signs")) {
            Location loc = SMethods.toLocation(s);
            Material blockType = loc.getBlock().getType();
            if (Materials.isWallSign(blockType) || Materials.isSignPost(blockType)) {
                plugin.signHandler.getSignLocations().add(loc);
            }
        }
        plugin.signHandler.updateSigns();
    }

    /**
     * Loads the lobby-spawn
     */
    public static void loadLobbySpawn() {
        if (plugin.getData().getString("lobby-spawn") != null) {
            plugin.arenaManager.setLobby(SMethods.toLocation(plugin.getData().getString("lobby-spawn")));
        }
    }

    /**
     * Loads all the arenas to the ArenaManager
     */
    public static void loadArenas() {
        FileConfiguration data = plugin.getData();

        ConfigurationSection arenaSection = data.getConfigurationSection("arena");
        if (arenaSection == null) {
            return;
        }

        Set<String> arenaIds = arenaSection.getKeys(false);
        ArenaManager arenaManager = plugin.getArenaManager();
        for (String arenaId : arenaIds) {
            int id = arenaManager.createArena();
            Arena arena = arenaManager.getArena(id);
            String pfx = "arena." + id + ".";

            arena.setName(data.getString(pfx + "name"));
            Collection<CrystalQuestTeam> teams = parseTeams(data, pfx + "teams");
            if (teams.size() >= 2 && teams.size() <=8) {
                arena.setTeams(teams);
            }
            arena.setMinPlayers(data.getInt(pfx + "min-players"));
            arena.setMaxPlayers(data.getInt(pfx + "max-players"));
            arena.setDoubleJump(data.getBoolean(pfx + "double-jump"));

            // Compatibility to v1.3 and below
            String teamLobbySection = pfx + "team-lobby";
            List<String> teamLobbies = data.getStringList(teamLobbySection);

            if (teamLobbies != null && !teamLobbies.isEmpty()) {
                compat_teamspawn_v1_3(arena, teamLobbies);
            }
            // Otherwise, use team names.
            else {
                ConfigurationSection section = data.getConfigurationSection(teamLobbySection);
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        CrystalQuestTeam team = CrystalQuestTeam.valueOfName(key);
                        String locationString = data.getString(teamLobbySection + "." + key);
                        Location location = SMethods.toLocation(locationString);
                        arena.setLobbySpawn(team, location);
                    }
                }
            }

            arena.setEnabled(data.getBoolean(pfx + "state"));

            for (String str : data.getStringList(pfx + "player-spawns")) {
                arena.addPlayerSpawn(SMethods.toLocation(str));
            }
            for (String str : data.getStringList(pfx + "crystal-spawns")) {
                arena.addCrystalSpawn(SMethods.toLocation(str));
            }
            for (String str : data.getStringList(pfx + "item-spawns")) {
                arena.addItemSpawn(SMethods.toLocation(str));
            }

            int i = 0;
            Location[] locs = new Location[2];
            for (String str : data.getStringList(pfx + "protection")) {
                locs[i] = SMethods.toLocation(str);
                i++;
            }
            arena.setProtection(locs);

            ConfigurationSection teamSpawns = data.getConfigurationSection(pfx + "team-spawns");
            if (teamSpawns == null) {
                continue;
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
                        .map(SMethods::toLocation)
                        .collect(Collectors.toList());
                arena.getTeamSpawns().put(team, spawnList);
            }
        }
    }

    /**
     * Loads the teamspawn data from CQ versions up to v1.3.
     * <p>
     * Pre v1.3: Teamlobbies are stored in a list mapped to team indices.<br>
     * Post v1.3: Teamlobbies are mapped from {@code CrystalQuestTeam.name} to location.
     */
    private static void compat_teamspawn_v1_3(Arena arena, List<String> locations) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i) == null) {
                continue;
            }

            Location parsed = SMethods.toLocation(locations.get(i));
            if (parsed == null) {
                continue;
            }

            arena.setLobbySpawn(CrystalQuestTeam.valueOf(i), parsed);
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