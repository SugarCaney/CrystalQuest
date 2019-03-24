package nl.sugcube.crystalquest;

import nl.sugcube.crystalquest.items.WandType;
import nl.sugcube.crystalquest.sba.SItem;
import nl.sugcube.crystalquest.sba.SMethods;
import nl.sugcube.crystalquest.util.Potions;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Arrays;

/**
 * @author SugarCaney
 */
public class StringHandler {

    public static CrystalQuest plugin;

    public StringHandler(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Turns a string into an actual ItemStack.
     * <p>
     * Parses the items in the following format:<br> {@code item;customName,amount,[e
     * ENCHANTMENTS],[p POTIONEFFECTS],[d DAMAGE]}
     * <p>
     * Here {@code ENCHANTMENTS} is a list with even length: {@code enchantment_name;level}.<br>
     * {@code POTIONEFFECTS} is a list with length % 3: {@code potion_effect_name;level;duration}.<br>
     * {@code DAMAGE} is an integer representing how much damage the item has taken.
     * <p>
     * Colour codes for the custom name are allowed.
     * <p>
     * The {@code [e]}, {@code [p]}, {@code [d]}, amount and custom name parts are optional. Omitted
     * amounts will default to 1.
     *
     * @param itemString
     *         The string to convert
     * @return The item the string represents. Returns null if the string couldn't be parsed.
     */
    public ItemStack toItemStack(String itemString) {
        String[] parts = itemString.split(",");

        if (parts.length == 1) {
            return createBaseItemStack(parts[0], "1");
        }

        ItemStack itemStack = createBaseItemStack(parts[0], parts[1]);
        applyCustomName(itemStack, parts[0]);
        for (int i = 2; i < parts.length; i++) {
            applyMetaInformation(itemStack, parts[i]);
        }

        return itemStack;
    }

    /**
     * Applies meta information to the item stack. This method will automatically switch type: e
     * (enchantments), p (potion effects), d (damage).
     */
    private void applyMetaInformation(ItemStack itemStack, String metaString) {
        if (metaString.startsWith("[e")) {
            String meta = removeMetaBrackets(metaString);
            applyEnchantmentMeta(itemStack, meta.split(";"));
        }
        else if (metaString.startsWith("[p")) {
            String meta = removeMetaBrackets(metaString);
            applyPotionEffectMeta(itemStack, meta.split(";"));
        }
        else if (metaString.startsWith("[d")) {
            String meta = removeMetaBrackets(metaString);
            applyDamageMeta(itemStack, meta);
        }
        else {
            plugin.getLogger().warning("Unknown item meta type '" + metaString + "', ignored.");
        }
    }

    /**
     * Removes the brackets from a meta string, leaving the trimmed contents.
     */
    private String removeMetaBrackets(String metaString) {
        return metaString.substring(2, metaString.length() - 1).trim();
    }

    /**
     * Applies the enchantments to the item.
     *
     * @param enchantmentList
     *         A list of pairs of enchantment names and levels.
     */
    private void applyEnchantmentMeta(ItemStack itemStack, String[] enchantmentList) {
        if (enchantmentList.length % 2 != 0) {
            plugin.getLogger().warning("Enchantment meta does not have enough arguments (expected an even number): " +
                    Arrays.toString(enchantmentList) + ", ignoring applying enchantments."
            );
            return;
        }

        for (int i = 0; i < enchantmentList.length; i += 2) {
            String enchantmentName = enchantmentList[i];
            String enchantmentLevelString = enchantmentList[i + 1];

            Enchantment enchantment = parseEnchantment(enchantmentName);
            if (enchantment == null) {
                plugin.getLogger().warning("Invalid enchantment: '" + enchantmentName + "', ignored.");
                continue;
            }
            int level = 1;
            try {
                level = Integer.parseInt(enchantmentLevelString);
            }
            catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid level number: '" + enchantmentLevelString + "', defaulting to 1.");
            }

            itemStack.addUnsafeEnchantment(enchantment, level);
        }
    }

