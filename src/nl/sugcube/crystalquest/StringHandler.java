package nl.sugcube.crystalquest;

import nl.sugcube.crystalquest.items.WandType;
import nl.sugcube.crystalquest.sba.SEnch;
import nl.sugcube.crystalquest.sba.SItem;
import nl.sugcube.crystalquest.sba.SMeth;
import nl.sugcube.crystalquest.util.Materials;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author SugarCaney
 */
public class StringHandler {

    public static CrystalQuest plugin;

    /**
     * CONSTRUCT
     * Passes through the actual plugin.
     *
     * @param instance
     *         (CrystalQuest) The instance of the plugin.
     */
    public StringHandler(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Get the name of a block without the capitals and underscores
     *
     * @param string
     *         (String) Inputstring (Material-name)
     * @return (String) The friendly name
     */
    public String getFriendlyItemName(String string) {
        String first = string.substring(0, 1).toUpperCase();
        String last = string.substring(1, string.length()).toLowerCase();
        string = first + last;
        return string.replaceAll("_", " ");
    }

    /**
     * Turns a string into an actual ItemStack
     *
     * @param s
     *         (String) The string to convert
     * @return (ItemStack) The item the string represents. Returns null if the string couldn't be
     * parsed.
     */
    public ItemStack toItemStack(String s) {
        ItemStack is = null;

        try {
            String[] item = s.split(",");

            if (item[0].split(";")[0].equalsIgnoreCase("wand_fire")) {
                is = plugin.wand.getWand(WandType.MAGMA);
            }
            else if (item[0].split(";")[0].equalsIgnoreCase("wand_ender")) {
                is = plugin.wand.getWand(WandType.TELEPORT);
            }
            else if (item[0].split(";")[0].equalsIgnoreCase("wand_healing")) {
                is = plugin.wand.getWand(WandType.HEAL);
            }
            else if (item[0].split(";")[0].equalsIgnoreCase("wand_ice")) {
                is = plugin.wand.getWand(WandType.FREEZE);
            }
            else if (item[0].split(";")[0].equalsIgnoreCase("wand_wither")) {
                is = plugin.wand.getWand(WandType.WITHER);
            }
            else if (item.length == 1) {
                is = new ItemStack(SItem.toMaterial(item[0].split(";")[0]), 1);
            }
            else if (item.length == 2) {
                is = new ItemStack(SItem.toMaterial(item[0].split(";")[0]), Integer.parseInt(item[1]));
            }
            else if (item.length >= 3) {
                is = new ItemStack(SItem.toMaterial(item[0].split(";")[0]), Integer.parseInt(item[1]), Short.parseShort(item[2]));
            }
            if (item.length >= 5) {
                is.addUnsafeEnchantment(SEnch.toEnchantment(item[3]), Integer.parseInt(item[4]));
            }
            if (item.length >= 7) {
                is.addUnsafeEnchantment(SEnch.toEnchantment(item[5]), Integer.parseInt(item[6]));
            }
            if (item.length >= 9) {
                is.addUnsafeEnchantment(SEnch.toEnchantment(item[7]), Integer.parseInt(item[8]));
            }
            if (item.length >= 11) {
                is.addUnsafeEnchantment(SEnch.toEnchantment(item[9]), Integer.parseInt(item[10]));
            }
            if (item.length >= 13) {
                is.addUnsafeEnchantment(SEnch.toEnchantment(item[11]), Integer.parseInt(item[12]));
            }
            if (item.length >= 15) {
                is.addUnsafeEnchantment(SEnch.toEnchantment(item[13]), Integer.parseInt(item[14]));
            }

            if (item[0].split(";").length > 1) {
                String newName = SMeth.setColours(item[0].split(";")[1]);
                if (Materials.isSkull(is.getType())) {
                    is.setDurability((short)3);
                    SkullMeta im = (SkullMeta)is.getItemMeta();
                    im.setOwner(newName);
                    is.setItemMeta(im);
                }
                else {
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(newName);
                    is.setItemMeta(im);
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return is;
    }
}