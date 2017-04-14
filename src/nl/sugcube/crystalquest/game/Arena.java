package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.events.ArenaStartEvent;
import nl.sugcube.crystalquest.events.PlayerJoinArenaEvent;
import nl.sugcube.crystalquest.events.PlayerLeaveArenaEvent;
import nl.sugcube.crystalquest.events.TeamWinGameEvent;
import nl.sugcube.crystalquest.sba.SMeth;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SugarCaney
 */
public class Arena {

    /**
     * The main plugin instance.
     */
    public CrystalQuest plugin;

    /**
     * The maximum amount of players in the arena.
     * <p>
     * {@code -1} when this value is not set.
     */
    private int maxPlayers = -1;

    /**
     * The minimum amount of players in the arena.
     * <p>
     * {@code -1} when this value is not set.
     */
    private int minPlayers = -1;

    /**
     * The amount of teams that participate in the arena.
     */
    private int teams = -1;

    /**
     * The name of the arena.
     * <p>
     * {@code ""} when there is no name set.
     */
    private String name = "";

    /**
     * The unique id of the arena.
     */
    private int id;

    /**
     * Determines if the crystalquest.vip permission is required in order to play in the arena.
     * <p>
     * {@code true} when required, {@code false} when not required.
     */
    private boolean vip;

    /**
     * Array containing per team where the lobby spawn is. The value is mapped to the id of the
     * team.
     * <p>
     * TODO: Convert to map.
     */
    private Location[] lobbySpawn = new Location[8];

    /**
     * The amount of seconds it takes before the arena starts.
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
     * Contains whether the arena is in game or not.
     * <p>
     * {@code true} when the arena is ingame, {@code false} otherwise.
     */
    private boolean inGame;

    /**
     * The amount of seconds that is left in the game.
     */
    private int timeLeft;

    /**
     * Whether the arena is enabled or disabled.
     * <p>
     * {@code true} when the arena is enabled, {@code false} when disabled.
     */
    private boolean enabled = true;

    /**
     * The amount of seconds that is left in the resetting phase.
     */
    private int afterCount;

    /**
     * Whether the game is over and is resetting or not.
     * <p>
     * {@code true} when the arena is resetting, {@code false} otherwise.
     */
    private boolean isEndGame = false;

    /**
     * The two corner positions of the arena that define the protection.
     */
    private Location[] protection = new Location[2];

    /**
     * Whether double jump is enabled in this arena or not.
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
     * List of all the wolfs that have been spawned in the arena.
     */
    private List<Wolf> gameWolfs = new ArrayList<>();

    /**
     * List of all creepers that have been spawned in the arena.
     */
    private List<Creeper> gameCreepers = new ArrayList<>();

    /**
     * List of all crystals that have been spawned in the arena.
     */
    private List<Entity> gameCrystals = new ArrayList<>();

    /**
     * List of all UUIDs of players that are in the arena.
     */
    private List<UUID> players = new ArrayList<>();

    /**
     * List of all UUIDs of players that are spectating the arena.
     */
    private List<UUID> spectators = new ArrayList<>();

    /**
     * Map that maps every player's UUID to the id of the team which they are part of.
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam}
     */
    private Map<UUID, Integer> playerTeams = new ConcurrentHashMap<>();

    /**
     * Map that maps every ender crystal to their location.
     */
    private Map<Entity, Location> crystalLocations = new ConcurrentHashMap<>();

    /**
     * List of all blocks that are placed during the game in the arena.
     */
    private List<Block> gameBlocks = new ArrayList<>();

    /**
     * Map that maps the team ID to a list of all locations where a team can spawn.
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam}
     */
    private Map<Integer, List<Location>> teamSpawns = new ConcurrentHashMap<>();

    /**
     * Map that maps all locations of placed landmines to the UUID of the player that placed it.
     */
    private Map<Location, UUID> landmines = new ConcurrentHashMap<>();