    /**
     * Applies the potion effects to the item.
     *
     * @param potionEffectList
     *         A list of triples of potion effect names, levels, and duration.
     */
    private void applyPotionEffectMeta(ItemStack itemStack, String[] potionEffectList) {
        if (potionEffectList.length % 3 != 0) {
            plugin.getLogger().warning("Potion effect meta does not have enough arguments (expected a multiple of 3): " +
                    Arrays.toString(potionEffectList) + ", ignoring applying potion effects."
            );
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (!(meta instanceof PotionMeta)) {
            plugin.getLogger().warning("Item '" + itemStack.getType() + "' does not support potion effects.");
            return;
        }

        PotionMeta potionMeta = (PotionMeta)meta;
        for (int i = 0; i < potionEffectList.length; i += 3) {
            String effectName = potionEffectList[i];
            String levelString = potionEffectList[i + 1];
            String durationString = potionEffectList[i + 2];

            PotionEffectType type = parsePotionEffectType(effectName);
            if (type == null) {
                plugin.getLogger().warning("Invalid potion effect: '" + effectName + "', ignored.");
                continue;
            }
            int level = 0;
            try {
                level = Integer.parseInt(levelString);
            }
            catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid level number: '" + levelString + ", defaulting to 0.");
            }
            int duration = 1;
            try {
                duration = Integer.parseInt(durationString);
            }
            catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid duration: '" + durationString + "', defaulting to 1.");
            }

            PotionEffect effect = new PotionEffect(type, duration, level);
            potionMeta.addCustomEffect(effect, false);

            PotionType potionType = Potions.getPotionType(effectName);
            if (potionType != null) {
                potionMeta.setBasePotionData(new PotionData(potionType));
            }
        }
        itemStack.setItemMeta(meta);
    }

    /**
     * Applies the damage value to the item.
     */
    private void applyDamageMeta(ItemStack itemStack, String damageValue) {
        ItemMeta meta = itemStack.getItemMeta();
        if (!(meta instanceof Damageable)) {
            plugin.getLogger().warning("Item '" + itemStack.getType() + "' does not support damage values.");
            return;
        }

        int damage = 0;
        try {
            damage = Integer.parseInt(damageValue);
        }
        catch (NumberFormatException e) {
            plugin.getLogger().warning("Invalid damage value: '" + damageValue + "', defaulting to 0.");
        }

        Damageable damageable = (Damageable)meta;
        damageable.setDamage(damage);
        itemStack.setItemMeta(meta);
    }

    /**
     * Creates an item stack from the given itemname (including custom name if applicable) and
     * amount.
     */
    private ItemStack createBaseItemStack(String itemName, String amountString) {
        String[] parts = itemName.split(";");

        // Check if the item is a wand.
        ItemStack wand = createWand(parts[0]);
        if (wand != null) {
            return wand;
        }

        // Otherwise, it's a regular item.
        Material material = parseMaterial(parts[0]);
        int amount = 1;
        try {
            amount = Integer.parseInt(amountString);
        }
        catch (NumberFormatException e) {
            plugin.getLogger().warning("Could not parse item amount '" + amountString + "', defaulting to 1.");
        }

        return new ItemStack(material, amount);
    }

    /**
     * Creates a wand item stack from the wand name. Returns {@code null} when there is no wand with
     * the given name.
     */
    private ItemStack createWand(String materialName) {
        String lowercaseMaterialName = materialName.toLowerCase();
        switch (lowercaseMaterialName) {
            case "wand_fire":
                return plugin.wand.getWand(WandType.MAGMA);
            case "wand_ender":
                return plugin.wand.getWand(WandType.TELEPORT);
            case "wand_healing":
                return plugin.wand.getWand(WandType.HEAL);
            case "wand_ice":
                return plugin.wand.getWand(WandType.FREEZE);
            case "wand_wither":
                return plugin.wand.getWand(WandType.WITHER);
            default:
                return null;
        }
    }

    /**
     * Applies formatting colours to the given string.
     */
    private String applyColours(String string) {
        return SMethods.setColours(string);
    }

    /**
     * Applies the custom name (if applicable).
     */
    private void applyCustomName(ItemStack itemStack, String itemName) {
        String[] parts = itemName.split(";");
        if (parts.length < 2) {
            return;
        }

        String coloured = applyColours(parts[1]);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(coloured);
        itemStack.setItemMeta(meta);
    }

    /**
     * Looks up the {@link Material} with the given material name.
     */
    public Material parseMaterial(String materialName) {
        Material material = Material.getMaterial(materialName.toLowerCase());
        if (material == null) {
            return SItem.toMaterial(materialName);
        }
        else {
            return material;
        }
    }

    /**
     * Looks up the {@link PotionEffectType} with the given name.
     */
    public PotionEffectType parsePotionEffectType(String potionEffectTypeName) {
        PotionEffectType type = PotionEffectType.getByName(potionEffectTypeName.toLowerCase());
        if (type == null) {
            return Potions.parseType(potionEffectTypeName);
        }
        else {
            return type;
        }
    }

    /**
     * Looks up the {@link Enchantment} with the given name.
     */
    public Enchantment parseEnchantment(String enchantmentName) {
        NamespacedKey key = NamespacedKey.minecraft(enchantmentName.toLowerCase());
        return Enchantment.getByKey(key);
    }
}