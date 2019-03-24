package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.events.ArenaStartEvent;
import nl.sugcube.crystalquest.events.PlayerJoinArenaEvent;
import nl.sugcube.crystalquest.events.PlayerLeaveArenaEvent;
import nl.sugcube.crystalquest.events.TeamWinGameEvent;
import nl.sugcube.crystalquest.sba.SMethods;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author SugarCaney
 */
public class Arena {

    /**
     * The main plugin instance.
     */
    public CrystalQuest plugin;

    /**
     * The maximum amount of players in the arenas.
     * <p>
     * {@code -1} when this value is not set.
     */
    private int maxPlayers = -1;

    /**
     * The minimum amount of players in the arenas.
     * <p>
     * {@code -1} when this value is not set.
     */
    private int minPlayers = -1;

    /**
     * The name of the arenas.
     * <p>
     * {@code ""} when there is no name set.
     */
    private String name = "";

    /**
     * The unique id of the arenas.
     */
    private int id;

    /**
     * Determines if the crystalquest.vip permission is required in order to play in the arenas.
     * <p>
     * {@code true} when required, {@code false} when not required.
     */
    private boolean vip;

    /**
     * Map that stores per team what the spawn location is for the lobby.
     */
    private Map<CrystalQuestTeam, Location> lobbySpawns = new HashMap<>();

    /**
     * The amount of seconds it takes before the arenas starts.
     */
    private int count;

    /**
     * Determines whether the countdown is doing its buisiness.
     * <p>
     * {@code true} when the countdown is happening, {@code false} when there is no countdown.
     */
    private boolean isCounting;

    /**
     * The scoreboard tracking the scores of the teams.
     */
    private Scoreboard score;

    /**
     * Contains whether the arenas is in game or not.
     * <p>
     * {@code true} when the arenas is ingame, {@code false} otherwise.
     */
    private boolean inGame;

    /**
     * The amount of seconds that is left in the game.
     */
    private int timeLeft;

    /**
     * Whether the arenas is enabled or disabled.
     * <p>
     * {@code true} when the arenas is enabled, {@code false} when disabled.
     */
    private boolean enabled = true;

    /**
     * The amount of seconds that is left in the resetting phase.
     */
    private int afterCount;

    /**
     * Whether the game is over and is resetting or not.
     * <p>
     * {@code true} when the arenas is resetting, {@code false} otherwise.
     */
    private boolean isEndGame = false;

    /**
     * The two corner positions of the arenas that define the protection.
     */
    private Location[] protection = new Location[2];

    /**
     * Whether double jump is enabled in this arenas or not.
     * <p>
     * {@code true} when there is double jump, {@code false} when there is no double jump.
     */
    private boolean doubleJump = false;

    /**
     * Random object to be used by this instance.
     */
    private Random ran = new Random();

    /**
     * List of all the places where players will spawn.
     */
    private List<Location> playerSpawns = new ArrayList<>();

    /**
     * List of all the places where crystal will spawn.
     */
    private List<Location> crystalSpawns = new ArrayList<>();

    /**
     * List of all the locations where items will spawn.
     */
    private List<Location> itemSpawns = new ArrayList<>();

    /**
     * List of all the wolfs that have been spawned in the arenas.
     */
    private List<Wolf> gameWolfs = new ArrayList<>();

    /**
     * List of all creepers that have been spawned in the arenas.
     */
    private List<Creeper> gameCreepers = new ArrayList<>();

    /**
     * List of all crystals that have been spawned in the arenas.
     */
    private List<Entity> gameCrystals = new ArrayList<>();

    /**
     * List of all UUIDs of players that are in the arenas.
     */
    private List<UUID> players = new ArrayList<>();

    /**
     * List of all UUIDs of players that are spectating the arenas.
     */
    private List<UUID> spectators = new ArrayList<>();

    /**
     * Map that maps every player's UUID to the team which they are part of.
     */
    private Map<UUID, CrystalQuestTeam> playerTeams = new ConcurrentHashMap<>();

    /**
     * Map that maps every ender crystal to their location.
     */
    private Map<Entity, Location> crystalLocations = new ConcurrentHashMap<>();

    /**
     * List of all blocks that are placed during the game in the arenas.
     */
    private List<Block> gameBlocks = new ArrayList<>();

    /**
     * Map that maps the team to a list of all locations where their players can spawn.
     */
    private Map<CrystalQuestTeam, List<Location>> teamSpawns = new ConcurrentHashMap<>();

    /**
     * Map that maps all locations of placed landmines to the UUID of the player that placed it.
     */
    private Map<Location, UUID> landmines = new ConcurrentHashMap<>();

    /**
     * Map that maps the UUID of a player to the gamemode which they had before entering the
     * arenas.
     */
    private Map<UUID, GameMode> preSpecGamemodes = new ConcurrentHashMap<>();

    /**
     * The menu that shows up when players want to select a team when joining the arenas.
     */
    private Inventory teamMenu;

    /**
     * List containing all the teams that are available for the arena.
     */
    private List<CrystalQuestTeam> teams = new ArrayList<>();

    //Scoreboard
    /**
     * Scoreboard: Maps each CrystalQuest team to a respective scoreboard team.
     */
    private Map<CrystalQuestTeam, Team> scoreboardTeams = new ConcurrentHashMap<>();

    /**
     * Scoreboard: The object used to track the points earned.
     */
    private Objective points;

    /**
     * Scoreboard: Maps CrystalQuest teams to their respective scoreboard scores.
     */
    private Map<CrystalQuestTeam, Score> scoreboardScores = new ConcurrentHashMap<>();

    /**
     * Scoreboard: Team that contains all the spectators.
     */
    private Team spectatorTeam;

