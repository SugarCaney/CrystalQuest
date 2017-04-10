package nl.sugcube.crystalquest;

import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
     * TODO: FUCKING FIX THIS. GOD ITS WORKING ATROUCIOUSLY.
     * <p>
     * Returns the TeamID of the team which will get striked without
     * being the team of the player passed as argument.
     *
     * @param player
     *         (Player) The player from whose team cannot be hit.
     * @return (int) The TeamID of the hit team.
     */
    public static int getRandomTeamToHit(Player player) {
        Arena arena = plugin.getArenaManager().getArena(player.getUniqueId());
        List<Integer> teams = new ArrayList<>();

        for (int team = 0; team < arena.getTeamCount(); team++) {
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
            return 0;
        }

        return teams.get(RANDOM.nextInt(teams.size()));
    }

    /**
     * Gets all the players from an arena in a certain team.
     *
     * @param arena
     *         The arena to get the players from.
     * @param teamId
     *         The teamId of the team to get the playres of.
     * @return A list containing all the players in a given team in a given arena.
     */
    public static List<Player> getPlayersFromTeam(Arena arena, int teamId) {
        List<Player> players = new ArrayList<>();

        for (OfflinePlayer offlinePlayer : arena.getTeams()[teamId].getPlayers()) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(onlinePlayer -> onlinePlayer.equals(offlinePlayer))
                    .forEach(players::add);
        }

        return players;
    }

    /**
     * Gets the team-id matching the wool colour.
     *
     * @param dataValue
     *         (short) The datavalue of the wool.
     * @return (int) The ID of the corresponding team.
     */
    public static int getTeamFromDataValue(short dataValue) {
        switch (dataValue) {
            case 5:
                return 0;
            case 1:
                return 1;
            case 4:
                return 2;
            case 14:
                return 3;
            case 3:
                return 4;
            case 2:
                return 5;
            case 0:
                return 6;
            case 15:
                return 7;
            default:
                return -1;
        }
    }

    /**
     * Gets the team-id matching the dye colour.
     *
     * @param color
     *         (DyeColor) The colour you want to check for.
     * @return (int) The ID of the corresponding team.
     */
    public static int getDyeColourTeam(DyeColor color) {
        DyeColor[] colors = new DyeColor[8];
        colors[0] = (DyeColor.LIME);
        colors[1] = (DyeColor.ORANGE);
        colors[2] = (DyeColor.YELLOW);
        colors[3] = (DyeColor.RED);
        colors[4] = (DyeColor.LIGHT_BLUE);
        colors[5] = (DyeColor.MAGENTA);
        colors[6] = (DyeColor.WHITE);
        colors[7] = (DyeColor.BLACK);
        int count = 0;
        for (DyeColor c : colors) {
            if (c == color) {
                return count;
            }
            else {
                count++;
            }
        }
        return 1;
    }

    /**
     * Gets the dye colour matching the team-id
     *
     * @param teamId
     *         (int) The ID of the team.
     * @return (DyeColor) The corresponding DyeColor.
     */
    public static DyeColor getTeamDyeColour(int teamId) {
        DyeColor colour = null;
        switch (teamId) {
            case 0:
                colour = DyeColor.LIME;
                break;
            case 1:
                colour = DyeColor.ORANGE;
                break;
            case 2:
                colour = DyeColor.YELLOW;
                break;
            case 3:
                colour = DyeColor.RED;
                break;
            case 4:
                colour = DyeColor.LIGHT_BLUE;
                break;
            case 5:
                colour = DyeColor.MAGENTA;
                break;
            case 6:
                colour = DyeColor.WHITE;
                break;
            case 7:
                colour = DyeColor.BLACK;
                break;
        }
        return colour;
    }

    /**
     * Get the ChatColor matching the team-id
     *
     * @param teamId
     *         (int) The ID of the team.
     * @return (ChatColor) The corresponding ChatColor.
     */
    public static ChatColor getTeamChatColour(int teamId) {
        ChatColor colour = null;
        switch (teamId) {
            case 0:
                colour = ChatColor.GREEN;
                break;
            case 1:
                colour = ChatColor.GOLD;
                break;
            case 2:
                colour = ChatColor.YELLOW;
                break;
            case 3:
                colour = ChatColor.RED;
                break;
            case 4:
                colour = ChatColor.AQUA;
                break;
            case 5:
                colour = ChatColor.LIGHT_PURPLE;
                break;
            case 6:
                colour = ChatColor.WHITE;
                break;
            case 7:
                colour = ChatColor.DARK_GRAY;
                break;
        }
        return colour;
    }

    /**
     * Get the team-id with the matching name (including colours).
     *
     * @param name
     *         (String) The name of the team.
     * @return (int) The corresponding team-id.
     */
    public static int getTeamIdFromNAME(String name) {
        try {
            int i = Integer.parseInt(name) - 1;
            return i;
        }
        catch (Exception e) {
            if (name == GREEN_NAME) {
                return 0;
            }
            else if (name == ORANGE_NAME) {
                return 1;
            }
            else if (name == YELLOW_NAME) {
                return 2;
            }
            else if (name == RED_NAME) {
                return 3;
            }
            else if (name == BLUE_NAME) {
                return 4;
            }
            else if (name == MAGENTA_NAME) {
                return 5;
            }
            else if (name == WHITE_NAME) {
                return 6;
            }
            else {
                return 7;
            }
        }
    }

    /**
     * Get the team-id of the matching team-name (the colour).
     *
     * @param name
     *         (String) The colour of the team.
     * @return (int) The corresponding team-id.
     */
    public static int getTeamId(String name) {
        try {
            return Integer.parseInt(name) - 1;
        }
        catch (Exception e) {
            switch (name.toLowerCase()) {
                case "green":
                    return 0;
                case "orange":
                    return 1;
                case "yellow":
                    return 2;
                case "red":
                    return 3;
                case "blue":
                    return 4;
                case "magenta":
                    return 5;
                case "white":
                    return 6;
                case "black":
                    return 7;
                default:
                    return -1;
            }
        }
    }

    /**
     * Get the name of the team matching the team-id.
     *
     * @param teamId
     *         (int) The ID of the team.
     * @return (String) The name of the corresponding team.
     */
    public static String getTeamNameById(int teamId) {
        switch (teamId) {
            case 0:
                return GREEN_NAME;
            case 1:
                return ORANGE_NAME;
            case 2:
                return YELLOW_NAME;
            case 3:
                return RED_NAME;
            case 4:
                return BLUE_NAME;
            case 5:
                return MAGENTA_NAME;
            case 6:
                return WHITE_NAME;
            case 7:
                return BLACK_NAME;
            default:
                return null;
        }
    }

    /**
     * Get the colour name of the team with the given id.
     */
    public static String getColourName(int teamId) {
        switch (teamId) {
            case 0:
                return "green";
            case 1:
                return "orange";
            case 2:
                return "yellow";
            case 3:
                return "red";
            case 4:
                return "blue";
            case 5:
                return "magenta";
            case 6:
                return "white";
            case 7:
                return "black";
            default:
                return null;
        }
    }
}