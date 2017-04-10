package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author SugarCaney
 */
public class ArenaManager {

    public static CrystalQuest plugin;
    public List<Arena> arena = new ArrayList<Arena>();

    private Location lobbyspawn;

    /**
     * CONSTRUCTOR
     * Passes through the instance of the plugin.
     *
     * @param instance
     *         (CrystalQuest) The instance of the plugin.
     */
    public ArenaManager(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Checks wether the player is in spectate-mode
     *
     * @param p
     *         (Player) The player to check for
     * @return (boolean) True if the player is a spectator. False if not.
     */
    public boolean isSpectator(Player p) {
        for (Arena a : this.arena) {
            for (UUID id : a.getPlayers()) {
                Player pl = Bukkit.getPlayer(id);
                if (a.getSpectators().contains(pl.getUniqueId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the location of the Lobbyspawn
     *
     * @return (Location) The Lobbyspawn
     */
    public Location getLobby() {
        return this.lobbyspawn;
    }

    /**
     * Sets the spawn of the main lobby
     *
     * @param loc
     *         (Location) The spawnpoint of the lobby
     */
    public void setLobby(Location loc) {
        this.lobbyspawn = loc;
    }

    /**
     * Starts the runnable managing the spawning of the items
     */
    public void registerItemSpawningSequence() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new ItemSpawner(plugin), 2L, 2L);
    }

    /**
     * Starts the runnable managing the spawning of the crystals
     */
    public void registerCrystalSpawningSequence() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new CrystalSpawner(plugin), 2L, 2L);
    }

    /**
     * Starts the GameLoop
     */
    public void registerGameLoop() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new GameLoop(plugin, this), 20L, 20L);
    }

    /**
     * Gets the team the player is in. (ID)
     *
     * @param p
     *         (Player) The player to check for.
     * @return (int) The teamId from the team the player is in.
     */
    public int getTeam(Player p) {
        Arena a = getArena(p.getUniqueId());
        if (a != null) {
            return a.getTeam(p);
        }
        else {
            return -1;
        }
    }

    /**
     * Gets the arena the player is in
     *
     * @param pId
     *         (UUID) The UUID of the player to look for.
     * @return (Arena) The arena the player is in.
     */
    public Arena getArena(UUID pId) {
        for (Arena a : arena) {
            for (UUID id : a.getPlayers()) {
                if (pId.equals(id)) {
                    return a;
                }
            }
            for (UUID id : a.getSpectators()) {
                if (pId.equals(id)) {
                    return a;
                }
            }
        }
        return null;
    }

    /**
     * Checks if the player is in game.
     *
     * @param p
     *         (Player) The player to check for.
     * @return (boolean) True if the player is in an arena. False if the player isn't.
     */
    public boolean isInGame(Player p) {
        for (Arena a : arena) {
            if (a.getPlayers().contains(p.getUniqueId()) || a.getSpectators().contains(p.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get an arena using the name.
     *
     * @param s
     *         (String) The arena's name.
     * @return (Arena) The arena with the given name or null if there is no arena with such name.
     */
    public Arena getArena(String s) {
        try {
            int id = Integer.parseInt(s);
            return this.getArena(id);
        }
        catch (Exception ignored) {
        }

        if (this.arena.size() > 0) {
            for (Arena a : this.arena) {
                if (a.getName().equalsIgnoreCase(s)) {
                    return a;
                }
            }
        }

        return null;
    }

    /**
     * Get an arena using the id.
     *
     * @param arenaId
     *         (int) The ID of the arena.
     * @return (Arena) The arena with the given ID or null if there is no arena with such ID.
     */
    public Arena getArena(int arenaId) {
        for (Arena a : this.arena) {
            if (a.getId() == arenaId) {
                return a;
            }
        }
        return null;
    }

    /**
     * Creates an arena with an ID of highestId + 1.
     *
     * @return (int) The arenaId
     */
    public int createArena() {
        boolean isFound = false;
        int i = 0;
        while (!isFound) {
            if (plugin.getArenaManager().getArena(i) == null) {
                isFound = true;
            }
            i++;
        }
        int arenaId = i - 1;
        arena.add(new Arena(plugin, arenaId));
        return arenaId;
    }

    /**
     * Get all arenas.
     *
     * @return (ArenaList) List of all arena's.
     */
    public List<Arena> getArenas() {
        return this.arena;
    }
}