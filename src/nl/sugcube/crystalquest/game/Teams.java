package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.util.Materials;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author SugarCaney
 */
public class Teams {

    public static CrystalQuest plugin;

    public static final OfflinePlayer GREEN = Bukkit.getOfflinePlayer(ChatColor.GREEN + "Team Green");
    public static final OfflinePlayer ORANGE = Bukkit.getOfflinePlayer(ChatColor.GOLD + "Team Orange");
    public static final OfflinePlayer YELLOW = Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Team Yellow");
    public static final OfflinePlayer RED = Bukkit.getOfflinePlayer(ChatColor.RED + "Team Red");
    public static final OfflinePlayer BLUE = Bukkit.getOfflinePlayer(ChatColor.AQUA + "Team Blue");
    public static final OfflinePlayer MAGENTA = Bukkit.getOfflinePlayer(ChatColor.LIGHT_PURPLE + "Team Magenta");
    public static final OfflinePlayer WHITE = Bukkit.getOfflinePlayer(ChatColor.WHITE + "Team White");
    public static final OfflinePlayer BLACK = Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY + "Team Black");

    public static final String GREEN_NAME = ChatColor.GREEN + "Team Green";
    public static final String ORANGE_NAME = ChatColor.GOLD + "Team Orange";
    public static final String YELLOW_NAME = ChatColor.YELLOW + "Team Yellow";
    public static final String RED_NAME = ChatColor.RED + "Team Red";
    public static final String BLUE_NAME = ChatColor.AQUA + "Team Blue";
    public static final String MAGENTA_NAME = ChatColor.LIGHT_PURPLE + "Team Magenta";
    public static final String WHITE_NAME = ChatColor.WHITE + "Team White";
    public static final String BLACK_NAME = ChatColor.DARK_GRAY + "Team Black";

    private static final Random RANDOM = new Random();

    public Teams(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Returns the TeamID of the team which will get striked without
     * being the team of the player passed as argument.
     *
     * @param player
     *         The player from whose team cannot be hit.
     * @return The team that has been randomly chosen, or {@code null} when there is no other team.
     */
    public static CrystalQuestTeam getRandomTeamToHit(Player player) {
        Arena arena = plugin.getArenaManager().getArena(player.getUniqueId());
        List<CrystalQuestTeam> teams = new ArrayList<>();

        for (CrystalQuestTeam team : arena.getTeams()) {
            List<Player> teamPlayers = getPlayersFromTeam(arena, team);
            if (teamPlayers.contains(player)) {
                continue;
            }

            if (teamPlayers.isEmpty()) {
                continue;
            }

            teams.add(team);
        }

        if (teams.size() == 0) {
            return null;
        }

        return teams.get(RANDOM.nextInt(teams.size()));
    }

    /**
     * Gets all the players from an arenas in a certain team.
     *
     * @param arena
     *         The arenas to get the players from.
     * @param team
     *         The team to get the players from.
     * @return A list containing all the players in a given team in a given arenas.
     */
    public static List<Player> getPlayersFromTeam(Arena arena, CrystalQuestTeam team) {
        Set<OfflinePlayer> offlinePlayers = arena.getTeamObject(team).getPlayers();
        return Bukkit.getOnlinePlayers().stream()
                .filter(offlinePlayers::contains)
                .collect(Collectors.toList());
    }

    /**
     * Gets the team matching the wool colour.
     *
     * @param dataValue
     *         The datavalue of the wool.
     * @return The corresponding team.
     * @throws IllegalArgumentException
     *         When there is no team for the given data value.
     */
    public static CrystalQuestTeam getTeamFromDataValue(short dataValue) throws IllegalArgumentException {
        return CrystalQuestTeam.valueOf(dataValue);
    }

    /**
     * Get the team matching the wool block.
     * @param wool
     *          The wool block material.
     * @return The corresponding team.
     */
    public static CrystalQuestTeam getTeamFromWoolMaterial(Material wool) {
        return CrystalQuestTeam.valueOf(Materials.woolDamageValue(wool));
    }

    /**
     * Gets the team-id matching the dye colour.
     *
     * @param colour
     *         The colour you want to check for.
     * @return The ID of the corresponding team.
     * @throws IllegalArgumentException
     *         When there is no team with the given colour.
     */
    public static CrystalQuestTeam getDyeColourTeam(DyeColor colour) throws IllegalArgumentException {
        return CrystalQuestTeam.valueOf(colour);
    }

    /**
     * Gets the dye colour matching the team-id
     *
     * @param teamId
     *         The ID of the team.
     * @return The corresponding DyeColor.
     * @throws IllegalArgumentException
     *         When there is no team with the given id.
     */
    public static DyeColor getTeamDyeColour(int teamId) throws IllegalArgumentException {
        return CrystalQuestTeam.valueOf(teamId).getDyeColour();
    }

    /**
     * Get the ChatColor matching the team-id
     *
     * @param teamId
     *         The ID of the team.
     * @return The corresponding ChatColor.
     * @throws IllegalArgumentException
     *         When there is no team with the given id.
     */
    public static ChatColor getTeamChatColour(int teamId) throws IllegalArgumentException {
        return CrystalQuestTeam.valueOf(teamId).getChatColour();
    }

    /**
     * Get the team-id with the matching name (including colours).
     *
     * @param name
     *         (String) The name of the team.
     * @return (int) The corresponding team-id.
     * @throws IllegalArgumentException
     *         When there is no team with the given name.
     * @deprecated Use {@link Teams#getTeamId(String)} instead.
     */
    @Deprecated
    public static int getTeamIdFromName(String name) throws IllegalArgumentException {
        return getTeamId(name);
    }

    /**
     * Get the team-id of the matching team-name (the colour).
     *
     * @param name
     *         The colour of the team.
     * @return The corresponding team-id.
     * @throws IllegalArgumentException
     *         When there is no team with the given name.
     */
    public static int getTeamId(String name) throws IllegalArgumentException {
        return CrystalQuestTeam.valueOfName(name).getId();
    }

    /**
     * Get the name of the team matching the team-id.
     *
     * @param teamId
     *         The ID of the team.
     * @return The name of the corresponding team including colours and team prefix.
     * @throws IllegalArgumentException
     *         When there is no team with the given id.
     */
    public static String getTeamNameById(int teamId) throws IllegalArgumentException {
        return CrystalQuestTeam.valueOf(teamId).toString();
    }

    /**
     * Get the colour name of the team with the given id.
     *
     * @throws IllegalArgumentException
     *         When there is no team with the given id.
     */
    public static String getColourName(int teamId) throws IllegalArgumentException {
        return CrystalQuestTeam.valueOf(teamId).getName();
    }
}