package nl.sugcube.crystalquest.economy;

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
public class ShopCrystals implements Listener {

    private static final String PREFIX = "" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.ITALIC;
    private static final String PREFIX_RED = "" + ChatColor.RED + ChatColor.BOLD + ChatColor.ITALIC;

    private Economy economy;

    public ShopCrystals(Economy economy) {
        this.economy = economy;
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

        //STATUS BAR
        contents[8] = getItemStatusExp(p);
        contents[17] = getItemStatusSmash(p);
        contents[35] = getItemStatusWin(p);
        contents[44] = getItemStatusKill(p);

        //ITEMS TO BUY
        contents[11] = getItemBuyExp(p);
        contents[13] = getItemBuySmash(p);
        contents[29] = getItemBuyWin(p);
        contents[31] = getItemBuyKill(p);

        //NAVIGATION
        contents[45] = getItemMainMenu();
        contents[49] = economy.getItemBalance(p);

        inv.setContents(contents);
    }

    /**
     * Shows the crystal menu of the CrystalQuest-Shop.
     *
     * @param player
     *         The player to show the menu to.
     */
    public void showMenu(Player player) {
        player.closeInventory();

        Inventory inv = Bukkit.createInventory(null, 54,
                ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Crystals"
        );

        updateMenu(player, inv);
        player.openInventory(inv);
    }

    /*
     * Inventory handling for the main menu
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!inventory.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Crystals")) {
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

        // Main Menu.
        if (name.equalsIgnoreCase(ChatColor.GREEN + "Main Menu")) {
            economy.getMainMenu().showMenu((Player)event.getWhoClicked());
        }
        // Buy XP.
        else if (name.equalsIgnoreCase(PREFIX + "BUY " + ChatColor.GREEN + "Xp-Bonus")) {
            buyClass(buyer, ShopUpgrade.CRYSTALS_XP, event.getInventory());
        }
        // Buy smash.
        else if (name.equalsIgnoreCase(PREFIX + "BUY " + ChatColor.LIGHT_PURPLE + "Smash-Bonus")) {
            buyClass(buyer, ShopUpgrade.CRYSTALS_SMASH, event.getInventory());
        }
        // Buy win.
        else if (name.equalsIgnoreCase(PREFIX + "BUY " + ChatColor.YELLOW + "Win-Cash")) {
            buyClass(buyer, ShopUpgrade.CRYSTALS_WIN, event.getInventory());
        }
        // buy blood.
        else if (name.equalsIgnoreCase(PREFIX + "BUY " + ChatColor.RED + "Blood Diamonds")) {
            buyClass(buyer, ShopUpgrade.CRYSTALS_BLOOD, event.getInventory());
        }

        event.setCancelled(true);
    }

    /**
     * Let the player buy the chosen crystal-upgrade.
     *
     * @param player
     *         The buyer.
     * @param upgrade
     *         The upgrade the player buys.
     * @param inventory
     *         The inventory-instance of the shop.
     * @return True if able to, false if they couldn't buy the upgrade.
     */
    public boolean buyClass(Player player, ShopUpgrade upgrade, Inventory inventory) {
        Balance balance = economy.getBalance();
        Upgrades upgrades = economy.getUpgrades();

        int level = upgrades.getLevel(player, upgrade) + 1;
        if (balance.canAfford(player, economy.getUpgradeCosts(level))) {
            balance.addBalance(player, -economy.getUpgradeCosts(level), false);
            updateMenu(player, inventory);
            upgrades.setLevel(player, upgrade, level);
            showMenu(player);
            return true;
        }

        return false;
    }

