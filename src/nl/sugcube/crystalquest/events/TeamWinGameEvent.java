package nl.sugcube.crystalquest.events;

import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
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
    private Collection<Team> teams;

    public TeamWinGameEvent(List<UUID> player, Arena arena, CrystalQuestTeam team, int teamCount,
                            Collection<Team> teams) {
        this.player = player;
        this.arena = arena;
        this.team = team;
        this.teamCount = teamCount;
        this.teams = teams;
    }

    /**
     * @return The name of the team who won.
     */
    public String getTeamName() {
        return team.toString();
    }

    /**
     * @return The teams that were in the game.
     */
    public Collection<Team> getTeams() {
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

    @Override
    public String toString() {
        return "TeamWinGameEvent{" + "player=" + player +
                ", arena=" + arena +
                ", team=" + team +
                ", teamCount=" + teamCount +
                ", teams=" + teams +
                '}';
    }
}