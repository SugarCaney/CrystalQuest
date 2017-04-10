package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.Teams;
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
@SuppressWarnings("unused")
public class Arena {

    /*
     * TEAM_ID=0: GREEN
     * TEAM_ID=1: ORANGE
     * TEAM_ID=2: YELLOW
     * TEAM_ID=3: RED
     * TEAM_ID=4: BLUE
     * TEAM_ID=5: MAGENTA
     * TEAM_ID=6: WHITE
     * TEAM_ID=7: BLACK
     */
    public CrystalQuest plugin;
    private int maxPlayers = -1;
    private int minPlayers = -1;
    private int teams = -1;
    private String name = "";
    private int id;
    private boolean vip;
    private Location[] lobbySpawn;
    private int count;                    //seconds
    private boolean isCounting;
    private Scoreboard score;
    private boolean inGame;
    private int timeLeft;                //seconds
    private boolean isReady;
    private boolean enabled;
    private int afterCount;
    private boolean isEndGame;
    private Location[] protection;
    private boolean doubleJump;
    private Random ran = new Random();

    private List<Wolf> gameWolfs;
    private List<Creeper> gameCreepers;
    private List<Entity> gameCrystals;
    private List<Location> playerSpawns;
    private List<Location> crystalSpawns;
    private List<Location> itemSpawns;
    private List<UUID> players;
    private List<UUID> spectators;
    private ConcurrentHashMap<UUID, Integer> playerTeams;
    private ConcurrentHashMap<Entity, Location> crystalLocations;
    private List<Block> gameBlocks;
    private ConcurrentHashMap<Integer, List<Location>> teamSpawns;
    private ConcurrentHashMap<Location, UUID> landmines;
    private ConcurrentHashMap<UUID, GameMode> preSpecGamemodes;

    private Inventory teamMenu;

    //Scoreboard
    private Team[] sTeams;
    private Objective points;
    private Score[] sScore;
    private Team spectatorTeam;

    /**
     * CONSTRUCTOR
     *
     * @param instance
     *         (CrystalQuest) Instance of the plugin
     * @param arenaId
     *         (int) ID of the arena
     * @return void
     */
    public Arena(CrystalQuest instance, int arenaId) {
        this.plugin = instance;
        this.score = Bukkit.getScoreboardManager().getNewScoreboard();
        this.playerSpawns = new ArrayList<Location>();
        this.crystalSpawns = new ArrayList<Location>();
        this.itemSpawns = new ArrayList<Location>();
        this.players = new ArrayList<UUID>();
        this.spectators = new ArrayList<UUID>();
        this.playerTeams = new ConcurrentHashMap<UUID, Integer>();
        this.preSpecGamemodes = new ConcurrentHashMap<UUID, GameMode>();
        this.timeLeft = plugin.getConfig().getInt("arena.game-length");
        this.count = plugin.getConfig().getInt("arena.countdown");
        this.isReady = false;
        this.id = arenaId;
        this.enabled = true;
        this.lobbySpawn = new Location[8];
        this.isEndGame = false;
        this.afterCount = plugin.getConfig().getInt("arena.after-count");
        this.gameCrystals = new ArrayList<Entity>();
        this.crystalLocations = new ConcurrentHashMap<Entity, Location>();
        this.gameCreepers = new ArrayList<Creeper>();
        this.gameBlocks = new ArrayList<Block>();
        this.gameWolfs = new ArrayList<Wolf>();
        this.protection = new Location[2];
        this.teamSpawns = new ConcurrentHashMap<Integer, List<Location>>();
        this.landmines = new ConcurrentHashMap<Location, UUID>();
        for (int i = 0; i <= 7; i++) {
            this.teamSpawns.put(i, new ArrayList<Location>());
        }
        this.doubleJump = false;
        initializeScoreboard();
    }

    /**
     * Get a list of players who are spectating the game.
     *
     * @return (PlayerList) A list containing the spectators
     */
    public List<UUID> getSpectators() {
        return this.spectators;
    }

    /**
     * Get the hashmap containing the players who placed certain landmines.
     *
     * @return (HashMap Location, Player)
     */
    public ConcurrentHashMap<Location, UUID> getLandmines() {
        return this.landmines;
    }

    /**
     * Sets if the arena accepts double jumps
     *
     * @param canDoubleJump
     *         (boolean) True to accept, False to decline.
     */
    public void setDoubleJump(boolean canDoubleJump) {
        this.doubleJump = canDoubleJump;
    }

    /**
     * Gets if this map accepts double jumps
     *
     * @return (boolean) True if accepted, false if not accepted.
     */
    public boolean canDoubleJump() {
        return this.doubleJump;
    }

    /**
     * Gets the hashmap with the teamId bound to the list containing the spawnpoints
     *
     * @return (HashMapLocationList)
     */
    public ConcurrentHashMap<Integer, List<Location>> getTeamSpawns() {
        return this.teamSpawns;
    }

