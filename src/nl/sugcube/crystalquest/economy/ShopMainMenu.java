package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SugarCaney
 */
public class ShopMainMenu implements Listener {

    public static CrystalQuest plugin;
    public static Economy economy;

    public ShopMainMenu(CrystalQuest instance, Economy eco) {
        plugin = instance;
        economy = eco;
    }

    /**
     * Shows the main menu of the CrystalQuest-Shop.
     *
     * @param p
     *         (Player) The player to show the menu to.
     */
    public void showMenu(Player p) {
        p.closeInventory();

        Inventory inv = Bukkit.createInventory(null, 9,
                ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Menu"
        );

        ItemStack[] contents = inv.getContents();
        contents[0] = getItemClass();
        contents[1] = getItemPowerUp();
        contents[2] = getItemCrystals();
        contents[8] = economy.getItemBalance(p);
        inv.setContents(contents);

        p.openInventory(inv);
    }

    /*
     * Inventory handling for the main menu
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        if (inv.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Menu")) {

            if (e.getCurrentItem() != null) {
                ItemStack item = e.getCurrentItem();

                if (item.hasItemMeta()) {
                    ItemMeta im = item.getItemMeta();
                    if (im.hasDisplayName()) {
                        String name = im.getDisplayName();

						/*
						 * POWERUP MENU
						 */
                        if (name.equalsIgnoreCase(ChatColor.GREEN + "Power-Up-Grades")) {
                            economy.getPowerupMenu().showMenu((Player)e.getWhoClicked());
                        }
						/*
						 * CRYSTALS MENU
						 */
                        else if (name.equalsIgnoreCase(ChatColor.AQUA + "More Crystals")) {
                            economy.getCrystalMenu().showMenu((Player)e.getWhoClicked());
                        }
						/*
						 * CLASSES MENU
						 */
                        else if (name.equalsIgnoreCase(ChatColor.GOLD + "Buy Classes")) {
                            economy.getClassesMenu().showMenu((Player)e.getWhoClicked());
                        }

                    }
                }
            }


            e.setCancelled(true);
        }
    }

    /**
     * Gets the item linking to the More Crystals-Menu
     *
     * @return (ItemStack)
     */
    public ItemStack getItemCrystals() {
        ItemStack is = new ItemStack(Material.DIAMOND, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.AQUA + "More Crystals");
        is.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Gimme money!");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item linking to the Power-Up-Grade-Menu
     *
     * @return (ItemStack)
     */
    public ItemStack getItemPowerUp() {
        ItemStack is = new ItemStack(Material.SLIME_BALL, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Power-Up-Grades");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade your Power-Ups");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item linking to the Buy Class-Menu
     *
     * @return (ItemStack)
     */
    public ItemStack getItemClass() {
        ItemStack is = new ItemStack(Material.GOLDEN_SWORD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Buy Classes");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Buy extra classes to play with!");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }
}