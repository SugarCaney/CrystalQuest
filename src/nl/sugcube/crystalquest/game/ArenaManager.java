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

    /**
     * Main plugin instance.
     */
    public final CrystalQuest plugin;

    /**
     * List containing all the registered arenas.
     */
    public final List<Arena> arenas = new ArrayList<>();

    /**
     * Location of the lobby.
     */
    private Location lobbyspawn;

    /**
     * Passes through the instance of the plugin.
     *
     * @param instance
     *         The instance of the plugin.
     */
    public ArenaManager(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Checks wether the player is in spectate-mode
     *
     * @param player
     *         The player to check for
     * @return True if the player is a spectator. False if not.
     */
    public boolean isSpectator(Player player) {
        for (Arena a : arenas) {
            if (a.getSpectators().contains(player.getUniqueId())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the location of the Lobbyspawn
     *
     * @return The Lobbyspawn
     */
    public Location getLobby() {
        return lobbyspawn;
    }

    /**
     * Sets the spawn of the main lobby
     *
     * @param lobbyspawn
     *         The spawnpoint of the lobby
     */
    public void setLobby(Location lobbyspawn) {
        this.lobbyspawn = lobbyspawn;
    }

    /**
     * Starts the runnable managing the spawning of the items
     */
    public void registerItemSpawningSequence() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin,
                new ItemSpawner(plugin),
                2L,
                2L
        );
    }

    /**
     * Starts the runnable managing the spawning of the crystals
     */
    public void registerCrystalSpawningSequence() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin,
                new CrystalSpawner(plugin),
                2L,
                2L
        );
    }

    /**
     * Starts the GameLoop
     */
    public void registerGameLoop() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin,
                new GameLoop(plugin, this),
                20L,
                20L
        );
    }

    /**
     * Gets the team the player is in.
     *
     * @param player
     *         The player to check for.
     * @return The team the player is in.
     * @throws IllegalStateException
     *         When the player is not in any team.
     */
    public CrystalQuestTeam getTeam(Player player) throws IllegalStateException {
        Arena arena = getArena(player.getUniqueId());
        if (arena == null) {
            throw new IllegalStateException("Player " + player + " is not in an arenas!");
        }

        return arena.getTeam(player);
    }

    /**
     * Gets the arena the entity with the given id is in
     *
     * @param id
     *         The UUID of the player to look for.
     * @return The arenas the player is in or {@code null} when no such arenas exists.
     */
    public Arena getArena(UUID id) {
        return arenas.stream()
                .filter(arena -> arena.isInArena(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the arena the player is in
     *
     * @param player
     *         The player to look for.
     * @return The arenas the player is in or {@code null} when no such arenas exists.
     */
    public Arena getArena(Player player) {
        return getArena(player.getUniqueId());
    }

    /**
     * Checks if the player is in game.
     *
     * @param player
     *         The player to check for.
     * @return True if the player is in an arenas. False if the player isn't.
     */
    public boolean isInGame(Player player) {
        return arenas.stream().anyMatch(arena -> arena.isInArena(player));
    }

    /**
     * Get an arenas using the name.
     *
     * @param string
     *         The arenas's name.
     * @return The arenas with the given name or null if there is no arenas with such name.
     */
    public Arena getArena(String string) {
        try {
            int id = Integer.parseInt(string);
            return getArena(id);
        }
        catch (NumberFormatException ignored) {
        }

        if (arenas.isEmpty()) {
            return null;
        }

        return arenas.stream()
                .filter(arena -> arena.getName().equalsIgnoreCase(string))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get an arenas using the id.
     *
     * @param arenaId
     *         The ID of the arenas.
     * @return The arenas with the given ID or null if there is no arenas with such ID.
     */
    public Arena getArena(int arenaId) {
        return arenas.stream()
                .filter(arena -> arena.getId() == arenaId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates an arenas with an ID of highestId + 1.
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
        arenas.add(new Arena(plugin, arenaId));
        return arenaId;
    }

    /**
     * Get all arenas.
     *
     * @return List of all arenas's.
     */
    public List<Arena> getArenas() {
        return arenas;
    }
}