    /**
     * Sets the positions of the protection of the Arena
     *
     * @param locs
     *         (Location[]) Index 1: pos1, Index 2: pos2.
     */
    public void setProtection(Location[] locs) {
        this.protection = locs;
    }

    /**
     * Gets the positions of the protection of the Arena
     *
     * @return (Location[]) Index 1: pos1, Index 2: pos2. Null if not set.
     */
    public Location[] getProtection() {
        return this.protection;
    }

    /**
     * Checks if the arena is full
     *
     * @return (boolean) True if full, False if not.
     */
    public boolean isFull() {
        return this.getPlayers().size() >= this.getMaxPlayers();
    }

    /**
     * Gets the Wolf-list containing all Wolfs spawned in-game
     *
     * @return (WolfList)
     */
    public List<Wolf> getGameWolfs() {
        return this.gameWolfs;
    }

    /**
     * Gets the Blocks-list containing all blocks placed in-game
     *
     * @return (BlockList) List of Blocks
     */
    public List<Block> getGameBlocks() {
        return this.gameBlocks;
    }

    /**
     * Gets the Creepers which are spawned in-game
     *
     * @return (CreeperList) List containing all the creepers
     */
    public List<Creeper> getGameCreepers() {
        return this.gameCreepers;
    }

    /**
     * Gets the inventory of the Team-Menu
     *
     * @return (Inventory) The team-menu inventory
     */
    public Inventory getTeamMenu() {
        return this.teamMenu;
    }

    /**
     * Get the hashmap containing the locations of the entities
     *
     * @return (HashMapEntityLocation) EnderCrystals and Locations
     */
    public ConcurrentHashMap<Entity, Location> getGameCrystalMap() {
        return this.crystalLocations;
    }

    /**
     * Sends a custom message to all players in the arena
     *
     * @param p
     *         (Player) The dead player
     * @param message
     *         (String) The verb that will show up. fe: Killed, Gibbed etc.
     */
    public void sendDeathMessage(Player p, String message) {
        int teamId = plugin.getArenaManager().getTeam(p);
        ChatColor c = Teams.getTeamChatColour(teamId);
        for (UUID id : this.getPlayers()) {
            Player pl = Bukkit.getPlayer(id);
            pl.sendMessage(c + p.getName() + ChatColor.GRAY + message);
        }
        for (UUID id : this.getSpectators()) {
            Player spec = Bukkit.getPlayer(id);
            spec.sendMessage(c + p.getName() + ChatColor.GRAY + message);
        }
    }

    /**
     * Sends a death message to all players in the arena with a custom verb
     *
     * @param p
     *         (Player) The dead player
     * @param killer
     *         (Player) The player who killed p
     * @param verb
     *         (String) The verb that will show up. fe: Killed, Gibbed etc.
     */
    public void sendDeathMessage(Player p, Player killer, String verb) {
        int teamId = plugin.getArenaManager().getTeam(p);
        int teamIdKiller = plugin.getArenaManager().getTeam(killer);
        ChatColor c = Teams.getTeamChatColour(teamId);
        ChatColor cK = Teams.getTeamChatColour(teamIdKiller);
        for (UUID id : this.getPlayers()) {
            Player pl = Bukkit.getPlayer(id);
            pl.sendMessage(c + p.getName() + ChatColor.GRAY + " has been " + verb + " by " + cK + killer.getName());
        }
        for (UUID id : this.getSpectators()) {
            Player spec = Bukkit.getPlayer(id);
            spec.sendMessage(c + p.getName() + ChatColor.GRAY + " has been " + verb + " by " + cK + killer.getName());
        }
    }

    /**
     * Sends a death message to all players in the arena
     *
     * @param p
     *         (Player) The dead player
     * @param killer
     *         (Player) The player who killed p
     */
    public void sendDeathMessage(Player p, Player killer) {
        int teamId = plugin.getArenaManager().getTeam(p);
        int teamIdKiller = plugin.getArenaManager().getTeam(killer);
        ChatColor c = Teams.getTeamChatColour(teamId);
        ChatColor cK = Teams.getTeamChatColour(teamIdKiller);
        for (UUID id : this.getPlayers()) {
            Player pl = Bukkit.getPlayer(id);
            pl.sendMessage(c + p.getName() + ChatColor.GRAY + " has been killed by " + cK + killer.getName());
        }
        for (UUID id : this.getSpectators()) {
            Player spec = Bukkit.getPlayer(id);
            spec.sendMessage(c + p.getName() + ChatColor.GRAY + " has been killed by " + cK + killer.getName());
        }
    }

