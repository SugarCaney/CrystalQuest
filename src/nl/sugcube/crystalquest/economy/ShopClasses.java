package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Classes;
import nl.sugcube.crystalquest.sba.SMeth;
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

    public static CrystalQuest plugin;
    public static Economy economy;

    public ShopClasses(CrystalQuest instance, Economy eco) {
        plugin = instance;
        economy = eco;
    }

    /**
     * Updates the item's names and lores.
     *
     * @param p
     *         (Player) The player who has opened the shop.
     * @param inv
     *         (Inventory) The inventory to update.
     */
    public void updateMenu(Player p, Inventory inv) {
        ItemStack[] contents = inv.getContents();

        //CLASSES TO BUY
        int i = 0;
        for (String key : plugin.getConfig().getConfigurationSection("kit").getKeys(false)) {
            if (plugin.getConfig().isSet("kit." + key + ".price")) {
                if (plugin.getConfig().getInt("kit." + key + ".price") > 0) {
                    if (!Classes.hasPermission(p, key)) {
                        ItemStack icon = plugin.sh.toItemStack(plugin.getConfig().getString("kit." + key + ".icon"));
                        ItemMeta im = icon.getItemMeta();
                        String name = plugin.getConfig().getString("kit." + key + ".name");
                        im.setDisplayName(SMeth.setColours(name));

                        if (plugin.getConfig().getString("kit." + key + ".lore") != "") {
                            List<String> lore = new ArrayList<>();
                            String[] lines = plugin.getConfig().getString("kit." + key + ".lore").split("%nl%");
                            for (String str : lines) {
                                lore.add(SMeth.setColours(str));
                            }

                            lore.add("");
                            lore.add(ChatColor.RESET + "" + ChatColor.RED + Broadcast.get("shop.price") + ": " +
                                    ChatColor.GOLD + plugin.getConfig().getInt("kit." + key + ".price"));

                            im.setLore(lore);
                        }

                        icon.setItemMeta(im);
                        contents[i] = icon;
                        i++;
                    }
                }
            }
        }

        //NAVIGATION
        contents[45] = getItemMainMenu();
        contents[49] = economy.getItemBalance(p);

        inv.setContents(contents);
    }

    /**
     * Shows the classes menu of the CrystalQuest-Shop.
     *
     * @param p
     *         (Player) The player to show the menu to.
     */
    public void showMenu(Player p) {
        p.closeInventory();

        Inventory inv = Bukkit.createInventory(null, 54,
                ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Classes"
        );

        updateMenu(p, inv);
        p.openInventory(inv);
    }

    /*
     * Inventory handling for the main menu
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        if (inv.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Classes")) {

            if (e.getCurrentItem() != null) {
                ItemStack item = e.getCurrentItem();

                if (item.hasItemMeta()) {
                    ItemMeta im = item.getItemMeta();
                    if (im.hasDisplayName()) {
                        String name = im.getDisplayName();
                        Player p = (Player)e.getWhoClicked();

						/*
                         * MAIN MENU
						 */
                        if (name.equalsIgnoreCase(ChatColor.GREEN + "Main Menu")) {
                            economy.getMainMenu().showMenu(p);
                        }
                        else if (!name.contains(ChatColor.GREEN + "Crystals: " + ChatColor.GOLD) &&
                                item.getType() != Material.EMERALD) {
                            String techName = plugin.menuSC.getTechnicalClassName(name);
                            int price = plugin.getConfig().getInt("kit." + techName + ".price");

                            if (economy.getBalance().canAfford(p, price)) {
                                if (plugin.getData().isSet("shop.classes." + p.getUniqueId().toString())) {
                                    List<String> list = plugin.getData().getStringList("shop.classes." + p.getUniqueId().toString());
                                    list.add(techName);
                                    plugin.getData().set("shop.classes." + p.getUniqueId().toString(), list);
                                }
                                else {
                                    List<String> list = new ArrayList<>();
                                    list.add(techName);
                                    plugin.getData().set("shop.classes." + p.getUniqueId().toString(), list);
                                }
                                economy.getBalance().addCrystals(p, -price, false);

                                showMenu(p);
                            }
                        }

                        e.setCancelled(true);
                    }
                }
            }

            e.setCancelled(true);
        }
    }

    /**
     * Gets the item linking to the Main Menu
     *
     * @return (ItemStack)
     */
    public ItemStack getItemMainMenu() {
        ItemStack is = new ItemStack(Material.ARROW, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Main Menu");
        is.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.main-menu"));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }
}