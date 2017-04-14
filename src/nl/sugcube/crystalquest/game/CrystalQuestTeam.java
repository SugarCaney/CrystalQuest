package nl.sugcube.crystalquest.game;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import java.util.*;
import java.util.function.Function;

/**
 * @author SugarCaney
 */
public enum CrystalQuestTeam {

    GREEN("green", ChatColor.GREEN, DyeColor.LIME, (short)5),
    ORANGE("orange", ChatColor.GOLD, DyeColor.ORANGE, (short)1),
    YELLOW("yellow", ChatColor.YELLOW, DyeColor.YELLOW, (short)4),
    RED("red", ChatColor.RED, DyeColor.RED, (short)14),
    BLUE("blue", ChatColor.AQUA, DyeColor.LIGHT_BLUE, (short)3),
    MAGENTA("magenta", ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA, (short)2),
    WHITE("white", ChatColor.WHITE, DyeColor.WHITE, (short)0),
    BLACK("black", ChatColor.BLACK, DyeColor.BLACK, (short)15);

    private final String name;
    private final ChatColor chatColour;
    private final DyeColor dyeColour;
    private final short dataValueWool;

    private static final Random RANDOM = new Random();
    private static final CrystalQuestTeam[] VALUES_ARRAY = values();
    private static final Set<CrystalQuestTeam> VALUES = EnumSet.of(
            GREEN, ORANGE, YELLOW, RED, BLUE, MAGENTA, WHITE, BLACK
    );

    /**
     * @param name
     *         The name of the colour of the team.
     * @param chat
     *         The colour that shows up in chat.
     * @param dye
     *         The dye colour of the team.
     */
    CrystalQuestTeam(String name, ChatColor chat, DyeColor dye, short dataValueWool) {
        this.name = name.toLowerCase();
        this.chatColour = chat;
        this.dyeColour = dye;
        this.dataValueWool = dataValueWool;
    }

    /**
     * Get the amount of possible teams.
     */
    public static int count() {
        return VALUES.size();
    }

    /**
     * Get the CrystalQuest team that has the given ID.
     *
     * @param id
     *         ID in range 0-{@link CrystalQuestTeam#count()} inclusive.
     * @throws IllegalArgumentException
     *         When the ID does not exist.
     */
    public static CrystalQuestTeam valueOf(int id) throws IllegalArgumentException {
        if (id < 0 || id >= count()) {
            throw new IllegalArgumentException("Invalid id " + id);
        }

        return VALUES_ARRAY[id];
    }

    /**
     * Get the CrystalQuest team that has the given dye colour.
     *
     * @param dyeColour
     *         The dye colour of the team.
     * @throws IllegalArgumentException
     *         When there is no team with the given dye colour.
     */
    public static CrystalQuestTeam valueOf(DyeColor dyeColour) throws IllegalArgumentException {
        return filterFind(dyeColour, CrystalQuestTeam::getDyeColour);
    }

    /**
     * Get the CrystalQuest team that has the given data value for wool.
     *
     * @param dataValueWool
     *         The datavalue of the wool representing the team.
     * @throws IllegalArgumentException
     *         When there is no team with the given data value for wool.
     */
    public static CrystalQuestTeam valueOf(short dataValueWool) throws IllegalArgumentException {
        return filterFind(dataValueWool, CrystalQuestTeam::getDataValueWool);
    }

    /**
     * Get the CrystalQuest team that has the given name
     *
     * @param name
     *         Either the full name incuding colours, just the colour name (lowercase) or the id of
     *         the team +1 (so 1-indexed).
     * @throws IllegalArgumentException
     *         When there is no team with the given name.
     */
    public static CrystalQuestTeam valueOfName(String name) throws IllegalArgumentException {
        // First try parsing the ID.
        try {
            int i = Integer.parseInt(name) - 1;
            return valueOf(i);
        }
        // Otherwise search for names.
        catch (NumberFormatException nfe) {
            try {
                return valueOfColouredName(name);
            }
            catch (IllegalArgumentException iae) {
                return filterFind(name.toLowerCase(), CrystalQuestTeam::getName);
            }
        }
    }

    /**
     * Get the CrystalQuest team that has the given name.
     *
     * @param teamName
     *         The name of the team. Just the colour name.
     * @throws IllegalArgumentException
     *         When there is no team with the given name.
     */
    public static CrystalQuestTeam valueOfTeamName(String teamName)
            throws IllegalArgumentException {
        return filterFind(teamName.toLowerCase(), CrystalQuestTeam::getName);
    }

    /**
     * Get the CrystalQuest team that has the given string for a coloured name (e.g. {@code
     * &aTeam Green}.
     *
     * @param colouredName
     *         The name including chat colours and team-prefix.
     * @throws IllegalArgumentException
     *         When there is no team with the given name.
     */
    public static CrystalQuestTeam valueOfColouredName(String colouredName)
            throws IllegalArgumentException {
        return filterFind(colouredName, CrystalQuestTeam::toString);
    }

    /**
     * Simple filter/find first stream.
     *
     * @throws IllegalArgumentException
     *         When no team could be found.
     */
    private static <T> CrystalQuestTeam filterFind(T obj, Function<CrystalQuestTeam, T> fun)
            throws IllegalArgumentException {
        return VALUES.stream()
                .filter(team -> Objects.equals(obj, fun.apply(team)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "There is no team for " + obj)
                );
    }

    /**
     * Get an unmodifiable set of all supported teams.
     */
    public static Set<CrystalQuestTeam> getValues() {
        return Collections.unmodifiableSet(VALUES);
    }

    public int getId() {
        return ordinal();
    }

    public ChatColor getChatColour() {
        return chatColour;
    }

    public DyeColor getDyeColour() {
        return dyeColour;
    }

    public short getDataValueWool() {
        return dataValueWool;
    }

    public String getName() {
        return name;
    }

    /**
     * @return {@code ChatColor.X + "Team X"}
     */
    @Override
    public String toString() {
        return getChatColour() + "Team " + getName();
    }
}