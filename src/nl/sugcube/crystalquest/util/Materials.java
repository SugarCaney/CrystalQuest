package nl.sugcube.crystalquest.util;

import org.bukkit.Material;

import java.util.*;

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
            Material.TURTLE_HELMET,
            Material.CARVED_PUMPKIN
    ));

    /**
     * A list of all different chestplate materials.
     */
    public static List<Material> ARMOUR_CHESTPLATES = Collections.unmodifiableList(Arrays.asList(
            Material.LEATHER_CHESTPLATE,
            Material.IRON_CHESTPLATE,
            Material.GOLDEN_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE,
            Material.ELYTRA
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
     * A list of all materials that must be put in the off hand.
     */
    public static List<Material> OFF_HAND = Collections.unmodifiableList(Arrays.asList(
            Material.SHIELD,
            Material.TOTEM_OF_UNDYING
    ));

    /**
     * A list of all materials that are sign posts.
     */
    public static List<Material> SIGNS = Collections.unmodifiableList(Arrays.asList(
            Material.OAK_SIGN,
            Material.SPRUCE_SIGN,
            Material.BIRCH_SIGN,
            Material.JUNGLE_SIGN,
            Material.ACACIA_SIGN,
            Material.DARK_OAK_SIGN
    ));

    /**
     * A list of all materials that are wall signs.
     */
    public static List<Material> WALL_SIGNS = Collections.unmodifiableList(Arrays.asList(
            Material.OAK_WALL_SIGN,
            Material.SPRUCE_WALL_SIGN,
            Material.BIRCH_WALL_SIGN,
            Material.JUNGLE_WALL_SIGN,
            Material.ACACIA_WALL_SIGN,
            Material.DARK_OAK_WALL_SIGN
    ));

    /**
     * Set of all materials that represent a sign, including legacy materials.
     */
    public static Set<Material> SIGNS = Collections.unmodifiableSet(EnumSet.of(
            Material.SIGN,
            Material.WALL_SIGN,
            Material.LEGACY_SIGN_POST,
            Material.LEGACY_SIGN,
            Material.LEGACY_WALL_SIGN
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

    /**
     * Checks if the given material is a sign.
     *
     * @return {@code true} when the given material is a sign, {@code false} otherwise.
     */
    public static boolean isSign(Material material) {
        return SIGNS.contains(material);
    }

    /**
     * Checks if the given material is a sign post.
     *
     * @return {@code true} when the given material is a sign post, {@code false} otherwise.
     */
    public static boolean isSignPost(Material material) {
        return SIGNS.contains(material);
    }

    /**
     * Checks if the given material is a wall sign.
     *
     * @return {@code true} when the given material is a wall sign, {@code false} otherwise.
     */
    public static boolean isWallSign(Material material) {
        return WALL_SIGNS.contains(material);
    }

    private Materials() {
    }
}