    /**
     * Sends a death message to all players in the arena
     *
     * @param p
     *         (Player) The dead player
     */
    public void sendDeathMessage(Player p) {
        int teamId = plugin.getArenaManager().getTeam(p);
        ChatColor c = Teams.getTeamChatColour(teamId);
        for (UUID id : this.getPlayers()) {
            Player pl = Bukkit.getPlayer(id);
            pl.sendMessage(c + p.getName() + ChatColor.GRAY + " has died");
        }
        for (UUID id : this.getSpectators()) {
            Player spec = Bukkit.getPlayer(id);
            spec.sendMessage(c + p.getName() + ChatColor.GRAY + " has died");
        }
    }

    /**
     * Get the list containing all the crystals that have spawned in the arena
     *
     * @return (EntityList) The crystals
     */
    public List<Entity> getGameCrystals() {
        return this.gameCrystals;
    }

    /**
     * Gets the time the game waits before teleporting to the lobby, after the game ended.
     *
     * @return (int) The amount of seconds left.
     */
    public int getAfterCount() {
        return this.afterCount;
    }

    /**
     * Sets the time the game waits before teleporting to the lobby, after the game ended.
     *
     * @param count
     *         (int) The amount of seconds to wait
     */
    public void setAfterCount(int count) {
        this.afterCount = count;
    }

    /**
     * Returns true if the game has finished and is in the after-game phase.
     *
     * @return (boolean) true if the game has ended, false if it hasn't
     */
    public boolean isEndGame() {
        return this.isEndGame;
    }

    /**
     * Sets the the arena is in the end-game phase
     *
     * @param isEndGame
     *         (boolean) true if end-game, false if isn't.
     */
    public void setEndGame(boolean isEndGame) {
        if (isEndGame) {
            for (Entity e : this.getGameCrystals()) {
                this.getGameCrystalMap().remove(e);
                e.remove();
            }
            this.setAfterCount(plugin.getConfig().getInt("arena.after-count"));
        }
        this.isEndGame = isEndGame;
        plugin.signHandler.updateSigns();
    }

    /**
     * Get the teams in the arena
     *
     * @return (Team[]) The teams in the arena
     */
    public Team[] getTeams() {
        return this.sTeams;
    }

    /**
     * Checks if the arena is enabled/disabled.
     *
     * @return (boolean) If enabled true, if disabled false
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Sets the state of the arena.
     *
     * @param isEnabled
     *         "true" to enable, "false" to disable
     */
    public void setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;

        for (UUID id : this.getPlayers()) {
            Player p = Bukkit.getPlayer(id);
            p.sendMessage(Broadcast.get("arena.disabled"));
        }

        if (!isEnabled) {
            this.resetArena(false);
        }

