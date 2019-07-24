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
public class ShopPowerup implements Listener {

    private static final String PREFIX = "" + ChatColor.GREEN + ChatColor.BOLD + ChatColor.ITALIC;
    private static final String PREFIX_RED = "" + ChatColor.RED + ChatColor.BOLD + ChatColor.ITALIC;

    private Economy economy;

    public ShopPowerup(Economy economy) {
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

        //STATUS BAR
        contents[8] = getItemStatusBuff(player);
        contents[17] = getItemStatusDebuff(player);
        contents[26] = getItemStatusExplosives(player);
        contents[35] = getItemStatusAmmo(player);
        contents[44] = getItemStatusCreeper(player);
        contents[53] = getItemStatusWolf(player);

        //ITEMS TO BUY
        contents[10] = getItemBuyBuff(player);
        contents[12] = getItemBuyDebuff(player);
        contents[14] = getItemBuyExplosive(player);
        contents[28] = getItemBuyAmmo(player);
        contents[30] = getItemBuyCreeper(player);
        contents[32] = getItemBuyWolf(player);

        //NAVIGATION
        contents[45] = getItemMainMenu();
        contents[49] = economy.getItemBalance(player);

        inventory.setContents(contents);
    }

    /**
     * Shows the powerup menu of the CrystalQuest-Shop.
     *
     * @param player
     *         The player to show the menu to.
     */
    public void showMenu(Player player) {
        player.closeInventory();

        Inventory inventory = Bukkit.createInventory(null, 54, getMenuTitle());

        updateMenu(player, inventory);
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

        if (!event.isLeftClick()) {
            return;
        }

        if (event.getCurrentItem() == null) {
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

        String upgradeItemName = meta.getDisplayName();
        Player player = (Player)event.getWhoClicked();

        // Main menu.
        if (upgradeItemName.equalsIgnoreCase(ChatColor.GREEN + Broadcast.get("menu.main"))) {
            economy.getMainMenu().showMenu(player);
            event.setCancelled(true);
            return;
        }
        // Buy buff.
        else if (upgradeItemName.equalsIgnoreCase(PREFIX + getBuyString() + " " + ChatColor.LIGHT_PURPLE + Broadcast.get("shop.buffs"))) {
            buyClass(player, ShopUpgrade.POWERUP_BUFF, event.getInventory());
        }
        // Buy debuff.
        else if (upgradeItemName.equalsIgnoreCase(PREFIX + getBuyString() + " " + ChatColor.RED + Broadcast.get("shop.debuffs"))) {
            buyClass(player, ShopUpgrade.POWERUP_DEBUFF, event.getInventory());
        }
        // Buy explosive.
        else if (upgradeItemName.equalsIgnoreCase(PREFIX + getBuyString() + " " + ChatColor.YELLOW + Broadcast.get("shop.explosives"))) {
            buyClass(player, ShopUpgrade.POWERUP_EXPLOSIVE, event.getInventory());
        }
        // Buy ammo.
        else if (upgradeItemName.equalsIgnoreCase(PREFIX + getBuyString() + " " + ChatColor.AQUA + Broadcast.get("shop.weaponry"))) {
            buyClass(player, ShopUpgrade.POWERUP_WEAPONRY, event.getInventory());
        }
        // Buy creepers.
        else if (upgradeItemName.equalsIgnoreCase(PREFIX + getBuyString() + " " + ChatColor.DARK_GREEN + Broadcast.get("shop.creepers"))) {
            buyClass(player, ShopUpgrade.POWERUP_CREEPERS, event.getInventory());
        }
        // Buy wolves.
        else if (upgradeItemName.equalsIgnoreCase(PREFIX + getBuyString() + " " + ChatColor.RESET + Broadcast.get("shop.wolfie") + " " + ChatColor.RED + "♥")) {
            buyClass(player, ShopUpgrade.POWERUP_WOLF, event.getInventory());
        }

        event.setCancelled(true);
    }

    /**
     * Let the player buy the chosen class.
     *
     * @param player
     *         The buyer.
     * @param upgrade
     *         The item-class the player buys.
     * @param inventory
     *         The inventory-instance of the shop.
     * @return True if able to, false if he/she couldn't buy the class.
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
     * Gets the item showing CREEPER buyable item
     */
    public ItemStack getItemBuyCreeper(Player player) {
        ItemStack item = new ItemStack(Material.CREEPER_SPAWN_EGG, 1);
        ItemMeta meta = item.getItemMeta();

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_CREEPERS);
        if (level < 5) {
            meta.setDisplayName(PREFIX + getBuyString() + " " + ChatColor.DARK_GREEN + Broadcast.get("shop.creepers"));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.upgrade-to") + " " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + (level + 1));
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.bonus-chance") + ": " + ChatColor.GREEN + "+12.5%");
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.extra-gems-drop") + ": " + ChatColor.GREEN + "+1");
            lore.add("");
            lore.add(ChatColor.RED + Broadcast.get("shop.price") + ": " + ChatColor.GOLD + economy.getUpgradeCosts(level + 1));
            meta.setLore(lore);
        }
        else {
            meta.setDisplayName(PREFIX_RED + getMaxString() + " " + ChatColor.DARK_GREEN + Broadcast.get("shop.creepers"));
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing WOLF buyable item
     */
    public ItemStack getItemBuyWolf(Player player) {
        ItemStack item = new ItemStack(Material.BONE, 1);
        ItemMeta meta = item.getItemMeta();

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_WOLF);
        if (level < 5) {
            meta.setDisplayName(PREFIX + getBuyString() + " " + ChatColor.RESET + Broadcast.get("shop.wolfie") + " " + ChatColor.RED + "♥");

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.upgrade-to") + " " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + (level + 1));

            if (level + 1 == 1 || level + 1 == 2 || level + 1 == 4) {
                lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.resistance-level") + ": " + ChatColor.GREEN + "+1");
            }
            else {
                lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.strength-level") + ": " + ChatColor.GREEN + "+1");
            }

            lore.add("");
            lore.add(ChatColor.RED + Broadcast.get("shop.price") + ": " + ChatColor.GOLD + economy.getUpgradeCosts(level + 1));
            meta.setLore(lore);
        }
        else {
            meta.setDisplayName(PREFIX_RED + getMaxString() + " " + ChatColor.RESET + Broadcast.get("shop.wolfie") + " " + ChatColor.RED + "♥");
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing AMMO buyable item
     */
    public ItemStack getItemBuyAmmo(Player player) {
        ItemStack item = new ItemStack(Material.DIAMOND_AXE, 1);
        Items.hideAllFlags(item);
        ItemMeta meta = item.getItemMeta();

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_WEAPONRY);
        if (level < 5) {
            meta.setDisplayName(PREFIX + getBuyString() + " " + ChatColor.AQUA + Broadcast.get("shop.weaponry"));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.upgrade-to") + " " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + (level + 1));
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.bonus-chance") + ": " + ChatColor.GREEN + "+10%");
            lore.add("");
            lore.add(ChatColor.RED + Broadcast.get("shop.price") + ": " + ChatColor.GOLD + economy.getUpgradeCosts(level + 1));
            meta.setLore(lore);
        }
        else {
            meta.setDisplayName(PREFIX_RED + getMaxString() + " " + ChatColor.AQUA + Broadcast.get("shop.weaponry"));
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing EXPLOSIVE buyable item
     */
    public ItemStack getItemBuyExplosive(Player player) {
        ItemStack item = new ItemStack(Material.EGG, 1);
        ItemMeta meta = item.getItemMeta();

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_EXPLOSIVE);
        if (level < 5) {
            meta.setDisplayName(PREFIX + getBuyString() + " " + ChatColor.YELLOW + Broadcast.get("shop.explosives"));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.upgrade-to") + " " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + (level + 1));
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.bonus-size") + ": " + ChatColor.GREEN + "+10%");
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.lightning-explosion") + ": " + ChatColor.GREEN + "+0.5");
            lore.add("");
            lore.add(ChatColor.RED + Broadcast.get("shop.price") + ": " + ChatColor.GOLD + economy.getUpgradeCosts(level + 1));
            meta.setLore(lore);
        }
        else {
            meta.setDisplayName(PREFIX_RED + getMaxString() + " " + ChatColor.YELLOW + Broadcast.get("shop.explosives"));
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing DEBUFF buyable item
     */
    public ItemStack getItemBuyDebuff(Player player) {
        ItemStack item = new ItemStack(Material.WITHER_SKELETON_SKULL, 1);
        ItemMeta meta = item.getItemMeta();

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_DEBUFF);
        if (level < 5) {
            meta.setDisplayName(PREFIX + getBuyString() + " " + ChatColor.RED + Broadcast.get("shop.debuffs"));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.upgrade-to") + " " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + (level + 1));
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.bonus-length") + ": " + ChatColor.GREEN + "+10%");
            lore.add("");
            lore.add(ChatColor.RED + Broadcast.get("shop.price") + ": " + ChatColor.GOLD + economy.getUpgradeCosts(level + 1));
            meta.setLore(lore);
        }
        else {
            meta.setDisplayName(PREFIX_RED + getMaxString() + " " + ChatColor.RED + Broadcast.get("shop.debuffs"));
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing BUFF buyable item
     */
    public ItemStack getItemBuyBuff(Player player) {
        ItemStack item = new ItemStack(Material.GLISTERING_MELON_SLICE, 1);
        ItemMeta meta = item.getItemMeta();

        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_BUFF);
        if (level < 5) {
            meta.setDisplayName(PREFIX + getBuyString() + " " + ChatColor.LIGHT_PURPLE + Broadcast.get("shop.buffs"));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.upgrade-to") + " " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + (level + 1));
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.bonus-length") + ": " + ChatColor.GREEN + "+10%");
            lore.add("");
            lore.add(ChatColor.RED + Broadcast.get("shop.price") + ": " + ChatColor.GOLD + economy.getUpgradeCosts(level + 1));
            meta.setLore(lore);
        }
        else {
            meta.setDisplayName(PREFIX_RED + getMaxString() + " " + ChatColor.LIGHT_PURPLE + Broadcast.get("shop.buffs"));
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing WOLF status
     */
    public ItemStack getItemStatusWolf(Player player) {
        ItemStack item = new ItemStack(Material.BONE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + Broadcast.get("shop.wolfie") + " " + ChatColor.RED + "♥");
        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_WOLF);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.current-level") + ": " +
                ChatColor.GREEN + Broadcast.get("shop.level") + " " + level);
        String multiplier = "" + Multipliers.getMultiplier("wolfstrength", level, false);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.strength-level") + ": " +
                ChatColor.GREEN + multiplier.replace(".0", ""));
        multiplier = "" + Multipliers.getMultiplier("wolfresistance", level, false);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.resistance-level") + ": " +
                ChatColor.GREEN + multiplier.replace(".0", ""));
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.wolfie-lore-1"));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.wolfie-lore-2"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing CREEPER status
     */
    public ItemStack getItemStatusCreeper(Player player) {
        ItemStack item = new ItemStack(Material.CREEPER_SPAWN_EGG, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + Broadcast.get("shop.creepers"));
        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_CREEPERS);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.current-level") + ": " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + level);
        String multiplier = "" + Multipliers.getMultiplier("creeper", level, true);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.chance-charged-creeper") + ": " + ChatColor.GREEN +
                multiplier.replace(".0", "") + "%");
        multiplier = "" + Multipliers.getMultiplier("creepergem", level, false);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.gems-on-death") + ": " + ChatColor.GREEN +
                multiplier.replace(".0", ""));
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.creeper-lore-1"));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.creeper-lore-2"));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.creeper-lore-3"));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.creeper-lore-4"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing AMMO status
     */
    public ItemStack getItemStatusAmmo(Player player) {
        ItemStack item = new ItemStack(Material.DIAMOND_AXE, 1);
        Items.hideAllFlags(item);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + Broadcast.get("shop.weaponry"));
        List<String> lore = new ArrayList<>();
        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_WEAPONRY);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.current-level") + ": " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + level);
        String multiplier = "" + Multipliers.getMultiplier("ammo", level, true);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.double-chance") + ": " + ChatColor.GREEN +
                multiplier.substring(0, 2) + "%");
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.weaponry-lore-1"));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.weaponry-lore-2"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing EXPLOSIVES status
     */
    public ItemStack getItemStatusExplosives(Player player) {
        ItemStack item = new ItemStack(Material.EGG, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + Broadcast.get("shop.explosives"));
        List<String> lore = new ArrayList<>();
        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_EXPLOSIVE);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.current-level") + ": " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + level);
        String multiplier = "" + Multipliers.getMultiplier("explosive", level, true);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.explosion-size") + ": " + ChatColor.GREEN +
                multiplier.substring(0, 3) + "%");
        multiplier = "" + Multipliers.getMultiplier("lightning", level, false);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.lightning-explosion-power") + ": " + ChatColor.GREEN + multiplier);
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.explosives-lore-1"));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.explosives-lore-2"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing DEBUFF status
     */
    public ItemStack getItemStatusDebuff(Player player) {
        ItemStack item = new ItemStack(Material.WITHER_SKELETON_SKULL, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + Broadcast.get("shop.debuffs"));
        List<String> lore = new ArrayList<>();
        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_DEBUFF);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.current-level") + ": " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + level);
        String multiplier = "" + Multipliers.getMultiplier("debuff", level, true);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.debuff-length") + ": " + ChatColor.GREEN +
                multiplier.substring(0, 3) + "%");
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.debuff-lore-1"));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.debuff-lore-2"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item showing BUFF status
     */
    public ItemStack getItemStatusBuff(Player player) {
        ItemStack item = new ItemStack(Material.GLISTERING_MELON_SLICE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + Broadcast.get("shop.buffs"));
        List<String> lore = new ArrayList<>();
        int level = economy.getUpgrades().getLevel(player, ShopUpgrade.POWERUP_BUFF);
        String multiplier = "" + Multipliers.getMultiplier("buff", level, true);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.current-level") + ": " + ChatColor.GREEN + Broadcast.get("shop.level") + " " + level);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.buff-length") + ": " + ChatColor.GREEN +
                multiplier.substring(0, 3) + "%");
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.buff-length-lore-1"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item linking to the Main Menu
     */
    public ItemStack getItemMainMenu() {
        ItemStack item = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + Broadcast.get("menu.main"));
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.main-menu"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private String getMenuTitle() {
        return Broadcast.get("shop.shop") + ":" + ChatColor.GOLD + " " + Broadcast.get("shop.powerups");
    }

    private String getBuyString() {
        return Broadcast.get("shop.buy").toUpperCase();
    }

    private String getMaxString() {
        return Broadcast.get("shop.max").toUpperCase();
    }
}