    /**
     * Gets the item showing BLOOD buyable item
     */
    public ItemStack getItemBuyKill(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = item.getItemMeta();

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.CRYSTALS_BLOOD);
        if (level < 5) {
            meta.setDisplayName(PREFIX + "BUY " + ChatColor.RED + "Blood Diamonds");
        }
        else {
            meta.setDisplayName(PREFIX_RED + "MAX " + ChatColor.RED + "Blood Diamonds");
        }

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade to: " + ChatColor.GREEN + "Lvl " + (level + 1));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Double chance: " + ChatColor.GREEN + "+20%");
        lore.add("");
        lore.add(ChatColor.RED + "Price: " + ChatColor.GOLD + economy.getUpgradeCosts(level + 1));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing WIN buyable item
     */
    public ItemStack getItemBuyWin(Player player) {
        ItemStack item = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta meta = item.getItemMeta();

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.CRYSTALS_WIN);
        if (level < 5) {
            meta.setDisplayName(PREFIX + "BUY " + ChatColor.YELLOW + "Win-Cash");
        }
        else {
            meta.setDisplayName(PREFIX_RED + "MAX " + ChatColor.YELLOW + "Win-Cash");
        }

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade to: " + ChatColor.GREEN + "Lvl " + (level + 1));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Cash on win: " + ChatColor.GREEN + "+10%");
        lore.add("");
        lore.add(ChatColor.RED + "Price: " + ChatColor.GOLD + economy.getUpgradeCosts(level + 1));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing SMASH buyable item
     */
    public ItemStack getItemBuySmash(Player player) {
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = item.getItemMeta();

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.CRYSTALS_SMASH);
        if (level < 5) {
            meta.setDisplayName(PREFIX + "BUY " + ChatColor.LIGHT_PURPLE + "Smash-Bonus");
        }
        else {
            meta.setDisplayName(PREFIX_RED + "MAX " + ChatColor.LIGHT_PURPLE + "Smash-Bonus");
        }

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade to: " + ChatColor.GREEN + "Lvl " + (level + 1));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Double chance: " + ChatColor.GREEN + "+10%");
        lore.add("");
        lore.add(ChatColor.RED + "Price: " + ChatColor.GOLD + economy.getUpgradeCosts(level + 1));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing XP buyable item
     */
    public ItemStack getItemBuyExp(Player player) {
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        ItemMeta meta = item.getItemMeta();

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.CRYSTALS_XP);
        if (level < 5) {
            meta.setDisplayName(PREFIX + "BUY " + ChatColor.GREEN + "Xp-Bonus");
        }
        else {
            meta.setDisplayName(PREFIX_RED + "MAX " + ChatColor.GREEN + "Xp-Bonus");
        }

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade to: " + ChatColor.GREEN + "Lvl " + (level + 1));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Crystals: " + ChatColor.GREEN + "+1");
        lore.add("");
        lore.add(ChatColor.RED + "Price: " + ChatColor.GOLD + economy.getUpgradeCosts(level + 1));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing Kill status
     */
    public ItemStack getItemStatusKill(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Blood Diamonds");

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.CRYSTALS_BLOOD);
        String multiplier = "" + Multipliers.getMultiplier("blood", level, true);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current level: " + ChatColor.GREEN + "Lvl " + level);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Chance double cash: " + ChatColor.GREEN +
                multiplier.replace(".0", "") + "%");
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Increase the chance of getting");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "double crystals (balance) when you");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "kill someone");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing Crystal-Win status
     */
    public ItemStack getItemStatusWin(Player player) {
        ItemStack item = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Win-Cash");

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.CRYSTALS_WIN);
        String multiplier = "" + Multipliers.getMultiplier("win", level, true);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current level: " + ChatColor.GREEN + "Lvl " + level);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Win-Cash multiplier: " + ChatColor.GREEN +
                multiplier.substring(0, 3) + "%");
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Increase the amount of crystals");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "you get when you win a quest.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing Crystal-Smash status
     */
    public ItemStack getItemStatusSmash(Player player) {
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Smash-Bonus");

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.CRYSTALS_SMASH);
        String multiplier = "" + Multipliers.getMultiplier("smash", level, true);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current level: " + ChatColor.GREEN + "Lvl " + level);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Chance double crystals: " + ChatColor.GREEN +
                multiplier.replace(".0", "") + "%");
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Increase the chance of");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "doubling the crystals you");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "get when you smash a crystal.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing Crystal-XP status
     */
    public ItemStack getItemStatusExp(Player player) {
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Xp-Bonus");

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.CRYSTALS_XP);
        String multiplier = "" + Multipliers.getMultiplier("xp", level, false);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current level: " + ChatColor.GREEN + "Lvl " + level);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Crystals per XP-Level: " + ChatColor.GREEN +
                multiplier.replace(".0", ""));
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Increase the amount of crystals");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "you get when you reach 1 LVL.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item linking to the Main Menu
     */
    public ItemStack getItemMainMenu() {
        ItemStack is = new ItemStack(Material.ARROW, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Main Menu");
        is.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Go back to the Main Menu!");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }
}