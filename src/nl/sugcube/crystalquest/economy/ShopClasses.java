package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.ClassUtils;
import nl.sugcube.crystalquest.sba.SMethods;
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
public class ShopClasses implements Listener {

    private CrystalQuest plugin;
    private Economy economy;

    public ShopClasses(CrystalQuest plugin, Economy economy) {
        this.plugin = plugin;
        this.economy = economy;
    }

    /**
     * Updates the item's names and lores.
     *
     * @param player
     *         The player who has opened the shop.
     * @param inventory
     *         The inventory to update.
     */
    public void updateMenu(Player player, Inventory inventory) {
        ItemStack[] contents = inventory.getContents();

        //CLASSES TO BUY
        int i = 0;
        for (String key : plugin.getConfig().getConfigurationSection("kit").getKeys(false)) {
            if (!plugin.getConfig().isSet("kit." + key + ".price")) {
                continue;
            }

            if (plugin.getConfig().getInt("kit." + key + ".price") <= 0) {
                continue;
            }

            if (ClassUtils.hasPermission(player, key)) {
                continue;
            }

            ItemStack icon = plugin.stringHandler.toItemStack(plugin.getConfig().getString("kit." + key + ".icon"));
            Items.hideAllFlags(icon);
            ItemMeta meta = icon.getItemMeta();
            String name = plugin.getConfig().getString("kit." + key + ".name");
            meta.setDisplayName(SMethods.setColours(name));

            if (!plugin.getConfig().getString("kit." + key + ".lore").isEmpty()) {
                List<String> lore = new ArrayList<>();
                String[] lines = plugin.getConfig().getString("kit." + key + ".lore").split("%nl%");
                for (String str : lines) {
                    lore.add(SMethods.setColours(str));
                }

                lore.add("");
                lore.add(ChatColor.RESET + "" + ChatColor.RED + Broadcast.get("shop.price") + ": " +
                        ChatColor.GOLD + plugin.getConfig().getInt("kit." + key + ".price"));

                meta.setLore(lore);
            }

            icon.setItemMeta(meta);
            contents[i] = icon;
            i++;
        }

        //NAVIGATION
        contents[45] = getItemMainMenu();
        contents[49] = economy.getItemBalance(player);

        inventory.setContents(contents);
    }

    /**
     * Shows the classes menu of the CrystalQuest-Shop.
     *
     * @param player
     *         The player to show the menu to.
     */
    public void showMenu(Player player) {
        player.closeInventory();

        Inventory inventory = Bukkit.createInventory(null, 54,
                ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Classes"
        );

        updateMenu(player, inventory);
        player.openInventory(inventory);
    }

    /*
     * Inventory handling for the main menu
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();

        if (!title.equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Classes")) {
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

        ItemMeta im = item.getItemMeta();
        if (!im.hasDisplayName()) {
            return;
        }

        String name = im.getDisplayName();
        Player buyer = (Player)event.getWhoClicked();
        Classes classes = economy.getClasses();

        /*
         * MAIN MENU
         */
        if (name.equalsIgnoreCase(ChatColor.GREEN + "Main Menu")) {
            economy.getMainMenu().showMenu(buyer);
        }
        else if (!name.contains(ChatColor.GREEN + "Crystals: " + ChatColor.GOLD) && item.getType() != Material.EMERALD) {
            String kitId = plugin.menuSelectClass.getTechnicalClassName(name);
            int price = plugin.getConfig().getInt("kit." + kitId + ".price");

            if (economy.getBalance().canAfford(buyer, price)) {
                classes.registerClass(buyer, kitId);
                economy.getBalance().addBalance(buyer, -price, false);
                showMenu(buyer);
            }
        }

        event.setCancelled(true);
    }

    /**
     * Gets the item linking to the Main Menu
     */
    public ItemStack getItemMainMenu() {
        ItemStack item = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Main Menu");
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.main-menu"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}