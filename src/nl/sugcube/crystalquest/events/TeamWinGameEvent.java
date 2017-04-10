package nl.sugcube.crystalquest.events;

import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.UUID;

/**
 * @author SugarCaney
 */
public class TeamWinGameEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private List<UUID> player;
    private Arena arena;
    private int team;
    private int teamCount;
    private Team[] teams;
    private String teamName;

    /**
     * @return (String) The name of the team who won.
     */
    public String getTeamName() {
        return this.teamName;
    }

    /**
     * @return (Team[]) The teams that were in the game.
     */
    public Team[] getTeams() {
        return this.teams;
    }

    /**
     * @return (int) The amount of teams that played the game.
     */
    public int getTeamCount() {
        return this.teamCount;
    }

    /**
     * @return (int) ID of the team the winning players is in
     */
    public int getTeam() {
        return this.team;
    }

    /**
     * @return (Arena) Get the arena the players won in
     */
    public Arena getArena() {
        return this.arena;
    }

    /**
     * @return (PlayerList) The players who won the game
     */
    public List<UUID> getPlayers() {
        return this.player;
    }

    public TeamWinGameEvent(List<UUID> p, Arena a, int teamId, int tc, Team[] tms, String tn) {
        this.player = p;
        this.arena = a;
        this.team = teamId;
        this.teamCount = tc;
        this.teams = tms;
        this.teamName = tn;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}