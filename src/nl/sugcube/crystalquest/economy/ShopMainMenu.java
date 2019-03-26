package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.util.Items;
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

    private Economy economy;

    public ShopMainMenu(Economy economy) {
        this.economy = economy;
    }

    /**
     * Shows the main menu of the CrystalQuest-Shop.
     *
     * @param player
     *         The player to show the menu to.
     */
    public void showMenu(Player player) {
        player.closeInventory();

        Inventory inventory = Bukkit.createInventory(null, 9,
                ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Menu"
        );

        ItemStack[] contents = inventory.getContents();
        contents[0] = getItemClass();
        contents[1] = getItemPowerUp();
        contents[2] = getItemCrystals();
        contents[8] = economy.getItemBalance(player);
        inventory.setContents(contents);

        player.openInventory(inventory);
    }

    /*
     * Inventory handling for the main menu
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!inventory.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Menu")) {
            return;
        }

        if (event.getCurrentItem() == null) {
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getCurrentItem();
        if (!item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) {
            return;
        }

        String name = meta.getDisplayName();
        if (name.equalsIgnoreCase(ChatColor.GREEN + "Power-Up-Grades")) {
            economy.getPowerupMenu().showMenu((Player)event.getWhoClicked());
        }
        else if (name.equalsIgnoreCase(ChatColor.AQUA + "More Crystals")) {
            economy.getCrystalMenu().showMenu((Player)event.getWhoClicked());
        }
        else if (name.equalsIgnoreCase(ChatColor.GOLD + "Buy Classes")) {
            economy.getClassesMenu().showMenu((Player)event.getWhoClicked());
        }
    }

    /**
     * Gets the item linking to the More Crystals-Menu
     */
    public ItemStack getItemCrystals() {
        ItemStack item = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "More Crystals");
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Gimme money!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item linking to the Power-Up-Grade-Menu
     */
    public ItemStack getItemPowerUp() {
        ItemStack item = new ItemStack(Material.SLIME_BALL, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Power-Up-Grades");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade your Power-Ups");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item linking to the Buy Class-Menu
     */
    public ItemStack getItemClass() {
        ItemStack item = new ItemStack(Material.GOLDEN_SWORD, 1);
        Items.hideAllFlags(item);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Buy Classes");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Buy extra classes to play with!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}