    /**
     * @param instance
     *         Instance of the plugin
     * @param arenaId
     *         ID of the arenas
     */
    public Arena(CrystalQuest instance, int arenaId) {
        this.plugin = instance;
        this.score = Bukkit.getScoreboardManager().getNewScoreboard();
        this.timeLeft = plugin.getConfig().getInt("arena.game-length");
        this.count = plugin.getConfig().getInt("arena.countdown");
        this.id = arenaId;
        this.afterCount = plugin.getConfig().getInt("arena.after-count");

        for (CrystalQuestTeam team : CrystalQuestTeam.getTeams()) {
            teamSpawns.put(team, new ArrayList<>());
        }

        initializeScoreboard();
    }

    /**
     * Get a list of UUIDs of players who are spectating the game.
     *
     * @see Arena#spectators
     */
    public List<UUID> getSpectators() {
        return spectators;
    }

    /**
     * Get the map containing the players who placed certain landmines.
     *
     * @see Arena#landmines
     */
    public Map<Location, UUID> getLandmines() {
        return this.landmines;
    }

    /**
     * Sets if the arenas accepts double jumps
     *
     * @param canDoubleJump
     *         True to accept, False to decline.
     * @see Arena#doubleJump
     */
    public void setDoubleJump(boolean canDoubleJump) {
        this.doubleJump = canDoubleJump;
    }

    /**
     * Gets if this map accepts double jumps
     *
     * @return True if accepted, false if not accepted.
     * @see Arena#doubleJump
     */
    public boolean canDoubleJump() {
        return doubleJump;
    }

    /**
     * Gets the hashmap with the crystalquest team mapped to the list containing the spawnpoints
     *
     * @see Arena#teamSpawns
     */
    public Map<CrystalQuestTeam, List<Location>> getTeamSpawns() {
        return teamSpawns;
    }

    /**
     * Get all the teamspawns of a given team.
     *
     * @param team
     *         The team to get the spawns of.
     * @return The teamspawns, or {@code null} when there are no team spawns.
     */
    public List<Location> getTeamSpawns(CrystalQuestTeam team) {
        return teamSpawns.get(team);
    }

    /**
     * Sets the positions of the protection of the Arena
     *
     * @param locs
     *         Index 0: pos1, Index 1: pos2.
     * @see Arena#protection
     */
    public void setProtection(Location[] locs) {
        this.protection = locs;
    }

    /**
     * Gets the positions of the protection of the Arena
     *
     * @return Index 0: pos1, Index 2: pos0. Null if not set.
     * @see Arena#protection
     */
    public Location[] getProtection() {
        return protection;
    }

    /**
     * Checks if the arenas is full
     *
     * @return True if full, False if not.
     */
    public boolean isFull() {
        return getPlayers().size() >= getMaxPlayers();
    }

    /**
     * Gets the Wolf-list containing all Wolfs spawned in-game
     *
     * @see Arena#gameWolfs
     */
    public List<Wolf> getGameWolfs() {
        return gameWolfs;
    }

    /**
     * Gets the Blocks-list containing all blocks placed in-game
     *
     * @see Arena#gameBlocks
     */
    public List<Block> getGameBlocks() {
        return gameBlocks;
    }

    /**
     * Gets the Creepers which are spawned in-game
     *
     * @return List containing all the creepers
     * @see Arena#gameCreepers
     */
    public List<Creeper> getGameCreepers() {
        return gameCreepers;
    }

    /**
     * Gets the inventory of the Team-Menu
     *
     * @return The team-menu inventory
     * @see Arena#teamMenu
     */
    public Inventory getTeamMenu() {
        return teamMenu;
    }

    /**
     * Get the hashmap containing the locations of the entities
     *
     * @return EnderCrystals and Locations
     * @see Arena#crystalLocations
     */
    public Map<Entity, Location> getCrystalLocations() {
        return crystalLocations;
    }

    /**
     * Checks whether a player with the given id is player or spectator in the arenas.
     *
     * @param uuid
     *         The uuid of the player.
     * @return {@code true} when the player is in the arenas, {@code false} otherwise.
     */
    public boolean isInArena(UUID uuid) {
        return players.contains(uuid) || spectators.contains(uuid);
    }

    /**
     * Checks whether a player is in the arenas (spectator or playing).
     *
     * @param player
     *         The player to check for.
     * @return {@code true} when the player is in the arenas, {@code false} otherwise.
     */
    public boolean isInArena(Player player) {
        return isInArena(player.getUniqueId());
    }

    /**
     * Sends a custom message to all players in the arenas
     *
     * @param player
     *         The dead player
     * @param message
     *         The verb that will show up. fe: Killed, Gibbed etc.
     */
    public void sendDeathMessage(Player player, String message) {
        CrystalQuestTeam team = plugin.getArenaManager().getTeam(player);
        ChatColor colour = team.getChatColour();

        for (UUID id : getPlayers()) {
            Player target = Bukkit.getPlayer(id);
            target.sendMessage(colour + player.getName() + ChatColor.GRAY + message);
        }

        for (UUID id : getSpectators()) {
            Player target = Bukkit.getPlayer(id);
            target.sendMessage(colour + player.getName() + ChatColor.GRAY + message);
        }
    }

    /**
     * Sends a death message to all players in the arenas with a custom verb
     *
     * @param dead
     *         The dead player
     * @param killer
     *         The player who killed dead
     * @param verb
     *         The verb that will show up. fe: Killed, Gibbed etc.
     */
    public void sendDeathMessage(Player dead, Player killer, String verb) {
        CrystalQuestTeam team = plugin.getArenaManager().getTeam(dead);
        CrystalQuestTeam teamKiller = plugin.getArenaManager().getTeam(killer);

        ChatColor c = team.getChatColour();
        ChatColor cK = teamKiller.getChatColour();

        for (UUID id : getPlayers()) {
            Player player = Bukkit.getPlayer(id);
            player.sendMessage(c + dead.getName() + ChatColor.GRAY + " has been " + verb + " by " +
                    cK + killer.getName());
        }

        for (UUID id : getSpectators()) {
            Player spectator = Bukkit.getPlayer(id);
            spectator.sendMessage(c + dead.getName() + ChatColor.GRAY + " has been " + verb +
                    " by " + cK + killer.getName());
        }
    }

