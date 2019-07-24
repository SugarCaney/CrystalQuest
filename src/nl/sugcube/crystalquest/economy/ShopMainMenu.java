package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.Broadcast;
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

        Inventory inventory = Bukkit.createInventory(null, 9, getMenuTitle());

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
        if (!inventory.getName().equalsIgnoreCase(getMenuTitle())) {
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
        if (name.equalsIgnoreCase(ChatColor.GREEN + Broadcast.get("shop.power-up-grades"))) {
            economy.getPowerupMenu().showMenu((Player)event.getWhoClicked());
        }
        else if (name.equalsIgnoreCase(ChatColor.AQUA + Broadcast.get("shop.more-crystals"))) {
            economy.getCrystalMenu().showMenu((Player)event.getWhoClicked());
        }
        else if (name.equalsIgnoreCase(ChatColor.GOLD + Broadcast.get("shop.buy-classes"))) {
            economy.getClassesMenu().showMenu((Player)event.getWhoClicked());
        }
    }

    /**
     * Gets the item linking to the More Crystals-Menu
     */
    public ItemStack getItemCrystals() {
        ItemStack item = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + Broadcast.get("shop.more-crystals"));
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.more-crystals-lore"));
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
        meta.setDisplayName(ChatColor.GREEN + Broadcast.get("shop.power-up-grades"));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.power-up-grades-lore"));
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
        meta.setDisplayName(ChatColor.GOLD + Broadcast.get("shop.buy-classes"));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.buy-classes-lore"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private String getMenuTitle() {
        return Broadcast.get("shop.shop") + ":" + ChatColor.GOLD + " " + Broadcast.get("menu.menu");
    }
}