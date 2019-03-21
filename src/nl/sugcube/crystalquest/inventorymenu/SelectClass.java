package nl.sugcube.crystalquest.inventorymenu;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Classes;
import nl.sugcube.crystalquest.sba.SMeth;
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

    /**
     * CONSTRUCTOR
     * Passes through the instance of the plugin.
     *
     * @param instance
     *         (CrystalQuest) The instance of the plugin.
     */
    public SelectClass(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Gets the config-name of a certain class (technical name)
     *
     * @param trivialName
     *         (String) The trivial name of the class (defined in the name-property)
     * @return (String) The technical name.
     */
    public String getTechnicalClassName(String trivialName) {
        for (String key : plugin.getConfig().getConfigurationSection("kit").getKeys(false)) {
            if (SMeth.setColours(plugin.getConfig().getString("kit." + key + ".name")).equals(trivialName)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Sets a random class for the player.
     *
     * @param player
     *         (Player) The target player.
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

        plugin.im.setPlayerClass(player, chosenClass);
    }

    /**
     * Opens the class menu for a specific player.
     *
     * @param p
     *         (Player) The target player.
     */
    public void openMenu(Player p) {

        List<String> classes = new ArrayList<>();

		/*
         * Gets a list of all classes' names.
		 */
        for (String key : plugin.getConfig().getConfigurationSection("kit").getKeys(false)) {
            if (Classes.hasPermission(p, key)) {
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

        Inventory inv = Bukkit.createInventory(p, inventorySize, "Pick a Class");

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
            ItemStack icon = plugin.sh.toItemStack(plugin.getConfig().getString("kit." + className + ".icon"));
            ItemMeta im = icon.getItemMeta();
            String name = plugin.getConfig().getString("kit." + className + ".name");
            im.setDisplayName(SMeth.setColours(name));

            if (plugin.getConfig().getString("kit." + className + ".lore") != "") {
                List<String> lore = new ArrayList<>();
                String[] lines = plugin.getConfig().getString("kit." + className + ".lore").split("%nl%");
                for (String str : lines) {
                    lore.add(SMeth.setColours(str));
                }

                if (!Classes.hasPermission(p, className)) {
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
        p.openInventory(inv);
    }
}