    /**
     * Sends a death message to all players in the arenas
     *
     * @param dead
     *         The dead player
     * @param killer
     *         The player who killed dead
     */
    public void sendDeathMessage(Player dead, Player killer) {
        CrystalQuestTeam team = plugin.getArenaManager().getTeam(dead);
        CrystalQuestTeam teamKiller = plugin.getArenaManager().getTeam(killer);

        ChatColor c = team.getChatColour();
        ChatColor cK = teamKiller.getChatColour();

        for (UUID id : getPlayers()) {
            Player pl = Bukkit.getPlayer(id);
            pl.sendMessage(c + dead.getName() + ChatColor.GRAY + " has been killed by " + cK +
                    killer.getName());
        }

        for (UUID id : getSpectators()) {
            Player spec = Bukkit.getPlayer(id);
            spec.sendMessage(c + dead.getName() + ChatColor.GRAY + " has been killed by " +
                    cK + killer.getName());
        }
    }

    /**
     * Sends a death message to all players in the arenas
     *
     * @param player
     *         The dead player
     */
    public void sendDeathMessage(Player player) {
        CrystalQuestTeam team = plugin.getArenaManager().getTeam(player);
        ChatColor c = team.getChatColour();

        for (UUID id : getPlayers()) {
            Player pl = Bukkit.getPlayer(id);
            pl.sendMessage(c + player.getName() + ChatColor.GRAY + " has died");
        }

        for (UUID id : getSpectators()) {
            Player spec = Bukkit.getPlayer(id);
            spec.sendMessage(c + player.getName() + ChatColor.GRAY + " has died");
        }
    }

    /**
     * Get the list containing all the crystals that have spawned in the arenas
     *
     * @return The crystals
     * @see Arena#gameCrystals
     */
    public List<Entity> getGameCrystals() {
        return gameCrystals;
    }

    /**
     * Gets the time the game waits before teleporting to the lobby, after the game ended.
     *
     * @return The amount of seconds left.
     * @see Arena#afterCount
     */
    public int getAfterCount() {
        return afterCount;
    }

    /**
     * Sets the time the game waits before teleporting to the lobby, after the game ended.
     *
     * @param count
     *         The amount of seconds to wait
     * @see Arena#afterCount
     */
    public void setAfterCount(int count) {
        this.afterCount = count;
    }

    /**
     * Returns true if the game has finished and is in the after-game phase.
     *
     * @return true if the game has ended, false if it hasn't
     * @see Arena#isEndGame
     */
    public boolean isEndGame() {
        return isEndGame;
    }

    /**
     * Sets the the arenas is in the end-game phase
     *
     * @param isEndGame
     *         true if end-game, false if isn't.
     * @see Arena#isEndGame
     */
    public void setEndGame(boolean isEndGame) {
        if (isEndGame) {
            for (Entity e : this.getGameCrystals()) {
                getCrystalLocations().remove(e);
                e.remove();
            }
            setAfterCount(plugin.getConfig().getInt("arena.after-count"));
        }

        this.isEndGame = isEndGame;
        plugin.signHandler.updateSigns();
    }

    /**
     * Get the teams (scoreboard) in the arenas
     *
     * @return The teams in the arenas
     */
    public Collection<Team> getScoreboardTeams() {
        return scoreboardTeams.values();
    }

    /**
     * Checks if the arenas is enabled/disabled.
     *
     * @return If enabled true, if disabled false
     * @see Arena#enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the state of the arenas.
     *
     * @param isEnabled
     *         "true" to enable, "false" to disable
     * @see Arena#enabled
     */
    public void setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;

        for (UUID id : this.getPlayers()) {
            Player player = Bukkit.getPlayer(id);
            player.sendMessage(Broadcast.get("arena.disabled"));
        }

        if (!isEnabled) {
            resetArena(false);
        }