    /**
     * Map that maps the UUID of a player to the gamemode which they had before entering the arena.
     */
    private Map<UUID, GameMode> preSpecGamemodes = new ConcurrentHashMap<>();

    /**
     * The menu that shows up when players want to select a team when joining the arena.
     */
    private Inventory teamMenu;

    //Scoreboard
    /**
     * Scoreboard: Array of teams by their team id.
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam} mapping.
     */
    private Team[] sTeams;

    /**
     * Scoreboard: The object used to track the points earned.
     */
    private Objective points;

    /**
     * Scoreboard: Array of scores of teams by their team id.
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam} mapping.
     */
    private Score[] sScore;

    /**
     * Scoreboard: Team that contains all the spectators.
     */
    private Team spectatorTeam;

    /**
     * @param instance
     *         Instance of the plugin
     * @param arenaId
     *         ID of the arena
     */
    public Arena(CrystalQuest instance, int arenaId) {
        this.plugin = instance;
        this.score = Bukkit.getScoreboardManager().getNewScoreboard();
        this.timeLeft = plugin.getConfig().getInt("arena.game-length");
        this.count = plugin.getConfig().getInt("arena.countdown");
        this.id = arenaId;
        this.afterCount = plugin.getConfig().getInt("arena.after-count");

        for (int i = 0; i <= 7; i++) {
            this.teamSpawns.put(i, new ArrayList<>());
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
     * Sets if the arena accepts double jumps
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
     * Gets the hashmap with the teamId bound to the list containing the spawnpoints
     *
     * @see Arena#teamSpawns
     */
    public Map<Integer, List<Location>> getTeamSpawns() {
        return teamSpawns;
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
     * Checks if the arena is full
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
     * Sends a custom message to all players in the arena
     *
     * @param player
     *         The dead player
     * @param message
     *         The verb that will show up. fe: Killed, Gibbed etc.
     */
    public void sendDeathMessage(Player player, String message) {
        int teamId = plugin.getArenaManager().getTeam(player);
        ChatColor colour = Teams.getTeamChatColour(teamId);
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
     * Sends a death message to all players in the arena with a custom verb
     *
     * @param dead
     *         The dead player
     * @param killer
     *         The player who killed dead
     * @param verb
     *         The verb that will show up. fe: Killed, Gibbed etc.
     */
    public void sendDeathMessage(Player dead, Player killer, String verb) {
        int teamId = plugin.getArenaManager().getTeam(dead);
        int teamIdKiller = plugin.getArenaManager().getTeam(killer);
        ChatColor c = Teams.getTeamChatColour(teamId);
        ChatColor cK = Teams.getTeamChatColour(teamIdKiller);
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
     * Sends a death message to all players in the arena
     *
     * @param dead
     *         The dead player
     * @param killer
     *         The player who killed dead
     */
    public void sendDeathMessage(Player dead, Player killer) {
        int teamId = plugin.getArenaManager().getTeam(dead);
        int teamIdKiller = plugin.getArenaManager().getTeam(killer);
        ChatColor c = Teams.getTeamChatColour(teamId);
        ChatColor cK = Teams.getTeamChatColour(teamIdKiller);
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
     * Sends a death message to all players in the arena
     *
     * @param player
     *         The dead player
     */
    public void sendDeathMessage(Player player) {
        int teamId = plugin.getArenaManager().getTeam(player);
        ChatColor c = Teams.getTeamChatColour(teamId);
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
     * Get the list containing all the crystals that have spawned in the arena
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
     * Sets the the arena is in the end-game phase
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
     * Get the teams in the arena
     *
     * @return The teams in the arena
     * @see Arena#sTeams
     */
    public Team[] getScoreboardTeams() {
        return sTeams;
    }

    /**
     * Checks if the arena is enabled/disabled.
     *
     * @return If enabled true, if disabled false
     * @see Arena#enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the state of the arena.
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
                SMeth.setColours(
                        "&c" + Broadcast.get("arena.time-left") + " &f" + SMeth.toTime(timeLeft)
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
     * <p>
     * TODO: Change to use {@link CrystalQuestTeam}.
     *
     * @return The teams with the least amount of players.
     */
    public List<Integer> getSmallestTeams() {
        List<Integer> list = new ArrayList<>();

        int least = 999999;
        for (int i = 0; i < this.getTeamCount(); i++) {
            if (this.getScoreboardTeams()[i].getPlayers().size() < least) {
                least = this.getScoreboardTeams()[i].getPlayers().size();
            }
        }

        int count = 0;
        for (Team t : this.getScoreboardTeams()) {
            if (t.getPlayers().size() == least && count < this.getTeamCount()) {
                list.add(count);
            }
            count++;
        }

        return list;
    }

    /**
     * Initializes the scoreboard. This makes or updates the scoreboard.
     * <p>
     * TODO: Change to {@link CrystalQuestTeam} constants.
     */
    public void initializeScoreboard() {
        score = Bukkit.getScoreboardManager().getNewScoreboard();
        sTeams = new Team[8];
        sScore = new Score[8];

        spectatorTeam = score.registerNewTeam("Spectate");
        spectatorTeam.setAllowFriendlyFire(false);
        spectatorTeam.setCanSeeFriendlyInvisibles(true);
        spectatorTeam.setPrefix(ChatColor.BLUE + "[Spec] ");
        sTeams[0] = score.registerNewTeam("Green");
        sTeams[0].setPrefix(ChatColor.GREEN + "");
        sTeams[1] = score.registerNewTeam("Orange");
        sTeams[1].setPrefix(ChatColor.GOLD + "");
        sTeams[2] = score.registerNewTeam("Yellow");
        sTeams[2].setPrefix(ChatColor.YELLOW + "");
        sTeams[3] = score.registerNewTeam("Red");
        sTeams[3].setPrefix(ChatColor.RED + "");
        sTeams[4] = score.registerNewTeam("Blue");
        sTeams[4].setPrefix(ChatColor.AQUA + "");
        sTeams[5] = score.registerNewTeam("Magenta");
        sTeams[5].setPrefix(ChatColor.LIGHT_PURPLE + "");
        sTeams[6] = score.registerNewTeam("White");
        sTeams[6].setPrefix(ChatColor.WHITE + "");
        sTeams[7] = score.registerNewTeam("Black");
        sTeams[7].setPrefix(ChatColor.BLACK + "");

        for (int i = 0; i <= 7; i++) {
            sTeams[i].setAllowFriendlyFire(false);
        }

        points = score.registerNewObjective("points", "dummy");
        points.setDisplaySlot(DisplaySlot.SIDEBAR);
        updateTimer();

        sScore[0] = points.getScore(Teams.GREEN);
        sScore[1] = points.getScore(Teams.ORANGE);
        if (teams >= 3) {
            sScore[2] = points.getScore(Teams.YELLOW);
        }
        if (this.teams >= 4) {
            sScore[3] = points.getScore(Teams.RED);
        }
        if (teams >= 5) {
            sScore[4] = points.getScore(Teams.BLUE);
        }
        if (teams >= 6) {
            sScore[5] = points.getScore(Teams.MAGENTA);
        }
        if (teams >= 7) {
            sScore[6] = points.getScore(Teams.WHITE);
        }
        if (this.teams >= 8) {
            sScore[7] = points.getScore(Teams.BLACK);
        }

        for (Score s : sScore) {
            if (s != null) {
                s.setScore(0);
            }
        }
    }

    /**
     * Gets the amount of players in a team.
     * <p>
     * TODO: Change to {@link CrystalQuestTeam}.
     *
     * @param teamId
     *         The ID of the team.
     * @return The amount of players.
     */
    public int getTeamPlayerCount(int teamId) {
        int ps = 0;
        Iterator<Entry<UUID, Integer>> it = playerTeams.entrySet().iterator();
        while (it.hasNext()) {
            Entry<UUID, Integer> pairs = it.next();
            if (pairs.getValue() == teamId) {
                ps++;
            }
            it.remove();
        }
        return ps;
    }

    /**
     * Gets the team the player is in.
     * <p>
     * TODO: Change to {@link CrystalQuestTeam}.
     *
     * @param player
     *         The player from whose you want to know the team he/she is in.
     * @return The TeamId of the team the player is in.
     */
    public int getTeam(Player player) {
        return playerTeams.get(player.getUniqueId());
    }

    /**
     * Checks if the player is in the specific team.
     *
     * @param player
     *         The player you want to check for.
     * @param teamId
     *         The ID of the team.
     * @return true if they're in, false if they aren't.
     */
    public boolean isInTeam(Player player, int teamId) {
        return playerTeams.get(player.getUniqueId()) == teamId;
    }

    /**
     * If there are players in the arena
     * Reveal the winner
     *
     * @return Winning team's name
     */
    public String declareWinner() {
        if (!getPlayers().isEmpty()) {
            int highest = -99999;
            Score hScore = null;
            for (Score score : this.sScore) {
                if (score != null) {
                    if (score.getScore() > highest) {
                        highest = score.getScore();
                        hScore = score;
                    }
                }
            }

            String winningTeam = "";
            int teamId = 0;
            ChatColor colour = null;
            List<UUID> winningPlayers = new ArrayList<>();

            if (hScore.getPlayer().getName().equalsIgnoreCase(Teams.GREEN_NAME)) {
                winningTeam = Teams.GREEN_NAME;
                colour = ChatColor.GREEN;
                teamId = 0;
            }
            else if (hScore.getPlayer().getName().equalsIgnoreCase(Teams.ORANGE_NAME)) {
                winningTeam = Teams.ORANGE_NAME;
                colour = ChatColor.GOLD;
                teamId = 1;
            }
            else if (hScore.getPlayer().getName().equalsIgnoreCase(Teams.YELLOW_NAME)) {
                winningTeam = Teams.YELLOW_NAME;
                colour = ChatColor.YELLOW;
                teamId = 2;
            }
            else if (hScore.getPlayer().getName().equalsIgnoreCase(Teams.RED_NAME)) {
                winningTeam = Teams.RED_NAME;
                colour = ChatColor.RED;
                teamId = 3;
            }
            else if (hScore.getPlayer().getName().equalsIgnoreCase(Teams.BLUE_NAME)) {
                winningTeam = Teams.BLUE_NAME;
                colour = ChatColor.AQUA;
                teamId = 4;
            }
            else if (hScore.getPlayer().getName().equalsIgnoreCase(Teams.MAGENTA_NAME)) {
                winningTeam = Teams.MAGENTA_NAME;
                colour = ChatColor.LIGHT_PURPLE;
                teamId = 5;
            }
            else if (hScore.getPlayer().getName().equalsIgnoreCase(Teams.WHITE_NAME)) {
                winningTeam = Teams.WHITE_NAME;
                colour = ChatColor.WHITE;
                teamId = 6;
            }
            else if (hScore.getPlayer().getName().equalsIgnoreCase(Teams.BLACK_NAME)) {
                winningTeam = Teams.BLACK_NAME;
                colour = ChatColor.DARK_GRAY;
                teamId = 7;
            }

            for (UUID id : this.getPlayers()) {
                Player p = Bukkit.getPlayer(id);
                p.sendMessage(colour + "<>---------------------------<>");
                p.sendMessage("    " + winningTeam + " " + Broadcast.get("arena.won"));
                p.sendMessage(colour + "<>---------------------------<>");

                if (plugin.am.getTeam(p) == teamId) {
                    winningPlayers.add(p.getUniqueId());
                }
            }

            Bukkit.getPluginManager().callEvent(new TeamWinGameEvent(winningPlayers, this,
                    teamId, getTeamCount(), sTeams, winningTeam));

            return winningTeam;
        }

        return "";
    }

    /**
     * Chooses a random player in the arena and from another team.
     *
     * @param excluded
     *         The player whose team cannot be chosen.
     * @return The chosen player. Null if there are no players to choose from.
     */
    public Player getRandomPlayer(Player excluded) {
        int team = getTeam(excluded);
        List<Player> toChoose = new ArrayList<>();
        for (UUID id : players) {
            Player p = Bukkit.getPlayer(id);
            if (getTeam(p) != team) {
                toChoose.add(p);
            }
        }

        if (toChoose.size() == 0) {
            return null;
        }
        else {
            return toChoose.get(ran.nextInt(toChoose.size()));
        }
    }

    /**
     * Resets Arena-properties to default. Containing:
     * Countdown,
     * Game-time,
     * Removes the players,
     * Re-initializes scoreboard.
     * Sends a "this-team-won" message
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
                List<Block> toRemove = new ArrayList<>();
                toRemove.addAll(getGameBlocks());
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
                        if (plugin.prot.isInProtectedArena(e.getLocation())) {
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
     * Get the scoreboard of the arena.
     *
     * @return Scoreboard of the arena
     * @see Arena#score
     */
    public Scoreboard getScoreboard() {
        return score;
    }

    /**
     * Removes a player from the arena including removal from the team,
     * resetting his/her scoreboard and restoring his/her inventory.
     *
     * @param p
     *         The player you want to remove from the arena.
     */
    public void removePlayer(Player p) {
        if (!this.spectators.contains(p.getUniqueId())) {
            for (UUID id : getPlayers()) {
                Player player = Bukkit.getPlayer(id);
                player.sendMessage(Broadcast.TAG + Broadcast.get("arena.leave")
                        .replace("%player%", Teams.getTeamChatColour(getTeam(p)) + p.getName())
                        .replace("%count%", "(" + (getPlayers().size() - 1) + "/" + getMaxPlayers() + ")"));
            }
        }

        this.players.remove(p.getUniqueId());
        for (Team team : sTeams) {
            if (team.hasPlayer((OfflinePlayer)p)) {
                team.removePlayer((OfflinePlayer)p);
            }
        }

        for (PotionEffect potionEffect : p.getActivePotionEffects()) {
            p.removePotionEffect(potionEffect.getType());
        }

        try {
            p.teleport(plugin.am.getLobby());
        }
        catch (Exception e) {
            plugin.getLogger().info("Lobby-spawn not set!");
        }

        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        playerTeams.remove(p.getUniqueId());
        plugin.im.restoreInventory(p);
        plugin.im.playerClass.remove(p.getUniqueId());
        this.spectators.remove(p.getUniqueId());

        if (p.getGameMode() != GameMode.CREATIVE) {
            p.setAllowFlight(false);
        }

        plugin.ab.getAbilities().remove(p.getUniqueId());
        p.setFireTicks(0);
        Bukkit.getPluginManager().callEvent(new PlayerLeaveArenaEvent(p, this));

        if (spectatorTeam.getPlayers().contains(p)) {
            spectatorTeam.removePlayer(p);
        }

        plugin.signHandler.updateSigns();
    }

    /**
     * Adds a player to the arena including putting into a team, set the
     * scoreboard and give the in-game inventory.
     *
     * @param p
     *         The player to add
     * @param teamId
     *         The team to put the player in
     * @param spectate
     *         True if the player is spectating
     * @return True if joined, False if not joined
     */
    public boolean addPlayer(Player p, int teamId, boolean spectate) {
        PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(p, this, spectate);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (!isFull() || plugin.getArenaManager().getArena(p.getUniqueId()).getSpectators()
                    .contains(p.getUniqueId())) {
                if (isEnabled()) {
                    try {
                        playerTeams.put(p.getUniqueId(), spectate ? -1 : teamId);

                        if (!spectate) {
                            players.add(p.getUniqueId());
                            sTeams[teamId].addPlayer((OfflinePlayer)p);
                        }
                        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                        p.setScoreboard(this.score);
                        plugin.im.setInGameInventory(p);

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
                            try {
                                if (getLobbySpawns()[teamId] == null) {
                                    p.teleport(getLobbySpawns()[0]);
                                }
                                else {
                                    p.teleport(getLobbySpawns()[teamId]);
                                }
                            }
                            catch (Exception ignored) {
                            }
                        }
                        else {
                            if (getPlayerSpawns().size() > 0) {
                                p.teleport(getPlayerSpawns().get(0));
                            }
                            else {
                                p.teleport(getTeamSpawns().get(0).get(0));
                            }
                        }

                        plugin.menuPT.updateMenu(this);

                        if (!spectate) {
                            for (UUID id : getPlayers()) {
                                Player player = Bukkit.getPlayer(id);
                                player.sendMessage(Broadcast.TAG + Broadcast.get("arena.join")
                                        .replace("%player%", Teams.getTeamChatColour(teamId) + p.getName())
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
     * Removes ALL players from the arena and resets their inventory etc.
     */
    public void removePlayers() {
        for (UUID id : players) {
            Player player = Bukkit.getPlayer(id);
            try {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                plugin.im.restoreInventory(player);
                try {
                    player.teleport(plugin.am.getLobby());
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
                plugin.im.restoreInventory(player);
                player.setGameMode(preSpecGamemodes.get(player.getUniqueId()));
                preSpecGamemodes.remove(player.getUniqueId());
                try {
                    player.teleport(plugin.am.getLobby());
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

        for (Team team : sTeams) {
            for (OfflinePlayer op : team.getPlayers()) {
                team.removePlayer(op);
            }
        }
    }

    /**
     * Gets a list of all players currently in the arena.
     *
     * @return The players in the arena
     * @see Arena#players
     */
    public List<UUID> getPlayers() {
        return players;
    }

    /**
     * Removes all possible Item-spawnpoints from the arena
     */
    public void clearItemSpawns() {
        itemSpawns.clear();
    }

    /**
     * Removes a specific Item-spawnpoint from the arena
     *
     * @param loc
     *         The spawnpoint-location you'd like to remove
     */
    public void removeItemSpawn(Location loc) {
        itemSpawns.remove(loc);
    }

    /**
     * Removes a specific Item-spawnpoint from the arena
     *
     * @param index
     *         The spawnpoint-index you'd like to remove
     */
    public void removeItemSpawn(int index) {
        itemSpawns.remove(index);
    }

    /**
     * Adds an Item-spawnpoint to the arena
     *
     * @param loc
     *         The spawnpoint-location you'd like to add
     */
    public void addItemSpawn(Location loc) {
        itemSpawns.add(loc);
    }

    /**
     * Sets all the Item-spawns for this arena
     *
     * @param spawns
     *         All spawn-locations
     */
    public void setItemSpawns(List<Location> spawns) {
        itemSpawns = spawns;
    }

    /**
     * Sets all the Item-spawns for this arena
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
     * Removes all crystal-spawns from the arena
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
        if (playerSpawns.contains(loc)) {
            playerSpawns.remove(loc);
        }
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
     * Get the time left in the arena.
     *
     * @return The time left in seconds
     * @see Arena#timeLeft
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Checks if the arena is in-game.
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
     * Sets the lobby spawns of the teams.
     * The team-id (0, 1, 2, 3, 4, 5, 6, 7) represents the spawn of the specific team.
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam}.
     *
     * @param locations
     *         The locations.
     * @see Arena#lobbySpawn
     */
    public void setLobbySpawns(Location[] locations) {
        this.lobbySpawn = locations;
    }

    /**
     * Gets the team-lobby spawns of the arena.
     * The team-id (0, 1, 2, 3, 4, 5, 6, 7) represents the spawn of the specific team.
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam}
     *
     * @return The lobbyspawn-array
     * @see Arena#lobbySpawn
     */
    public Location[] getLobbySpawns() {
        return lobbySpawn;
    }

    /**
     * Toggles the crystalquest.vip requirement on the arena.
     *
     * @param isVip
     *         True if VIP is needed. False if VIP is not needed.
     * @see Arena#vip
     */
    public void setVip(boolean isVip) {
        this.vip = isVip;
    }

    /**
     * Checks if it is an arena only for people with the crystalquest.vip node.
     *
     * @return True if it is VIP-only, False if it isn't
     * @see Arena#vip
     */
    public boolean isVip() {
        return vip;
    }

    /**
     * Gets the arena ID.
     *
     * @return The arena ID
     * @see Arena#id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the arena.
     *
     * @return Arena-name
     * @see Arena#name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the arena.
     *
     * @param name
     *         The new arena-name.
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

        if (plugin.am.getArena(name) == null) {
            this.name = name;
            this.teamMenu = Bukkit.createInventory(null, 9, "Pick Team: " + this.getName());
            plugin.menuPT.updateMenu(this);
            plugin.signHandler.updateSigns();
            return true;
        }

        return false;
    }

    /**
     * Sets the amount of teams available for the arena.
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam}.
     *
     * @param amountOfTeams
     *         The amount of teams used to play this arena.
     * @return true if applied succesful, false if amountOfTeams is greater than 6.
     */
    public boolean setTeams(int amountOfTeams) {
        if (amountOfTeams > 8) {
            return false;
        }

        this.teams = amountOfTeams;
        return true;
    }

    /**
     * Returns the amount of teams available for the arena.
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam}.
     *
     * @return The amount of teams of the arena.
     * @see Arena#teams
     */
    public int getTeamCount() {
        return teams;
    }

    /**
     * Get the minimum amount of players for an arena to start.
     *
     * @return The minimum amount of players to start.
     * @see Arena#minPlayers
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Set the minimum amount of players for an arena to start.
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
     * Get the maximum amount of players for an arena to start.
     *
     * @return The maximum amount of players to start.
     * @see Arena#maxPlayers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Set the maximum amount of players for an arena to start.
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

        if (!e.isCancelled()) {
            for (UUID id : getPlayers()) {
                Player player = Bukkit.getPlayer(id);
                plugin.im.setClassInventory(player);
                player.sendMessage(Broadcast.TAG + Broadcast.get("arena.started"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 20F, 20F);
                player.sendMessage(Broadcast.TAG + Broadcast.get("arena.using-class")
                        .replace("%class%", SMeth.setColours(plugin.getConfig().getString(
                                "kit." + plugin.im.playerClass.get(player.getUniqueId()) + ".name"))));
            }

            setInGame(true);

            Random ran = new Random();

            for (UUID id : getPlayers()) {
                Player player = Bukkit.getPlayer(id);
                boolean isTeamSpawns = false;
                for (int i = 0; i < getTeamCount(); i++) {
                    if (getTeamSpawns().get(i).size() > 0) {
                        isTeamSpawns = true;
                    }
                }
                if (isTeamSpawns) {
                    int team = getTeam(player);
                    player.teleport(getTeamSpawns().get(team).get(ran.nextInt(getTeamSpawns().get(team).size())));
                }
                else {
                    player.teleport((getPlayerSpawns().get(ran.nextInt(getPlayerSpawns().size()))));
                }
            }

            plugin.signHandler.updateSigns();
        }

    }

    /**
     * Adds points to a team
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam}.
     *
     * @param teamId
     *         The team-ID of the team.
     * @param score
     *         The points to add.
     */
    public void addScore(int teamId, int score) {
        sScore[teamId].setScore(sScore[teamId].getScore() + score);
    }

    /**
     * Sets the score of a team
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam}.
     *
     * @param teamId
     *         The team-ID of the team.
     * @param score
     *         The new score.
     */
    public void setScore(int teamId, int score) {
        sScore[teamId].setScore(score);
    }

    /**
     * Gets the score of a team
     * <p>
     * TODO: Replace by {@link CrystalQuestTeam}.
     *
     * @param teamId
     *         The team-ID of the team.
     * @return The score
     */
    public int getScore(int teamId) {
        if (this.sScore == null) {
            return Integer.MIN_VALUE;
        }

        return this.sScore[teamId].getScore();
    }
}