package nl.sugcube.crystalquest.events;

import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
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
    private CrystalQuestTeam team;
    private int teamCount;
    private Team[] teams;
    private String teamName;

    public TeamWinGameEvent(List<UUID> player, Arena arena, CrystalQuestTeam team, int teamCount,
                            Team[] teams, String teamName) {
        this.player = player;
        this.arena = arena;
        this.team = team;
        this.teamCount = teamCount;
        this.teams = teams;
        this.teamName = teamName;
    }

    /**
     * @return The name of the team who won.
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * @return The teams that were in the game.
     */
    public Team[] getTeams() {
        return teams;
    }

    /**
     * @return The amount of teams that played the game.
     */
    public int getTeamCount() {
        return teamCount;
    }

    /**
     * @return The team that won.
     */
    public CrystalQuestTeam getTeam() {
        return team;
    }

    /**
     * @return Get the arenas the players won in
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * @return (PlayerList) The players who won the game
     */
    public List<UUID> getPlayers() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}