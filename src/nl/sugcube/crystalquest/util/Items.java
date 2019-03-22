package nl.sugcube.crystalquest.util;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author SugarCaney
 */
public class Items {

    /**
     * Hides all attribute flags from the item.
     */
    public static void hideAllFlags(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(
                ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE
        );
        item.setItemMeta(meta);
    }

    private Items() {
    }
}
