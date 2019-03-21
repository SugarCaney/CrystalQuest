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
public class ShopPowerup implements Listener {

    public static CrystalQuest plugin;
    public static Economy economy;

    public ShopPowerup(CrystalQuest instance, Economy eco) {
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

        //STATUS BAR
        contents[8] = getItemStatusBuff(p);
        contents[17] = getItemStatusDebuff(p);
        contents[26] = getItemStatusExplosives(p);
        contents[35] = getItemStatusAmmo(p);
        contents[44] = getItemStatusCreeper(p);
        contents[53] = getItemStatusWolf(p);

        //ITEMS TO BUY
        contents[10] = getItemBuyBuff(p);
        contents[12] = getItemBuyDebuff(p);
        contents[14] = getItemBuyExplosive(p);
        contents[28] = getItemBuyAmmo(p);
        contents[30] = getItemBuyCreeper(p);
        contents[32] = getItemBuyWolf(p);

        //NAVIGATION
        contents[45] = getItemMainMenu();
        contents[49] = economy.getItemBalance(p);

        inv.setContents(contents);
    }

    /**
     * Shows the powerup menu of the CrystalQuest-Shop.
     *
     * @param p
     *         (Player) The player to show the menu to.
     */
    public void showMenu(Player p) {
        p.closeInventory();

        Inventory inv = Bukkit.createInventory(null, 54,
                ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Powerups"
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
        if (inv.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:" + ChatColor.GOLD + " Powerups")) {

            if (e.isLeftClick()) {
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
                                economy.getMainMenu().showMenu((Player)e.getWhoClicked());
                                e.setCancelled(true);
                                return;
                            }
							/*
							 * BUY BUFF
							 */
                            else if (name.equalsIgnoreCase(ChatColor.GREEN + "[Buy] " + ChatColor.LIGHT_PURPLE + "Buffs")) {
                                buyClass(p, "buff", e.getInventory());
                            }
							/*
							 * BUY DEBUFF
							 */
                            else if (name.equalsIgnoreCase(ChatColor.GREEN + "[Buy] " + ChatColor.RED + "Debuffs")) {
                                buyClass(p, "debuff", e.getInventory());
                            }
							/*
							 * BUY EXPLOSIVE
							 */
                            else if (name.equalsIgnoreCase(ChatColor.GREEN + "[Buy] " + ChatColor.YELLOW + "Explosives")) {
                                buyClass(p, "explosive", e.getInventory());
                            }
							/*
							 * BUY AMMO
							 */
                            else if (name.equalsIgnoreCase(ChatColor.GREEN + "[Buy] " + ChatColor.AQUA + "Weaponry")) {
                                buyClass(p, "weaponry", e.getInventory());
                            }
							/*
							 * BUY CREEPERS
							 */
                            else if (name.equalsIgnoreCase(ChatColor.GREEN + "[Buy] " + ChatColor.DARK_GREEN + "Creepers")) {
                                buyClass(p, "creepers", e.getInventory());
                            }
							/*
							 * BUY WOLF
							 */
                            else if (name.equalsIgnoreCase(ChatColor.GREEN + "[Buy] " + ChatColor.RESET + "Wolfie " + ChatColor.RED + "♥")) {
                                buyClass(p, "wolf", e.getInventory());
                            }

                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Let the player buy the chosen class.
     *
     * @param p
     *         (Player) The buyer.
     * @param Class
     *         (String) The item-class the player buys.
     * @param inv
     *         (Inventory) The inventory-instance of the shop.
     * @return (boolean) True if able to, false if he/she couldn't buy the class.
     */
    public boolean buyClass(Player p, String Class, Inventory inv) {
        int level = economy.getLevel(p, Class, "upgrade") + 1;
        Balance bal = economy.getBalance();

        if (bal.canAfford(p, economy.getCosts(level))) {
            bal.addCrystals(p, -economy.getCosts(level), false);
            updateMenu(p, inv);
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + "." + Class, level);
            showMenu(p);
            return true;
        }

        return false;
    }

    /**
     * Gets the item showing CREEPER buyable item
     *
     * @return (ItemStack)
     */
    public ItemStack getItemBuyCreeper(Player p) {
        ItemStack is = new ItemStack(Material.CREEPER_SPAWN_EGG, 1);
        ItemMeta im = is.getItemMeta();
        if (economy.getLevel(p, "creepers", "upgrade") < 5) {
            im.setDisplayName(ChatColor.GREEN + "[Buy] " + ChatColor.DARK_GREEN + "Creepers");
        }
        else {
            im.setDisplayName(ChatColor.RED + "[MAX] " + ChatColor.DARK_GREEN + "Creepers");
        }
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".creepers")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".creepers");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".creepers", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade to: " + ChatColor.GREEN + "Lvl " + (level + 1));

        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Bonus chance: " + ChatColor.GREEN + "+12.5%");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Extra gems-drop: " + ChatColor.GREEN + "+1");
        lore.add("");
        lore.add(ChatColor.RED + "Price: " + ChatColor.GOLD + economy.getCosts(level + 1));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing WOLF buyable item
     *
     * @return (ItemStack)
     */
    public ItemStack getItemBuyWolf(Player p) {
        ItemStack is = new ItemStack(Material.BONE, 1);
        ItemMeta im = is.getItemMeta();
        if (economy.getLevel(p, "wolf", "upgrade") < 5) {
            im.setDisplayName(ChatColor.GREEN + "[Buy] " + ChatColor.RESET + "Wolfie " + ChatColor.RED + "♥");
        }
        else {
            im.setDisplayName(ChatColor.RED + "[MAX] " + ChatColor.RESET + "Wolfie " + ChatColor.RED + "♥");
        }
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".wolf")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".wolf");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".wolf", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade to: " + ChatColor.GREEN + "Lvl " + (level + 1));

        if (level + 1 == 1 || level + 1 == 2 || level + 1 == 4) {
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Resistance level: " + ChatColor.GREEN + "+1");
        }
        else {
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Strength level: " + ChatColor.GREEN + "+1");
        }

        lore.add("");
        lore.add(ChatColor.RED + "Price: " + ChatColor.GOLD + economy.getCosts(level + 1));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing AMMO buyable item
     *
     * @return (ItemStack)
     */
    public ItemStack getItemBuyAmmo(Player p) {
        ItemStack is = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta im = is.getItemMeta();
        if (economy.getLevel(p, "weaponry", "upgrade") < 5) {
            im.setDisplayName(ChatColor.GREEN + "[Buy] " + ChatColor.AQUA + "Weaponry");
        }
        else {
            im.setDisplayName(ChatColor.RED + "[MAX] " + ChatColor.AQUA + "Weaponry");
        }
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".weaponry")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".weaponry");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".weaponry", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade to: " + ChatColor.GREEN + "Lvl " + (level + 1));

        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Bonus chance: " + ChatColor.GREEN + "+10%");
        lore.add("");
        lore.add(ChatColor.RED + "Price: " + ChatColor.GOLD + economy.getCosts(level + 1));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing EXPLOSIVE buyable item
     *
     * @return (ItemStack)
     */
    public ItemStack getItemBuyExplosive(Player p) {
        ItemStack is = new ItemStack(Material.EGG, 1);
        ItemMeta im = is.getItemMeta();
        if (economy.getLevel(p, "explosive", "upgrade") < 5) {
            im.setDisplayName(ChatColor.GREEN + "[Buy] " + ChatColor.YELLOW + "Explosives");
        }
        else {
            im.setDisplayName(ChatColor.RED + "[MAX] " + ChatColor.YELLOW + "Explosives");
        }
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".explosive")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".explosive");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".explosive", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade to: " + ChatColor.GREEN + "Lvl " + (level + 1));

        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Bonus size: " + ChatColor.GREEN + "+10%");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Lightning explosion: " + ChatColor.GREEN + "+0.5");
        lore.add("");
        lore.add(ChatColor.RED + "Price: " + ChatColor.GOLD + economy.getCosts(level + 1));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing DEBUFF buyable item
     *
     * @return (ItemStack)
     */
    public ItemStack getItemBuyDebuff(Player p) {
        ItemStack is = new ItemStack(Material.WITHER_SKELETON_SKULL, 1);
        ItemMeta im = is.getItemMeta();
        if (economy.getLevel(p, "debuff", "upgrade") < 5) {
            im.setDisplayName(ChatColor.GREEN + "[Buy] " + ChatColor.RED + "Debuffs");
        }
        else {
            im.setDisplayName(ChatColor.RED + "[MAX] " + ChatColor.RED + "Debuffs");
        }
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".debuff")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".debuff");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".debuff", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade to: " + ChatColor.GREEN + "Lvl " + (level + 1));

        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Bonus length: " + ChatColor.GREEN + "+10%");
        lore.add("");
        lore.add(ChatColor.RED + "Price: " + ChatColor.GOLD + economy.getCosts(level + 1));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing BUFF buyable item
     *
     * @return (ItemStack)
     */
    public ItemStack getItemBuyBuff(Player p) {
        ItemStack is = new ItemStack(Material.GLISTERING_MELON_SLICE, 1);
        ItemMeta im = is.getItemMeta();
        if (economy.getLevel(p, "buff", "upgrade") < 5) {
            im.setDisplayName(ChatColor.GREEN + "[Buy] " + ChatColor.LIGHT_PURPLE + "Buffs");
        }
        else {
            im.setDisplayName(ChatColor.RED + "[MAX] " + ChatColor.LIGHT_PURPLE + "Buffs");
        }
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".buff")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".buff");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".buff", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Upgrade to: " + ChatColor.GREEN + "Lvl " + (level + 1));

        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Bonus length: " + ChatColor.GREEN + "+10%");
        lore.add("");
        lore.add(ChatColor.RED + "Price: " + ChatColor.GOLD + economy.getCosts(level + 1));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing WOLF status
     *
     * @return (ItemStack)
     */
    public ItemStack getItemStatusWolf(Player p) {
        ItemStack is = new ItemStack(Material.BONE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.RESET + "Wolfie " + ChatColor.RED + "♥");
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".wolf")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".wolf");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".wolf", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current level: " + ChatColor.GREEN + "Lvl " + level);
        String multiplier = "" + Multipliers.getMultiplier("wolfstrength", level, false);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Strength level: " + ChatColor.GREEN +
                multiplier.replace(".0", ""));
        multiplier = "" + Multipliers.getMultiplier("wolfresistance", level, false);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Resistance level: " + ChatColor.GREEN +
                multiplier.replace(".0", ""));
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Buff your wolf-army with strength");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "and resistance!");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing CREEPER status
     *
     * @return (ItemStack)
     */
    public ItemStack getItemStatusCreeper(Player p) {
        ItemStack is = new ItemStack(Material.CREEPER_SPAWN_EGG, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.DARK_GREEN + "Creepers");
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".creepers")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".creepers");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".creepers", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current level: " + ChatColor.GREEN + "Lvl " + level);
        String multiplier = "" + Multipliers.getMultiplier("creeper", level, true);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Chance Charged Creeper: " + ChatColor.GREEN +
                multiplier.replace(".0", "") + "%");
        multiplier = "" + Multipliers.getMultiplier("creepergem", level, false);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Gems on death: " + ChatColor.GREEN +
                multiplier.replace(".0", ""));
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Increase the chance of getting");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "a charged creeper. Also increases");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "the amount of crystals a creeper");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "drops on death.");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing AMMO status
     *
     * @return (ItemStack)
     */
    public ItemStack getItemStatusAmmo(Player p) {
        ItemStack is = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.AQUA + "Weaponry");
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".weaponry")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".weaponry");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".weaponry", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current level: " + ChatColor.GREEN + "Lvl " + level);
        String multiplier = "" + Multipliers.getMultiplier("ammo", level, true);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Chance to double: " + ChatColor.GREEN +
                multiplier.substring(0, 2) + "%");
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Increase the chance of getting");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "double durability/ammo on weapons.");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing EXPLOSIVES status
     *
     * @return (ItemStack)
     */
    public ItemStack getItemStatusExplosives(Player p) {
        ItemStack is = new ItemStack(Material.EGG, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "Explosives");
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".explosive")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".explosive");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".explosive", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current level: " + ChatColor.GREEN + "Lvl " + level);
        String multiplier = "" + Multipliers.getMultiplier("explosive", level, true);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Explosion size: " + ChatColor.GREEN +
                multiplier.substring(0, 3) + "%");
        multiplier = "" + Multipliers.getMultiplier("lightning", level, false);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Lightning explosion power: " + ChatColor.GREEN + multiplier);
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Increase the size of your explosives");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "and let lightning explode when hit.");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing DEBUFF status
     *
     * @return (ItemStack)
     */
    public ItemStack getItemStatusDebuff(Player p) {
        ItemStack is = new ItemStack(Material.WITHER_SKELETON_SKULL, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.RED + "Debuffs");
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".debuff")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".debuff");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".debuff", 0);
        }
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current level: " + ChatColor.GREEN + "Lvl " + level);
        String multiplier = "" + Multipliers.getMultiplier("debuff", level, true);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Debuff length: " + ChatColor.GREEN +
                multiplier.substring(0, 3) + "%");
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Let the debuffs apply");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "longer to your oppontents.");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the item showing BUFF status
     *
     * @return (ItemStack)
     */
    public ItemStack getItemStatusBuff(Player p) {
        ItemStack is = new ItemStack(Material.GLISTERING_MELON_SLICE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.LIGHT_PURPLE + "Buffs");
        List<String> lore = new ArrayList<>();
        int level = 0;
        if (plugin.getData().isSet("shop.upgrade." + p.getUniqueId().toString() + ".buff")) {
            level = plugin.getData().getInt("shop.upgrade." + p.getUniqueId().toString() + ".buff");
        }
        else {
            plugin.getData().set("shop.upgrade." + p.getUniqueId().toString() + ".buff", 0);
        }
        String multiplier = "" + Multipliers.getMultiplier("buff", level, true);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Current level: " + ChatColor.GREEN + "Lvl " + level);
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Buff length: " + ChatColor.GREEN +
                multiplier.substring(0, 3) + "%");
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Let your buff-powerups last longer.");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
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
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Go back to the Main Menu!");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }
}