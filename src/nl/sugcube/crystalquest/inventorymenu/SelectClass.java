package nl.sugcube.crystalquest.inventorymenu;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.ClassUtils;
import nl.sugcube.crystalquest.sba.SMethods;
import nl.sugcube.crystalquest.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author SugarCaney
 */
public class SelectClass {

    public static CrystalQuest plugin;

    public SelectClass(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Gets the config-name of a certain class (technical name)
     *
     * @param trivialName
     *         The trivial name of the class (defined in the name-property)
     * @return The technical name.
     */
    public String getTechnicalClassName(String trivialName) {
        for (String key : plugin.getConfig().getConfigurationSection("kit").getKeys(false)) {
            if (SMethods.setColours(plugin.getConfig().getString("kit." + key + ".name")).equals(trivialName)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Sets a random class for the player.
     *
     * @param player
     *         The target player.
     */
    public void setRandomClass(Player player) {
        String chosenClass = "";
        boolean isOk = false;
        while (!isOk) {
            Random ran = new Random();

            List<String> classes = new ArrayList<>(plugin.getConfig().getConfigurationSection("kit").getKeys(false));

            int classId = ran.nextInt(classes.size());
            String pickedClass = classes.get(classId);
            if (player.hasPermission("crystalquest.kit." + pickedClass) ||
                    player.hasPermission("crystalquest.admin") ||
                    player.hasPermission("crystalquest.staff")) {
                chosenClass = pickedClass;
                isOk = true;
            }
        }

        plugin.inventoryManager.setPlayerClass(player, chosenClass);
    }

    /**
     * Opens the class menu for a specific player.
     *
     * @param player
     *         The target player.
     */
    public void openMenu(Player player) {
        List<String> classes = new ArrayList<>();

        /*
         * Gets a list of all classes' names.
         */
        for (String key : plugin.getConfig().getConfigurationSection("kit").getKeys(false)) {
            if (ClassUtils.hasPermission(player, key)) {
                classes.add(key);
            }
        }

        /*
         * Checks and sets the size the inventory has to be to fit in all the classes.
         */
        int inventorySize = 9;
        for (int i = 54; i >= 9; i -= 9) {
            if (classes.size() < i) {
                inventorySize = i;
            }
        }

        Inventory inv = Bukkit.createInventory(player, inventorySize, "Pick a Class");

        //Adds the random-class
        ItemStack randomItem = new ItemStack(Material.REDSTONE, 1);
        ItemMeta randomMeta = randomItem.getItemMeta();
        randomMeta.setDisplayName("Random Class");
        randomItem.setItemMeta(randomMeta);
        inv.addItem(randomItem);

        /*
         * Fills the inventory with the avaiable classes.
         */
        for (String className : classes) {
            ItemStack icon = plugin.stringHandler.toItemStack(plugin.getConfig().getString("kit." + className + ".icon"));
            ItemMeta im = icon.getItemMeta();
            String name = plugin.getConfig().getString("kit." + className + ".name");
            im.setDisplayName(SMethods.setColours(name));
            Items.hideAllFlags(icon);

            if (plugin.getConfig().getString("kit." + className + ".lore") != "") {
                List<String> lore = new ArrayList<>();
                String[] lines = plugin.getConfig().getString("kit." + className + ".lore").split("%nl%");
                for (String str : lines) {
                    lore.add(SMethods.setColours(str));
                }

                if (!ClassUtils.hasPermission(player, className)) {
                    lore.add(ChatColor.RESET + "" + ChatColor.RED + Broadcast.get("menu.not-available"));
                }

                im.setLore(lore);
            }

            icon.setItemMeta(im);
            inv.addItem(icon);
        }

        /*
         * Shows the Inventory Menu.
         */
        player.openInventory(inv);
    }
}