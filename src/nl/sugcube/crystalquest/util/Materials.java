package nl.sugcube.crystalquest.util;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author SugarCaney
 */
public class Materials {

    /**
     * A list of all different wool block materials. The index in which they appear in the list is
     * also their former data value.
     */
    public static List<Material> WOOL_BLOCKS = Collections.unmodifiableList(Arrays.asList(
            Material.WHITE_WOOL,
            Material.ORANGE_WOOL,
            Material.MAGENTA_WOOL,
            Material.LIGHT_BLUE_WOOL,
            Material.YELLOW_WOOL,
            Material.LIME_WOOL,
            Material.PINK_WOOL,
            Material.GRAY_WOOL,
            Material.LIGHT_GRAY_WOOL,
            Material.CYAN_WOOL,
            Material.PURPLE_WOOL,
            Material.BLUE_WOOL,
            Material.BROWN_WOOL,
            Material.GREEN_WOOL,
            Material.RED_WOOL,
            Material.BLACK_WOOL
    ));

    /**
     * Get the wool block material that is tied to a damage value (in legacy code).
     *
     * @param damageValue
     *         The damage value that used to represent wool with a certain colour.
     * @return The wool block material tied to the damage value.
     */
    public static Material woolFromDamageValue(int damageValue) {
        return WOOL_BLOCKS.get(damageValue);
    }

    /**
     * Get the legacy damage value of the given wool block.
     */
    public static short woolDamageValue(Material material) {
        return (short)WOOL_BLOCKS.indexOf(material);
    }

    /**
     * Checks if the given material is a wool block.
     *
     * @return {@code true} when the given material is a wool block, {@code false} otherwise.
     */
    public static boolean isWool(Material material) {
        return WOOL_BLOCKS.contains(material);
    }

    private Materials() {
    }
}