        plugin.signHandler.updateSigns();
    }

    /**
     * Updates the time in the scoreboardname
     */
    public void updateTimer() {
        this.points.setDisplayName(
                SMethods.setColours(
                        "&c" + Broadcast.get("arena.time-left") + " &f" + SMethods.toTime(timeLeft)
                )
        );
    }

    /**
     * Sets the menu from which the players choose their teams.
     *
     * @param teamMenu
     *         Inventory to set it to.
     * @see Arena#teamMenu
     */
    public void setTeamMenu(Inventory teamMenu) {
        this.teamMenu = teamMenu;
    }

    /**
     * Gets the teams with the least amount of players for a fair distribution process.
     *
     * @return The teams with the least amount of players.
     */
    public List<CrystalQuestTeam> getSmallestTeams() {
        if (getScoreboardTeams().isEmpty()) {
            return new ArrayList<>();
        }

        int leastAmount = getScoreboardTeams().stream()
                .min(Comparator.comparingInt(Team::getSize))
                .map(Team::getSize)
                .orElseThrow(() -> new IllegalStateException("There is no minimum"));

        return scoreboardTeams.entrySet().stream()
                .filter(entry -> entry.getValue().getEntries().size() <= leastAmount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Initializes the scoreboard. This makes or updates the scoreboard.
     */
    public void initializeScoreboard() {
        score = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboardTeams.clear();
        scoreboardScores.clear();

        spectatorTeam = score.registerNewTeam("Spectate");
        spectatorTeam.setAllowFriendlyFire(false);
        spectatorTeam.setCanSeeFriendlyInvisibles(true);
        spectatorTeam.setPrefix(ChatColor.BLUE + "[Spec] ");

        for (CrystalQuestTeam cqTeam : getTeams()) {
            Team team = score.registerNewTeam(cqTeam.getName());
            team.setPrefix(cqTeam.getChatColour().toString());
            team.setAllowFriendlyFire(false);
            scoreboardTeams.put(cqTeam, team);
        }

        points = score.registerNewObjective("points", "dummy", "CrystalQuest");
        points.setDisplaySlot(DisplaySlot.SIDEBAR);
        updateTimer();

        for (CrystalQuestTeam cqTeam : getTeams()) {
            Score score = points.getScore(cqTeam.toString());
            score.setScore(0);
            scoreboardScores.put(cqTeam, score);
        }
    }

    /**
     * Gets the amount of players in a team.
     *
     * @param team
     *         The team to get the amount of players from.
     * @return The amount of players.
     */
    public int getTeamPlayerCount(CrystalQuestTeam team) {
        return (int)playerTeams.entrySet().stream()
                .filter(entry -> entry.getValue().equals(team))
                .count();
    }

    /**
     * Gets the team the player is in.
     *
     * @param player
     *         The player from whose you want to know the team he/she is in.
     * @return The team the player is in.
     */
    public CrystalQuestTeam getTeam(Player player) {
        return playerTeams.get(player.getUniqueId());
    }

    /**
     * Get an unmodifiable collection containing all the teams that are present in the arena.
     */
    public Collection<CrystalQuestTeam> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    /**
     * Checks if the given team participates in the arena.
     *
     * @param team
     *         The team to check for.
     * @return {@code true} if the team participates, {@code false} otherwise.
     */
    public boolean hasTeam(CrystalQuestTeam team) {
        return teams.contains(team);
    }

    /**
     * Checks if the player is in the specific team.
     *
     * @param player
     *         The player you want to check for.
     * @param team
     *         The team constant.
     * @return true if they're in, false if they aren't.
     */
    public boolean isInTeam(Player player, CrystalQuestTeam team) {
        return team.equals(playerTeams.get(player.getUniqueId()));
    }

    /**
     * If there are players in the arenas Reveal the winner
     *
     * @return The winning team, or {@code null} when something went wrong.
     */
    public CrystalQuestTeam declareWinner() {
        if (getPlayers().isEmpty()) {
            return null;
        }

        Score hScore = scoreboardScores.values().stream()
                .max(Comparator.comparingInt(Score::getScore))
                .orElseThrow(() -> new AssertionError("Should not happen!"));

        CrystalQuestTeam winningTeam = null;
        List<UUID> winningPlayers = new ArrayList<>();

        for (CrystalQuestTeam cqTeam : getTeams()) {
            if (hScore.getPlayer().getName().equalsIgnoreCase(cqTeam.toString())) {
                winningTeam = cqTeam;
            }
        }

        if (winningTeam == null) {
            throw new IllegalStateException("Winning team could not be found!");
        }

        ChatColor colour = winningTeam.getChatColour();

        for (UUID id : getPlayers()) {
            Player p = Bukkit.getPlayer(id);
            p.sendMessage(colour + "<>---------------------------<>");
            p.sendMessage("    " + winningTeam + " " + Broadcast.get("arena.won"));
            p.sendMessage(colour + "<>---------------------------<>");

            if (plugin.arenaManager.getTeam(p).equals(winningTeam)) {
                winningPlayers.add(p.getUniqueId());
            }
        }

        Bukkit.getPluginManager().callEvent(
                new TeamWinGameEvent(
                        winningPlayers,
                        this,
                        winningTeam,
                        getTeamCount(),
                        scoreboardTeams.values()
                )
        );

        return winningTeam;
    }

    /**
     * Chooses a random player in the arenas and from another team.
     *
     * @param excluded
     *         The player whose team cannot be chosen.
     * @return The chosen player. Null if there are no players to choose from.
     */
    public Player getRandomPlayer(Player excluded) {
        CrystalQuestTeam team = getTeam(excluded);

        List<Player> toChoose = new ArrayList<>();
        for (UUID id : players) {
            Player player = Bukkit.getPlayer(id);
            if (!getTeam(player).equals(team)) {
                toChoose.add(player);
            }
        }

        if (toChoose.size() == 0) {
            return null;
        }

        return toChoose.get(ran.nextInt(toChoose.size()));
    }

    /**
     * Resets Arena-properties to default. Containing: Countdown, Game-time, Removes the players,
     * Re-initializes scoreboard. Sends a "this-team-won" message
     *
     * @param onEnable
     *         (boolean) If it's called in onEnable.
     */
    public void resetArena(boolean onEnable) {
        if (!onEnable) {
            //Removes all potion-effects on players
            if (getPlayers().size() > 0) {
                for (UUID id : getPlayers()) {
                    Player player = Bukkit.getPlayer(id);
                    Collection<PotionEffect> effects = player.getActivePotionEffects();
                    for (PotionEffect effect : effects) {
                        player.removePotionEffect(effect.getType());
                    }
                    plugin.itemHandler.cursed.remove(player);
                }
            }

            //Removes all blocks placed in-game
            if (getGameBlocks().size() > 0) {
                List<Block> toRemove = new ArrayList<>(getGameBlocks());
                for (Location location : getLandmines().keySet()) {
                    toRemove.add(location.getBlock());
                }
                for (Block block : toRemove) {
                    block.setType(Material.AIR);
                }
            }

            //Removs all wolfs
            if (getGameWolfs().size() > 0) {
                for (Wolf wolf : getGameWolfs()) {
                    if (wolf != null) {
                        wolf.setHealth(0);
                    }
                }
            }

            //Removes all items and crystals
            if (getCrystalSpawns().size() > 0) {
                List<Entity> toRemove = new ArrayList<>();
                for (Entity e : getCrystalSpawns().get(0).getWorld().getEntities()) {
                    if ((e instanceof Item || e instanceof ExperienceOrb || e instanceof Arrow ||
                            e instanceof EnderCrystal || e instanceof LivingEntity) &&
                            !(e instanceof Player)) {
                        if (plugin.protection.isInProtectedArena(e.getLocation())) {
                            toRemove.add(e);
                        }
                    }
                }
                for (Entity entity : toRemove) {
                    entity.remove();
                }
            }
        }

        gameCrystals.clear();
        count = plugin.getConfig().getInt("arena.countdown");
        isCounting = false;
        timeLeft = this.plugin.getConfig().getInt("arena.game-length");
        inGame = false;
        afterCount = plugin.getConfig().getInt("arena.after-game");
        isEndGame = false;
        crystalLocations.clear();
        gameBlocks.clear();
        initializeScoreboard();
        gameWolfs.clear();
        landmines.clear();
        removePlayers();

        plugin.signHandler.updateSigns();
    }

    /**
     * Get the scoreboard of the arenas.
     *
     * @return Scoreboard of the arenas
     * @see Arena#score
     */
    public Scoreboard getScoreboard() {
        return score;
    }

    /**
     * Removes a player from the arenas including removal from the team, resetting his/her
     * scoreboard and restoring his/her inventory.
     *
     * @param p
     *         The player you want to remove from the arenas.
     */
    public void removePlayer(Player p) {
        if (!spectators.contains(p.getUniqueId())) {
            for (UUID id : getPlayers()) {
                Player player = Bukkit.getPlayer(id);
                player.sendMessage(Broadcast.TAG + Broadcast.get("arena.leave")
                        .replace("%player%", getTeam(p).getChatColour() + p.getName())
                        .replace("%count%", "(" + (getPlayers().size() - 1) + "/" + getMaxPlayers() + ")"));
            }
        }

        players.remove(p.getUniqueId());
        for (Team team : scoreboardTeams.values()) {
            if (team.hasPlayer(p)) {
                team.removePlayer(p);
            }
        }

        for (PotionEffect potionEffect : p.getActivePotionEffects()) {
            p.removePotionEffect(potionEffect.getType());
        }

        try {
            p.teleport(plugin.arenaManager.getLobby());
        }
        catch (Exception e) {
            plugin.getLogger().info("Lobby-spawn not set!");
        }

        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        if (p.isOnline()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        playerTeams.remove(p.getUniqueId());
        plugin.inventoryManager.restoreInventory(p);
        plugin.inventoryManager.playerClass.remove(p.getUniqueId());
        spectators.remove(p.getUniqueId());

        if (p.getGameMode() != GameMode.CREATIVE) {
            p.setAllowFlight(false);
        }

        plugin.ability.getAbilities().remove(p.getUniqueId());
        p.setFireTicks(0);
        Bukkit.getPluginManager().callEvent(new PlayerLeaveArenaEvent(p, this));

        if (spectatorTeam.getPlayers().contains(p)) {
            spectatorTeam.removePlayer(p);
        }

        plugin.signHandler.updateSigns();
    }

    /**
     * Adds a player to the arenas including putting into a team, set the scoreboard and give the
     * in-game inventory.
     *
     * @param p
     *         The player to add
     * @param team
     *         The team to put the player in
     * @param spectate
     *         True if the player is spectating
     * @return True if joined, False if not joined
     */
    public boolean addPlayer(Player p, CrystalQuestTeam team, boolean spectate) {
        PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(p, this, spectate);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (!isFull() || plugin.getArenaManager().getArena(p.getUniqueId()).getSpectators()
                    .contains(p.getUniqueId())) {
                if (isEnabled()) {
                    try {
                        if (!spectate) {
                            playerTeams.put(p.getUniqueId(), team);
                        }

                        if (!spectate) {
                            players.add(p.getUniqueId());
                            getTeamObject(team).addPlayer(p);
                        }
                        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                        p.setScoreboard(this.score);
                        plugin.inventoryManager.setInGameInventory(p);

                        if (spectate) {
                            preSpecGamemodes.put(p.getUniqueId(), p.getGameMode());
                            p.setGameMode(GameMode.SPECTATOR);
                            p.setAllowFlight(true);
                            getSpectators().add(p.getUniqueId());
                            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 127));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 127));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 127));
                            p.sendMessage(Broadcast.TAG + Broadcast.get("arena.spectate")
                                    .replace("%arena%", this.getName()));
                            spectatorTeam.addPlayer(p);
                        }

                        if (!spectate) {
                            Location lobby = getLobbySpawn(team);
                            if (lobby == null) {
                                p.teleport(lobbySpawns.values().iterator().next());
                            }
                            else {
                                p.teleport(lobby);
                            }
                        }
                        else {
                            if (getPlayerSpawns().size() > 0) {
                                p.teleport(getPlayerSpawns().get(0));
                            }
                            else {
                                p.teleport(getTeamSpawns().values().iterator().next().get(0));
                            }
                        }

                        plugin.menuPickTeam.updateMenu(this);

                        if (!spectate) {
                            for (UUID id : getPlayers()) {
                                Player player = Bukkit.getPlayer(id);
                                player.sendMessage(Broadcast.TAG + Broadcast.get("arena.join")
                                        .replace("%player%", team.getChatColour() + p.getName())
                                        .replace("%count%", "(" + getPlayers().size() + "/" + getMaxPlayers() + ")"));
                            }
                        }

                        plugin.signHandler.updateSigns();
                        return true;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                else {
                    p.sendMessage(Broadcast.get("arena.disabled"));
                }
            }
            else {
                p.sendMessage(Broadcast.get("arena.full"));
            }
        }
        return false;
    }

    /**
     * Removes ALL players from the arenas and resets their inventory etc.
     */
    public void removePlayers() {
        for (UUID id : players) {
            Player player = Bukkit.getPlayer(id);
            try {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                plugin.inventoryManager.restoreInventory(player);
                try {
                    player.teleport(plugin.arenaManager.getLobby());
                }
                catch (Exception e) {
                    plugin.getLogger().info("Lobby-spawn not set!");
                }

            }
            catch (Exception ignored) {
            }
        }
        players.clear();

        for (UUID id : getSpectators()) {
            Player player = Bukkit.getPlayer(id);
            try {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                plugin.inventoryManager.restoreInventory(player);
                player.setGameMode(preSpecGamemodes.get(player.getUniqueId()));
                preSpecGamemodes.remove(player.getUniqueId());
                try {
                    player.teleport(plugin.arenaManager.getLobby());
                }
                catch (Exception e) {
                    plugin.getLogger().info("Lobby-spawn not set!");
                }
                finally {
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    player.removePotionEffect(PotionEffectType.WEAKNESS);
                    player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                }

            }
            catch (Exception ignored) {
            }
        }
        spectators.clear();

        for (Team team : scoreboardTeams.values()) {
            for (OfflinePlayer op : team.getPlayers()) {
                team.removePlayer(op);
            }
        }
    }

    /**
     * Gets a list of all players currently in the arenas.
     *
     * @return The players in the arenas
     * @see Arena#players
     */
    public List<UUID> getPlayers() {
        return players;
    }

    /**
     * Removes all possible Item-spawnpoints from the arenas
     */
    public void clearItemSpawns() {
        itemSpawns.clear();
    }

    /**
     * Removes a specific Item-spawnpoint from the arenas
     *
     * @param loc
     *         The spawnpoint-location you'd like to remove
     */
    public void removeItemSpawn(Location loc) {
        itemSpawns.remove(loc);
    }

    /**
     * Removes a specific Item-spawnpoint from the arenas
     *
     * @param index
     *         The spawnpoint-index you'd like to remove
     */
    public void removeItemSpawn(int index) {
        itemSpawns.remove(index);
    }

    /**
     * Adds an Item-spawnpoint to the arenas
     *
     * @param loc
     *         The spawnpoint-location you'd like to add
     */
    public void addItemSpawn(Location loc) {
        itemSpawns.add(loc);
    }

    /**
     * Sets all the Item-spawns for this arenas
     *
     * @param spawns
     *         All spawn-locations
     */
    public void setItemSpawns(List<Location> spawns) {
        itemSpawns = spawns;
    }

    /**
     * Sets all the Item-spawns for this arenas
     *
     * @param spawns
     *         All spawn-locations
     */
    public void setItemSpawns(Location[] spawns) {
        itemSpawns.clear();
        Collections.addAll(itemSpawns, spawns);
    }

    /**
     * Get a list containing all the Item-spawn locations
     *
     * @return All the item-spawn locations
     * @see Arena#itemSpawns
     */
    public List<Location> getItemSpawns() {
        return itemSpawns;
    }

    /**
     * Removes all crystal-spawns from the arenas
     *
     * @see Arena#crystalSpawns
     */
    public void clearCrystalSpawns() {
        crystalSpawns.clear();
    }

    /**
     * Remove a specific crystal-spawn location
     *
     * @param loc
     *         The location you'd like to remove
     * @see Arena#crystalSpawns
     */
    public void removeCrystalSpawn(Location loc) {
        crystalSpawns.remove(loc);
    }

    /**
     * Remove a specific crystal-spawn location
     *
     * @param index
     *         The index in the location-list
     * @see Arena#crystalSpawns
     */
    public void removeCrystalSpawn(int index) {
        crystalSpawns.remove(index);
    }

    /**
     * Add a crystal spawn.
     *
     * @param loc
     *         The location you want to add
     * @see Arena#crystalSpawns
     */
    public void addCrystalSpawn(Location loc) {
        crystalSpawns.add(loc);
    }

    /**
     * Set the crystal Spawns from a list
     *
     * @param spawns
     *         List containing all the spawnpoints
     * @see Arena#crystalSpawns
     */
    public void setCrystalSpawns(List<Location> spawns) {
        crystalSpawns = spawns;
    }

    /**
     * Set the crystal Spawns from an array
     *
     * @param spawns
     *         Array containing all the spawnpoints
     * @see Arena#crystalSpawns
     */
    public void setCrystalSpawns(Location[] spawns) {
        crystalSpawns.clear();
        Collections.addAll(crystalSpawns, spawns);
    }

    /**
     * Get the crystal spawns.
     *
     * @return List containing all the crystal-spawn locations
     * @see Arena#crystalSpawns
     */
    public List<Location> getCrystalSpawns() {
        return crystalSpawns;
    }

    /**
     * Clear playerSpawns.
     *
     * @see Arena#playerSpawns
     */
    public void clearPlayerSpawns() {
        playerSpawns.clear();
    }

    /**
     * Remove a player spawn.
     *
     * @param loc
     *         Location to remove.
     * @see Arena#playerSpawns
     */
    public void removePlayerSpawn(Location loc) {
        playerSpawns.remove(loc);
    }

    /**
     * Remove a player spawn.
     *
     * @param index
     *         Location to remove (index).
     * @see Arena#playerSpawns
     */
    public void removePlayerSpawn(int index) {
        playerSpawns.remove(index);
    }

    /**
     * Add a player spawn.
     *
     * @param loc
     *         A new Player-spawnpoint
     * @see Arena#playerSpawns
     */
    public void addPlayerSpawn(Location loc) {
        playerSpawns.add(loc);
    }

    /**
     * Set the player Spawns
     *
     * @param spawns
     *         A list containing all the player-spawns.
     * @see Arena#playerSpawns
     */
    public void setPlayerSpawns(List<Location> spawns) {
        playerSpawns = spawns;
    }

    /**
     * Set the player Spawns
     *
     * @param spawns
     *         An array containing all the player-spawns.
     * @see Arena#playerSpawns
     */
    public void setPlayerSpawns(Location[] spawns) {
        playerSpawns.clear();
        Collections.addAll(playerSpawns, spawns);
    }

    /**
     * Get the player spawns.
     *
     * @return A list containing all the player-spawnpoints
     * @see Arena#playerSpawns
     */
    public List<Location> getPlayerSpawns() {
        return playerSpawns;
    }

    /**
     * Set the time left in the game.
     *
     * @param timeInSeconds
     *         The time in seconds the game will last
     * @see Arena#timeLeft
     */
    public void setTimeLeft(int timeInSeconds) {
        timeLeft = timeInSeconds;
    }

    /**
     * Get the time left in the arenas.
     *
     * @return The time left in seconds
     * @see Arena#timeLeft
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Checks if the arenas is in-game.
     *
     * @return true if in-game, false if not in-game.
     * @see Arena#inGame
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * Set the in-game status of the game.
     *
     * @see Arena#inGame
     */
    public void setInGame(boolean inGame) {
        this.inGame = inGame;

    }

    /**
     * Get if the countdown is happening.
     *
     * @return True if the countdown has started. False if not.
     * @see Arena#isCounting
     */
    public boolean isCounting() {
        return isCounting;
    }

    /**
     * Set if countdown is happening.
     *
     * @see Arena#isCounting
     */
    public void setIsCounting(boolean isCountingB) {
        isCounting = isCountingB;
        plugin.signHandler.updateSigns();
    }

    /**
     * Set the countdown.
     *
     * @param seconds
     *         The amount of seconds to set the countdown to (-1 for default countdown).
     * @see Arena#count
     */
    public void setCountdown(int seconds) {
        this.count = seconds;
    }

    /**
     * Get the amount of seconds left.
     *
     * @return Seconds left.
     * @see Arena#count
     */
    public int getCountdown() {
        return count;
    }

    /**
     * Set where the lobbyspawn is of a certain team.
     *
     * @param team
     *         The team to set the lobby spawn of.
     * @param location
     *         The location of the lobby spawn.
     */
    public void setLobbySpawn(CrystalQuestTeam team, Location location) {
        if (location == null) {
            lobbySpawns.remove(team);
        }

        lobbySpawns.put(team, location);
    }

    /**
     * Gets the team-lobby spawn of a certain team.
     *
     * @param team
     *         The team to get the lobby spawn of.
     * @return The lobby spawn of the team, or {@code null} when it's not set for the given team.
     * @see Arena#lobbySpawns
     */
    public Location getLobbySpawn(CrystalQuestTeam team) {
        return lobbySpawns.get(team);
    }

    /**
     * Checks if the lobbyspawns for all arenas are set.
     *
     * @return {@code true} when all team lobby spawns are set, {@code false} when at least one team
     * lobby is not setup correctly.
     */
    public boolean areLobbySpawnsSet() {
        for (CrystalQuestTeam team : getTeams()) {
            Location lobbyspawn = getLobbySpawn(team);
            if (lobbyspawn == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Toggles the crystalquest.vip requirement on the arenas.
     *
     * @param isVip
     *         True if VIP is needed. False if VIP is not needed.
     * @see Arena#vip
     */
    public void setVip(boolean isVip) {
        this.vip = isVip;
    }

    /**
     * Checks if it is an arenas only for people with the crystalquest.vip node.
     *
     * @return True if it is VIP-only, False if it isn't
     * @see Arena#vip
     */
    public boolean isVip() {
        return vip;
    }

    /**
     * Gets the arenas ID.
     *
     * @return The arenas ID
     * @see Arena#id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the arenas.
     *
     * @return Arena-name
     * @see Arena#name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the arenas.
     *
     * @param name
     *         The new arenas-name.
     * @return true when the name is succesfully applied, false when the name already exists.
     * @see Arena#name
     */
    public boolean setName(String name) {
        // Lol. I didn't bother to use regex.
        // You gotta admire young people's resourcefulness.
        try {
            Integer.parseInt(name);
            return false;
        }
        catch (Exception ignored) {
        }

        if (plugin.arenaManager.getArena(name) == null) {
            this.name = name;
            this.teamMenu = Bukkit.createInventory(null, 9, "Pick Team: " + this.getName());
            plugin.menuPickTeam.updateMenu(this);
            plugin.signHandler.updateSigns();
            return true;
        }

        return false;
    }

    /**
     * Set the teams that can be joined in this arena.
     *
     * @param teams
     *         Collection containing all the team possibilities for the arena. Can be at most 8
     *         teams and must contain at least 2 teams.
     * @throws IllegalArgumentException
     *         When you entered more than 8 teams, or fewer than 2.
     */
    public void setTeams(Collection<CrystalQuestTeam> teams) throws IllegalArgumentException {
        if (teams.size() > 8) {
            throw new IllegalArgumentException("You can only have 8 teams per arena!");
        }

        if (teams.size() < 2) {
            throw new IllegalArgumentException("The arena must have at least 2 teams.");
        }

        this.teams.clear();
        this.teams.addAll(teams);

        initializeScoreboard();
    }

    /**
     * Set the teams that can be joined in this arena.
     *
     * @param team1
     *         The first team.
     * @param team2
     *         The second team.
     * @param other
     *         All the other teams, must be at most 6.
     * @throws IllegalArgumentException
     *         When you entered more than 8 teams.
     */
    public void setTeams(CrystalQuestTeam team1, CrystalQuestTeam team2,
                         CrystalQuestTeam... other) throws IllegalArgumentException {
        if (other.length > 6) {
            throw new IllegalArgumentException("Amount of teams must be at most 8!");
        }

        this.teams.clear();
        this.teams.add(team1);
        this.teams.add(team2);
        Collections.addAll(this.teams, other);

        initializeScoreboard();
    }

    /**
     * Returns the amount of teams available for the arenas.
     *
     * @return The amount of teams of the arenas.
     * @see Arena#teams
     */
    public int getTeamCount() {
        return teams.size();
    }

    /**
     * Get the minimum amount of players for an arenas to start.
     *
     * @return The minimum amount of players to start.
     * @see Arena#minPlayers
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Set the minimum amount of players for an arenas to start.
     *
     * @param minPlayers
     *         The minimum amount of players.
     * @see Arena#minPlayers
     */
    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        plugin.signHandler.updateSigns();
    }

    /**
     * Get the maximum amount of players for an arenas to start.
     *
     * @return The maximum amount of players to start.
     * @see Arena#maxPlayers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Set the maximum amount of players for an arenas to start.
     *
     * @param maxPlayers
     *         The maximum amount of players.
     * @see Arena#maxPlayers
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        plugin.signHandler.updateSigns();
    }

    /**
     * Start the game!
     */
    public void startGame() {
        ArenaStartEvent e = new ArenaStartEvent(this);
        Bukkit.getPluginManager().callEvent(e);

        if (e.isCancelled()) {
            return;
        }

        for (UUID id : getPlayers()) {
            Player player = Bukkit.getPlayer(id);
            plugin.inventoryManager.setClassInventory(player);
            player.sendMessage(Broadcast.TAG + Broadcast.get("arena.started"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 20F, 20F);
            player.sendMessage(Broadcast.TAG + Broadcast.get("arena.using-class")
                    .replace("%class%", SMethods.setColours(plugin.getConfig().getString(
                            "kit." + plugin.inventoryManager.playerClass.get(player.getUniqueId()) + ".name"))));
        }

        setInGame(true);

        Random ran = new Random();

        for (UUID id : getPlayers()) {
            Player player = Bukkit.getPlayer(id);
            boolean isTeamSpawns = false;
            for (int i = 0; i < getTeamCount(); i++) {
                if (getTeamSpawns().get(CrystalQuestTeam.valueOf(i)).size() > 0) {
                    isTeamSpawns = true;
                }
            }
            if (isTeamSpawns) {
                CrystalQuestTeam team = getTeam(player);
                player.teleport(getTeamSpawns().get(team).get(ran.nextInt(getTeamSpawns().get(team).size())));
            }
            else {
                player.teleport((getPlayerSpawns().get(ran.nextInt(getPlayerSpawns().size()))));
            }
        }

        plugin.signHandler.updateSigns();

        Collection<CrystalQuestTeam> allPresentTeams = playerTeams.values();
        for (CrystalQuestTeam team : getTeams()) {
            if (!allPresentTeams.contains(team)) {
                points.getScoreboard().resetScores(team.toString());
            }
        }
    }

    /**
     * Get the score of the given team.
     *
     * @param team
     *         The team to get the score of.
     * @return The score of the given team.
     * @throws IllegalStateException
     *         When the given team is not present in the arena.
     */
    public Score getScoreObject(CrystalQuestTeam team) throws IllegalStateException {
        if (!hasTeam(team)) {
            throw new IllegalStateException("CrystalQuestTeam " + team.getName() + " is not " +
                    "present in the arena.");
        }

        return scoreboardScores.get(team);
    }

    /**
     * Get the team object (scoreboard) of a given team.
     *
     * @param team
     *         The team to get the scoreboard score of.
     * @return The scoreboard Team corresponding to the given CrystalQuest Team.
     * @throws IllegalStateException
     *         When the given team is not present in the arena.
     */
    public Team getTeamObject(CrystalQuestTeam team) throws IllegalStateException {
        if (!hasTeam(team)) {
            throw new IllegalStateException("CrystalQuestTeam " + team.getName() + " is not " +
                    "present in the arena.");
        }

        return scoreboardTeams.get(team);
    }

    /**
     * Adds points to a team
     *
     * @param team
     *         The team to add the score to.
     * @param scoreToAdd
     *         The points to add.
     */
    public void addScore(CrystalQuestTeam team, int scoreToAdd) {
        Score score = getScoreObject(team);
        score.setScore(score.getScore() + scoreToAdd);
    }

    /**
     * Sets the score of a team.
     *
     * @param team
     *         The team to get the score of.
     * @param newScore
     *         The new score.
     */
    public void setScore(CrystalQuestTeam team, int newScore) {
        Score score = getScoreObject(team);
        score.setScore(newScore);
    }

    /**
     * Gets the score of a team
     *
     * @param team
     *         The team to get the score of.
     * @return The score of the given team.
     */
    public int getScore(CrystalQuestTeam team) {
        if (getScoreboardTeams() == null) {
            return Integer.MIN_VALUE;
        }

        Score score = getScoreObject(team);
        return score.getScore();
    }

    /**
     * @deprecated Uses the old team system. There is no alternative however.
     */
    @Deprecated
    public List<Location> getLobbySpawns() {
        return getTeams().stream()
                .map(this::getLobbySpawn)
                .collect(Collectors.toList());
    }
}