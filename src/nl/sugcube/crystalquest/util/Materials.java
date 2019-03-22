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
     * A list of all different skull materials. The index in which they appear in the list is also
     * their former data value.
     */
    public static List<Material> SKULLS = Collections.unmodifiableList(Arrays.asList(
            Material.SKELETON_SKULL,
            Material.WITHER_SKELETON_SKULL,
            Material.ZOMBIE_HEAD,
            Material.PLAYER_HEAD,
            Material.CREEPER_HEAD
    ));

    /**
     * A list of all different helmet materials.
     */
    public static List<Material> ARMOUR_HELMETS = Collections.unmodifiableList(Arrays.asList(
            Material.LEATHER_HELMET,
            Material.IRON_HELMET,
            Material.GOLDEN_HELMET,
            Material.CHAINMAIL_HELMET,
            Material.DIAMOND_HELMET,
            Material.TURTLE_HELMET
    ));

    /**
     * A list of all different chestplate materials.
     */
    public static List<Material> ARMOUR_CHESTPLATES = Collections.unmodifiableList(Arrays.asList(
            Material.LEATHER_CHESTPLATE,
            Material.IRON_CHESTPLATE,
            Material.GOLDEN_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE
    ));

    /**
     * A list of all different leggings materials.
     */
    public static List<Material> ARMOUR_LEGGINGS = Collections.unmodifiableList(Arrays.asList(
            Material.LEATHER_LEGGINGS,
            Material.IRON_LEGGINGS,
            Material.GOLDEN_LEGGINGS,
            Material.CHAINMAIL_LEGGINGS,
            Material.DIAMOND_LEGGINGS
    ));

    /**
     * A list of all different boot materials.
     */
    public static List<Material> ARMOUR_BOOTS = Collections.unmodifiableList(Arrays.asList(
            Material.LEATHER_BOOTS,
            Material.IRON_BOOTS,
            Material.GOLDEN_BOOTS,
            Material.CHAINMAIL_BOOTS,
            Material.DIAMOND_BOOTS
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

    /**
     * Checks if the given material is a skull/head block.
     *
     * @return {@code true} when the given material is a skull/head block, {@code false} otherwise.
     */
    public static boolean isSkull(Material material) {
        return SKULLS.contains(material);
    }

    private Materials() {
    }
}