        plugin.signHandler.updateSigns();
    }

    /**
     * Checks if all criteria have been configured.
     *
     * @return (String) Error message if not set, null if succeeded
     */
    public String setReady() {
        if (this.maxPlayers <= 0) {
            return Broadcast.get("arena.ready-1");
        }
        else if (this.minPlayers <= 0) {
            return Broadcast.get("arena.ready-2");
        }
        else if (this.minPlayers > this.maxPlayers) {
            return Broadcast.get("arena.ready-3");
        }
        else if (this.teams <= 2) {
            return Broadcast.get("arena.ready-4");
        }
        else if (this.name == "") {
            return Broadcast.get("arena.ready-5");
        }
        else if (this.playerSpawns.size() == 0) {
            return Broadcast.get("arena.ready-6");
        }
        else if (this.crystalSpawns.size() == 0) {
            return Broadcast.get("arena.ready-7");
        }
        else {
            return null;
        }
    }

    /**
     * Updates the time in the scoreboardname
     */
    public void updateTimer() {
        this.points.setDisplayName(SMeth.setColours("&c" + Broadcast.get("arena.time-left") + " &f" +
                SMeth.toTime(this.timeLeft)));
    }

    /**
     * Sets the menu from which the players choose their teams.
     *
     * @param inv
     *         (Inventory) Inventory to set it to.
     */
    public void setTeamMenu(Inventory inv) {
        this.teamMenu = inv;
    }

    /**
     * Gets the teams with the least amount of players for a fair distribution process.
     *
     * @return (IntegerList) The teams with the least amount of players.
     */
    public List<Integer> getSmallestTeams() {
        List<Integer> list = new ArrayList<Integer>();

        int least = 999999;
        for (int i = 0; i < this.getTeamCount(); i++) {
            if (this.getTeams()[i].getPlayers().size() < least) {
                least = this.getTeams()[i].getPlayers().size();
            }
        }

        int count = 0;
        for (Team t : this.getTeams()) {
            if (t.getPlayers().size() == least && count < this.getTeamCount()) {
                list.add(count);
            }
            count++;
        }

        return list;
    }

    /**
     * Initializes the scoreboard. This makes or updates the scoreboard.
     */
    public void initializeScoreboard() {

        this.score = Bukkit.getScoreboardManager().getNewScoreboard();
        this.sTeams = new Team[8];
        this.sScore = new Score[8];

        this.spectatorTeam = this.score.registerNewTeam("Spectate");
        this.spectatorTeam.setAllowFriendlyFire(false);
        this.spectatorTeam.setCanSeeFriendlyInvisibles(true);
        this.spectatorTeam.setPrefix(ChatColor.BLUE + "[Spec] ");
        this.sTeams[0] = this.score.registerNewTeam("Green");
        this.sTeams[0].setPrefix(ChatColor.GREEN + "");
        this.sTeams[1] = this.score.registerNewTeam("Orange");
        this.sTeams[1].setPrefix(ChatColor.GOLD + "");
        this.sTeams[2] = this.score.registerNewTeam("Yellow");
        this.sTeams[2].setPrefix(ChatColor.YELLOW + "");
        this.sTeams[3] = this.score.registerNewTeam("Red");
        this.sTeams[3].setPrefix(ChatColor.RED + "");
        this.sTeams[4] = this.score.registerNewTeam("Blue");
        this.sTeams[4].setPrefix(ChatColor.AQUA + "");
        this.sTeams[5] = this.score.registerNewTeam("Magenta");
        this.sTeams[5].setPrefix(ChatColor.LIGHT_PURPLE + "");
        this.sTeams[6] = this.score.registerNewTeam("White");
        this.sTeams[6].setPrefix(ChatColor.WHITE + "");
        this.sTeams[7] = this.score.registerNewTeam("Black");
        this.sTeams[7].setPrefix(ChatColor.BLACK + "");

        for (int i = 0; i <= 7; i++) {
            this.sTeams[i].setAllowFriendlyFire(false);
        }

        this.points = this.score.registerNewObjective("points", "dummy");
        this.points.setDisplaySlot(DisplaySlot.SIDEBAR);
        updateTimer();

        this.sScore[0] = this.points.getScore(Teams.GREEN);
        this.sScore[1] = this.points.getScore(Teams.ORANGE);
        if (this.teams >= 3) {
            this.sScore[2] = this.points.getScore(Teams.YELLOW);
        }
        if (this.teams >= 4) {
            this.sScore[3] = this.points.getScore(Teams.RED);
        }
        if (this.teams >= 5) {
            this.sScore[4] = this.points.getScore(Teams.BLUE);
        }
        if (this.teams >= 6) {
            this.sScore[5] = this.points.getScore(Teams.MAGENTA);
        }
        if (this.teams >= 7) {
            this.sScore[6] = this.points.getScore(Teams.WHITE);
        }
        if (this.teams >= 8) {
            this.sScore[7] = this.points.getScore(Teams.BLACK);
        }

        for (Score s : sScore) {
            if (s != null) {
                s.setScore(0);
            }
        }
    }

    /**
     * Gets the amount of players in a team.
     *
     * @param teamId
     *         (int) The ID of the team.
     * @return (int) The amount of players.
     */
    public int getTeamPlayerCount(int teamId) {
        int ps = 0;
        Iterator<Entry<UUID, Integer>> it = this.playerTeams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Integer> pairs = (Map.Entry<UUID, Integer>)it.next();
            if (pairs.getValue() == teamId) {
                ps++;
            }
            it.remove();
        }
        return ps;
    }

    /**
     * Gets the team the player is in.
     *
     * @param p
     *         (Player) The player from whose you want to know the team he/she is in.
     * @return (int) The TeamId of the team the player is in.
     */
    public int getTeam(Player p) {
        return playerTeams.get(p.getUniqueId());
    }

    /**
     * Checks if the player is in the specific team.
     *
     * @param p
     *         (Player) The player you want to check for.
     * @param teamId
     *         (int) The ID of the team.
     * @return (boolean) true if he/she's in, false if he/she isn't.
     */
    public boolean isInTeam(Player p, int teamId) {
        return this.playerTeams.get(p.getUniqueId()) == teamId;
    }

    /**
     * If there are players in the arena
     * Reveal the winner
     *
     * @return (String) Winning team's name
     */
    public String declareWinner() {
        if (!this.getPlayers().isEmpty()) {
            int highest = -99999;
            Score hScore = null;
            for (Score s : this.sScore) {
                if (s != null) {
                    if (s.getScore() > highest) {
                        highest = s.getScore();
                        hScore = s;
                    }
                }
            }

            String winningTeam = "";
            int teamId = 0;
            ChatColor colour = null;
            List<UUID> winningPlayers = new ArrayList<UUID>();

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

            Bukkit.getPluginManager().callEvent(new TeamWinGameEvent(winningPlayers, this, teamId, this.getTeamCount(),
                    this.sTeams, winningTeam));

            return winningTeam;
        }

        return "";
    }

    /**
     * Chooses a random player in the arena and from another team.
     *
     * @param excluded
     *         (Player) The player whose team cannot be chosen.
     * @return (Player) The chosen player. Null if there are no players to choose from.
     */
    public Player getRandomPlayer(Player excluded) {
        int team = getTeam(excluded);
        List<Player> toChoose = new ArrayList<>();
        for (UUID id : this.players) {
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
     * @return void
     */
    public void resetArena(boolean onEnable) {
        if (!onEnable) {
            //Removes all potion-effects on players
            if (this.getPlayers().size() > 0) {
                for (UUID id : this.getPlayers()) {
                    Player p = Bukkit.getPlayer(id);
                    Collection<PotionEffect> eff = p.getActivePotionEffects();
                    for (PotionEffect ef : eff) {
                        p.removePotionEffect(ef.getType());
                    }
                    plugin.itemHandler.cursed.remove(p);
                }
            }
            //Removes all blocks placed in-game
            if (this.getGameBlocks().size() > 0) {
                List<Block> toRemove = new ArrayList<>();
                toRemove.addAll(this.getGameBlocks());
                for (Location loc : this.getLandmines().keySet()) {
                    toRemove.add(loc.getBlock());
                }
                for (Block b : toRemove) {
                    b.setType(Material.AIR);
                }
            }
            //Removs all wolfs
            if (this.getGameWolfs().size() > 0) {
                for (Wolf w : this.getGameWolfs()) {
                    if (w != null) {
                        w.setHealth(0);
                    }
                }
            }
            //Removes all items and
            if (this.getCrystalSpawns().size() > 0) {
                List<Entity> toRemove = new ArrayList<Entity>();
                for (Entity e : this.getCrystalSpawns().get(0).getWorld().getEntities()) {
                    if ((e instanceof Item || e instanceof ExperienceOrb || e instanceof Arrow || e instanceof EnderCrystal ||
                            e instanceof LivingEntity) && !(e instanceof Player)) {
                        if (plugin.prot.isInProtectedArena(e.getLocation())) {
                            toRemove.add(e);
                        }
                    }
                }
                for (Entity e : toRemove) {
                    e.remove();
                }
            }
        }

        this.gameCrystals.clear();
        this.count = this.plugin.getConfig().getInt("arena.countdown");
        this.isCounting = false;
        this.timeLeft = this.plugin.getConfig().getInt("arena.game-length");
        this.inGame = false;
        this.afterCount = plugin.getConfig().getInt("arena.after-game");
        this.isEndGame = false;
        this.crystalLocations.clear();
        this.gameBlocks.clear();
        initializeScoreboard();
        this.gameWolfs.clear();
        this.landmines.clear();
        removePlayers();

        plugin.signHandler.updateSigns();
    }

    /**
     * Get the scoreboard of the arena.
     *
     * @return (Scoreboard) Scoreboard of the arena
     */
    public Scoreboard getScoreboard() {
        return this.score;
    }

    /**
     * Removes a player from the arena including removal from the team,
     * resetting his/her scoreboard and restoring his/her inventory.
     *
     * @param p
     *         (Player) The player you want to remove from the arena.
     */
    public void removePlayer(Player p) {
        if (!this.spectators.contains(p.getUniqueId())) {
            for (UUID id : this.getPlayers()) {
                Player player = Bukkit.getPlayer(id);
                player.sendMessage(Broadcast.TAG + Broadcast.get("arena.leave")
                        .replace("%player%", Teams.getTeamChatColour(this.getTeam(p)) + p.getName())
                        .replace("%count%", "(" + (this.getPlayers().size() - 1) + "/" + this.getMaxPlayers() + ")"));
            }
        }

        this.players.remove(p.getUniqueId());
        for (Team t : this.sTeams) {
            if (t.hasPlayer((OfflinePlayer)p)) {
                t.removePlayer((OfflinePlayer)p);
            }
        }

        for (PotionEffect pe : p.getActivePotionEffects()) {
            p.removePotionEffect(pe.getType());
        }

        try {
            p.teleport(plugin.am.getLobby());
        }
        catch (Exception e) {
            plugin.getLogger().info("Lobby-spawn not set!");
        }

        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        this.playerTeams.remove(p.getUniqueId());
        plugin.im.restoreInventory(p);
        plugin.im.playerClass.remove(p.getUniqueId());
        this.spectators.remove(p.getUniqueId());

        if (p.getGameMode() != GameMode.CREATIVE) {
            p.setAllowFlight(false);
        }

        plugin.ab.getAbilities().remove(p.getUniqueId());
        p.setFireTicks(0);
        Bukkit.getPluginManager().callEvent(new PlayerLeaveArenaEvent(p, this));

        if (this.spectatorTeam.getPlayers().contains(p)) {
            this.spectatorTeam.removePlayer(p);
        }

        plugin.signHandler.updateSigns();
    }

    /**
     * Adds a player to the arena including putting into a team, set the
     * scoreboard and give the in-game inventory.
     *
     * @param p
     *         (Player) The player to add
     * @param teamId
     *         (int) The team to put the player in
     * @param spectate
     *         (boolean) True if the player is spectating
     * @return (boolean) True if joined, False if not joined
     */
    public boolean addPlayer(Player p, int teamId, boolean spectate) {
        PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(p, this, spectate);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (!this.isFull() || plugin.getArenaManager().getArena(p.getUniqueId()).getSpectators()
                    .contains(p.getUniqueId())) {
                if (this.isEnabled()) {
                    try {
                        this.playerTeams.put(p.getUniqueId(), spectate ? -1 : teamId);

                        if (!spectate) {
                            this.players.add(p.getUniqueId());
                            this.sTeams[teamId].addPlayer((OfflinePlayer)p);
                        }
                        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                        p.setScoreboard(this.score);
                        plugin.im.setInGameInventory(p);

                        if (spectate) {
                            preSpecGamemodes.put(p.getUniqueId(), p.getGameMode());
                            p.setGameMode(GameMode.SPECTATOR);
                            p.setAllowFlight(true);
                            this.getSpectators().add(p.getUniqueId());
                            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 127));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 127));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 127));
                            p.sendMessage(Broadcast.TAG + Broadcast.get("arena.spectate")
                                    .replace("%arena%", this.getName()));
                            this.spectatorTeam.addPlayer(p);
                        }

                        if (!spectate) {
                            try {
                                if (this.getLobbySpawns()[teamId] == null) {
                                    p.teleport(this.getLobbySpawns()[0]);
                                }
                                else {
                                    p.teleport(this.getLobbySpawns()[teamId]);
                                }
                            }
                            catch (Exception ignored) {
                            }
                        }
                        else {
                            if (this.getPlayerSpawns().size() > 0) {
                                p.teleport(this.getPlayerSpawns().get(0));
                            }
                            else {
                                p.teleport(this.getTeamSpawns().get(0).get(0));
                            }
                        }

                        plugin.menuPT.updateMenu(this);

                        if (!spectate) {
                            for (UUID id : this.getPlayers()) {
                                Player player = Bukkit.getPlayer(id);
                                player.sendMessage(Broadcast.TAG + Broadcast.get("arena.join")
                                        .replace("%player%", Teams.getTeamChatColour(teamId) + p.getName())
                                        .replace("%count%", "(" + this.getPlayers().size() + "/" + this.getMaxPlayers() + ")"));
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
        for (UUID id : this.players) {
            Player p = Bukkit.getPlayer(id);
            try {
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                plugin.im.restoreInventory(p);
                try {
                    p.teleport(plugin.am.getLobby());
                }
                catch (Exception e) {
                    plugin.getLogger().info("Lobby-spawn not set!");
                }

            }
            catch (Exception ignored) {
            }
        }
        this.players.clear();

        for (UUID id : this.getSpectators()) {
            Player p = Bukkit.getPlayer(id);
            try {
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                plugin.im.restoreInventory(p);
                p.setGameMode(preSpecGamemodes.get(p.getUniqueId()));
                preSpecGamemodes.remove(p.getUniqueId());
                try {
                    p.teleport(plugin.am.getLobby());
                }
                catch (Exception e) {
                    plugin.getLogger().info("Lobby-spawn not set!");
                }
                finally {
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    p.removePotionEffect(PotionEffectType.WEAKNESS);
                    p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                }

            }
            catch (Exception ignored) {
            }
        }
        this.spectators.clear();

        for (Team t : this.sTeams) {
            for (OfflinePlayer op : t.getPlayers()) {
                t.removePlayer(op);
            }
        }
    }

    /**
     * Gets a list of all players currently in the arena.
     *
     * @return (PlayerList) The players in the arena
     */
    public List<UUID> getPlayers() {
        return this.players;
    }

    /**
     * Removes all possible Item-spawnpoints from the arena
     */
    public void clearItemSpawns() {
        this.itemSpawns.clear();
    }

    /**
     * Removes a specific Item-spawnpoint from the arena
     *
     * @param loc
     *         (Location) The spawnpoint-location you'd like to remove
     */
    public void removeItemSpawn(Location loc) {
        this.itemSpawns.remove(loc);
    }

    /**
     * Removes a specific Item-spawnpoint from the arena
     *
     * @param index
     *         (int) The spawnpoint-index you'd like to remove
     */
    public void removeItemSpawn(int index) {
        this.itemSpawns.remove(index);
    }

    /**
     * Adds an Item-spawnpoint to the arena
     *
     * @param loc
     *         (Location) The spawnpoint-location you'd like to add
     */
    public void addItemSpawn(Location loc) {
        this.itemSpawns.add(loc);
    }

    /**
     * Sets all the Item-spawns for this arena
     *
     * @param spawns
     *         (LocationList) All spawn-locations
     */
    public void setItemSpawns(List<Location> spawns) {
        this.itemSpawns = spawns;
    }

    /**
     * Sets all the Item-spawns for this arena
     *
     * @param spawns
     *         (Location[]) All spawn-locations
     */
    public void setItemSpawns(Location[] spawns) {
        this.itemSpawns.clear();
        Collections.addAll(this.itemSpawns, spawns);
    }

    /**
     * Get a list containing all the Item-spawn locations
     *
     * @return (LocationList) All the item-spawn locations
     */
    public List<Location> getItemSpawns() {
        return this.itemSpawns;
    }

    /**
     * Removes all crystal-spawns from the arena
     */
    public void clearCrystalSpawns() {
        this.crystalSpawns.clear();
    }

    /**
     * Remove a specific crystal-spawn location
     *
     * @param loc
     *         (Location) The location you'd like to remove
     */
    public void removeCrystalSpawn(Location loc) {
        this.crystalSpawns.remove(loc);
    }

    /**
     * Remove a specific crystal-spawn location
     *
     * @param index
     *         (int) The index in the location-list
     */
    public void removeCrystalSpawn(int index) {
        this.crystalSpawns.remove(index);
    }

    /**
     * Add a crystal spawn.
     *
     * @param loc
     *         (Location) The location you want to add
     */
    public void addCrystalSpawn(Location loc) {
        this.crystalSpawns.add(loc);
    }

    /**
     * Set the crystal Spawns from a list
     *
     * @param spawns
     *         (LocationList) List containing all the spawnpoints
     */
    public void setCrystalSpawns(List<Location> spawns) {
        this.crystalSpawns = spawns;
    }

    /**
     * Set the crystal Spawns from an array
     *
     * @param spawns
     *         (Location[]) Array containing all the spawnpoints
     */
    public void setCrystalSpawns(Location[] spawns) {
        this.crystalSpawns.clear();
        Collections.addAll(this.crystalSpawns, spawns);
    }

    /**
     * Get the crystal spawns.
     *
     * @return (LocationList) List containing all the crystal-spawn locations
     */
    public List<Location> getCrystalSpawns() {
        return this.crystalSpawns;
    }

    /**
     * Clear playerSpawns.
     */
    public void clearPlayerSpawns() {
        this.playerSpawns.clear();
    }

    /**
     * Remove a player spawn.
     *
     * @param loc
     *         (Location) Location to remove.
     */
    public void removePlayerSpawn(Location loc) {
        if (this.playerSpawns.contains(loc)) {
            this.playerSpawns.remove(loc);
        }
    }

    /**
     * Remove a player spawn.
     *
     * @param index
     *         (int) Location to remove (index).
     */
    public void removePlayerSpawn(int index) {
        this.playerSpawns.remove(index);
    }

    /**
     * Add a player spawn.
     *
     * @param loc
     *         (Location) A new Player-spawnpoint
     */
    public void addPlayerSpawn(Location loc) {
        this.playerSpawns.add(loc);
    }

    /**
     * Set the player Spawns
     *
     * @param spawns
     *         (LocationList) A list containing all the player-spawns.
     */
    public void setPlayerSpawns(List<Location> spawns) {
        this.playerSpawns = spawns;
    }

    /**
     * Set the player Spawns
     *
     * @param spawns
     *         (Location[]) An array containing all the player-spawns.
     */
    public void setPlayerSpawns(Location[] spawns) {
        this.playerSpawns.clear();
        Collections.addAll(this.playerSpawns, spawns);
    }

    /**
     * Get the player spawns.
     *
     * @return (LocationList) A list containing all the player-spawnpoints
     */
    public List<Location> getPlayerSpawns() {
        return this.playerSpawns;
    }

    /**
     * Set the time left in the game.
     *
     * @param timeInSeconds
     *         (int) The time in seconds the game will last
     */
    public void setTimeLeft(int timeInSeconds) {
        this.timeLeft = timeInSeconds;
    }

    /**
     * Get the time left in the arena.
     *
     * @return (int) The time left in seconds
     */
    public int getTimeLeft() {
        return this.timeLeft;
    }

    /**
     * Checks if the arena is in-game.
     *
     * @return (boolean) true if in-game, false if not in-game.
     */
    public boolean isInGame() {
        return this.inGame;
    }

    /**
     * Set the in-game status of the game.
     *
     * @param inGame
     *         (boolean) True/False
     */
    public void setInGame(boolean inGame) {
        this.inGame = inGame;

    }

    /**
     * Get if the countdown is happening.
     *
     * @return (boolean) True if the countdown has started. False if not.
     */
    public boolean isCounting() {
        return this.isCounting;
    }

    /**
     * Set if countdown is happening.
     *
     * @param isCountingB
     *         (boolean)
     */
    public void setIsCounting(boolean isCountingB) {
        this.isCounting = isCountingB;
        plugin.signHandler.updateSigns();
    }

    /**
     * Set the countdown.
     *
     * @param seconds
     *         (int) The amount of seconds to set the countdown to (-1 for default countdown).
     */
    public void setCountdown(int seconds) {
        this.count = seconds;
    }

    /**
     * Get the amount of seconds left.
     *
     * @return (int) Seconds left.
     */
    public int getCountdown() {
        return this.count;
    }

    /**
     * Sets the lobby spawns of the teams.
     * The team-id (0, 1, 2, 3, 4, 5, 6, 7) represents the spawn of the specific team.
     *
     * @param locations
     *         (Location[]) The locations.
     */
    public void setLobbySpawns(Location[] locations) {
        this.lobbySpawn = locations;
    }

    /**
     * Gets the team-lobby spawns of the arena.
     * The team-id (0, 1, 2, 3, 4, 5, 6, 7) represents the spawn of the specific team.
     *
     * @return (Location[]) The lobbyspawn-array
     */
    public Location[] getLobbySpawns() {
        return this.lobbySpawn;
    }

    /**
     * Toggles the crystalquest.vip requirement on the arena.
     *
     * @param isVip
     *         (boolean) True if VIP is needed. False if VIP is not needed.
     */
    public void setVip(boolean isVip) {
        this.vip = isVip;
    }

    /**
     * Checks if it is an arena only for people with the crystalquest.vip node.
     *
     * @return (boolean) True if it is VIP-only, False if it isn't
     */
    public boolean isVip() {
        return this.vip;
    }

    /**
     * Gets the arena ID.
     *
     * @return (int) The arena ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the name of the arena.
     *
     * @return (String) Arena-name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the arena.
     *
     * @param name
     *         (String) The new arena-name.
     * @return (boolean) true when the name is succesfully applied, false when the name already
     * exists.
     */
    public boolean setName(String name) {
        try {
            int nn = Integer.parseInt(name);
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
        else {
            return false;
        }
    }

    /**
     * Sets the amount of teams available for the arena.
     *
     * @param amountOfTeams
     *         (int) The amount of teams used to play this arena.
     * @return (boolean) true if applied succesful, false if amountOfTeams is greater than 6.
     */
    public boolean setTeams(int amountOfTeams) {
        if (amountOfTeams > 8) {
            return false;
        }
        else {
            this.teams = amountOfTeams;
            return true;
        }
    }

    /**
     * Returns the amount of teams available for the arena.
     *
     * @return (int) The amount of teams of the arena.
     */
    public int getTeamCount() {
        return this.teams;
    }

    /**
     * Get the minimum amount of players for an arena to start.
     *
     * @return (int) The minimum amount of players to start.
     */
    public int getMinPlayers() {
        return this.minPlayers;
    }

    /**
     * Set the minimum amount of players for an arena to start.
     *
     * @param minPlayers
     *         (int) The minimum amount of players.
     */
    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    /**
     * Get the maximum amount of players for an arena to start.
     *
     * @return (int) The maximum amount of players to start.
     */
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    /**
     * Set the maximum amount of players for an arena to start.
     *
     * @param maxPlayers
     *         (int) The maximum amount of players.
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
            for (UUID id : this.getPlayers()) {
                Player pl = Bukkit.getPlayer(id);
                plugin.im.setClassInventory(pl);
                pl.sendMessage(Broadcast.TAG + Broadcast.get("arena.started"));
                pl.playSound(pl.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 20F, 20F);
                pl.sendMessage(Broadcast.TAG + Broadcast.get("arena.using-class")
                        .replace("%class%", SMeth.setColours(plugin.getConfig().getString(
                                "kit." + plugin.im.playerClass.get(pl.getUniqueId()) + ".name"))));
            }

            this.setInGame(true);

            Random ran = new Random();

            for (UUID id : this.getPlayers()) {
                Player p = Bukkit.getPlayer(id);
                boolean isTeamSpawns = false;
                for (int i = 0; i < this.getTeamCount(); i++) {
                    if (this.getTeamSpawns().get(i).size() > 0) {
                        isTeamSpawns = true;
                    }
                }
                if (isTeamSpawns) {
                    int team = this.getTeam(p);
                    p.teleport(getTeamSpawns().get(team).get(ran.nextInt(getTeamSpawns().get(team).size())));
                }
                else {
                    p.teleport((this.getPlayerSpawns().get(ran.nextInt(this.getPlayerSpawns().size()))));
                }
            }

            plugin.signHandler.updateSigns();
        }

    }

    /**
     * Adds points to a team
     *
     * @param teamId
     *         (int) The team-ID of the team.
     * @param score
     *         (int) The points to add.
     */
    public void addScore(int teamId, int score) {
        this.sScore[teamId].setScore(this.sScore[teamId].getScore() + score);
    }

    /**
     * Sets the score of a team
     *
     * @param teamId
     *         (int) The team-ID of the team.
     * @param score
     *         (int) The new score.
     */
    public void setScore(int teamId, int score) {
        this.sScore[teamId].setScore(score);
    }

    /**
     * Gets the score of a team
     *
     * @param teamId
     *         (int) The team-ID of the team.
     * @return (int) The score
     */
    public int getScore(int teamId) {
        if (this.sScore == null) {
            return -1;
        }
        else {
            return this.sScore[teamId].getScore();
        }
    }
}