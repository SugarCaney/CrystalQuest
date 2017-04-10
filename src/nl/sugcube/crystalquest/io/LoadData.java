package nl.sugcube.crystalquest.io;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.ArenaManager;
import nl.sugcube.crystalquest.sba.SMeth;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

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
        try {
            FileConfiguration data = plugin.getData();

            for (String s : data.getConfigurationSection("arena").getKeys(false)) {
                ArenaManager am = plugin.getArenaManager();
                int id = am.createArena();
                Arena a = am.getArena(id);
                String pfx = "arena." + id + ".";

                a.setName(data.getString(pfx + "name"));
                a.setTeams(data.getInt(pfx + "teams"));
                a.setMinPlayers(data.getInt(pfx + "min-players"));
                a.setMaxPlayers(data.getInt(pfx + "max-players"));
                a.setDoubleJump(data.getBoolean(pfx + "double-jump"));

                int count = 0;
                Location[] loc = new Location[8];
                for (String str : data.getStringList(pfx + "team-lobby")) {
                    loc[count] = SMeth.toLocation(str);
                    count++;
                }
                a.setLobbySpawns(loc);

                a.setEnabled(data.getBoolean(pfx + "state"));

                for (String str : data.getStringList(pfx + "player-spawns")) {
                    a.addPlayerSpawn(SMeth.toLocation(str));
                }
                for (String str : data.getStringList(pfx + "crystal-spawns")) {
                    a.addCrystalSpawn(SMeth.toLocation(str));
                }
                for (String str : data.getStringList(pfx + "item-spawns")) {
                    a.addItemSpawn(SMeth.toLocation(str));
                }

                try {
                    int i = 0;
                    Location[] locs = new Location[2];
                    for (String str : data.getStringList(pfx + "protection")) {
                        locs[i] = SMeth.toLocation(str);
                        i++;
                    }
                    a.setProtection(locs);
                }
                catch (Exception ignored) {
                }

                for (int i = 0; i < 8; i++) {
                    if (data.isSet(pfx + "team-spawns." + i)) {
                        for (String str : data.getStringList(pfx + "team-spawns." + i)) {
                            a.getTeamSpawns().get(i).add(SMeth.toLocation(str));
                        }
                    }
                }
            }
        }
        catch (Exception ignored) {
        }
    }

}