package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.ArenaManager;
import nl.sugcube.crystalquest.util.CrystalQuestException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SugarCaney
 */
public class Economy {

    private CrystalQuest plugin;

    /**
     * How many crystals each upgrade level costs. Index i means upgrade to level i+1.
     */
    private int[] UPGRADE_COSTS = new int[5];

    private Balance balance;
    private Upgrades upgrades;
    private Classes classes;
    private ShopMainMenu shopMainMenu;
    private ShopPowerup shopPowerupMenu;
    private ShopCrystals shopCrystals;
    private ShopClasses shopClasses;

    public Economy(CrystalQuest instance) {
        new Multipliers(plugin);
        plugin = instance;
        shopMainMenu = new ShopMainMenu(this);
        shopPowerupMenu = new ShopPowerup(this);
        shopCrystals = new ShopCrystals(this);
        shopClasses = new ShopClasses(plugin, this);

        if (instance.getConfig().getBoolean("mysql.enabled")) {
            balance = new DatabaseBalance(plugin.queryEconomy);
            upgrades = new DatabaseUpgrades(plugin.queryEconomy);
            classes = new DatabaseClasses(plugin.queryEconomy);
        }
        else {
            balance = new YamlBalance(plugin);
            upgrades = new YamlUpgrades(plugin);
            classes = new YamlClasses(plugin);
        }

        UPGRADE_COSTS[0] = plugin.getConfig().getInt("shop.start-price");
        double multiplier = plugin.getConfig().getDouble("shop.multiplier");
        for (int i = 1; i < 5; i++) {
            UPGRADE_COSTS[i] = (int)(UPGRADE_COSTS[i - 1] * multiplier);
        }
    }

    /**
     * Registers all events in the menus.
     *
     * @param pluginManager
     *         The plugin manager of the plugin.
     */
    public void registerEvents(PluginManager pluginManager) {
        pluginManager.registerEvents(shopMainMenu, plugin);
        pluginManager.registerEvents(shopPowerupMenu, plugin);
        pluginManager.registerEvents(shopCrystals, plugin);
        pluginManager.registerEvents(shopClasses, plugin);
    }

    /**
     * Gets the instance managing the balances.
     */
    public Balance getBalance() {
        return this.balance;
    }

    /**
     * Gets the instance managing the upgrades.
     */
    public Upgrades getUpgrades() {
        return this.upgrades;
    }

    /**
     * Gets the instance managing bought classes.
     */
    public Classes getClasses() {
        return classes;
    }

    /**
     * Gets the class menu of the shop.
     */
    public ShopClasses getClassesMenu() {
        return this.shopClasses;
    }

    /**
     * Gets the powerup menu of the shop.
     */
    public ShopPowerup getPowerupMenu() {
        return this.shopPowerupMenu;
    }

    /**
     * Gets the crystal-section of the shop.
     */
    public ShopCrystals getCrystalMenu() {
        return this.shopCrystals;
    }

    /**
     * Gets the main menu of the shop.
     */
    public ShopMainMenu getMainMenu() {
        return this.shopMainMenu;
    }

    /**
     * Gets the item representing your balance.
     *
     * @param player
     *         The player to show the menu to.
     */
    public ItemStack getItemBalance(Player player) {
        String balanceString = Broadcast.get("shop.balance-crystal");
        int balance = getBalance().getBalance(player, false);

        ItemStack is = new ItemStack(Material.EMERALD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(balanceString.replace("%amount%", Integer.toString(balance)));
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.spend-lore1"));
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + Broadcast.get("shop.spend-lore2"));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets how many crystals the upgrade will cost
     *
     * @param lvl
     *         The level to upgrade to.
     */
    public int getUpgradeCosts(int lvl) {
        if (lvl < 1 || lvl > 5) {
            return 0;
        }
        return UPGRADE_COSTS[lvl - 1];
    }

    /**
     * Gets the level a player has for a certain upgrade.
     *
     * @param player
     *         The player to check for.
     * @param upgrade
     *         The upgrade's name.
     * @return The level of the player for a certain upgrade.
     */
    public int getLevel(Player player, String upgrade, String type) {
        // MySQL database.
        if (plugin.getConfig().getBoolean("mysql.enabled")) {
            ShopUpgrade shopUpgrade = ShopUpgrade.getById(upgrade);
            if (shopUpgrade == null) {
                throw new CrystalQuestException("Unknown upgrade " + upgrade);
            }
            return plugin.queryEconomy.getUpgradeLevel(player.getUniqueId(), shopUpgrade);
        }
        // Yml configuration lookup
        else {
            String node = "shop." + type + "." + player.getUniqueId().toString() + "." + upgrade;
            if (plugin.getData().isSet(node)) {
                return plugin.getData().getInt(node);
            }
            else {
                plugin.getData().set(node, 0);
                return 0;
            }
        }
    }

    /**
     * Gets the message to be sent when a player earns crystals
     *
     * @param p
     *         The targeted player.
     * @param amount
     *         The amount of crystals to add.
     * @return The message to be displayed to the player. OR null when there is no message set.
     */
    public String getCoinMessage(Player p, int amount) {
        if (plugin.getConfig().isSet("shop.crystal-message")) {
            String template = plugin.getConfig().getString("shop.crystal-message");
            if (template != "") {
                String s = template.replace("%amount%", amount + "");
                String finals = "";
                ArenaManager am = plugin.getArenaManager();
                if (am.isInGame(p)) {
                    finals = s.replace("%teamcolour%", am.getTeam(p).getChatColour() + "");
                }
                else {
                    finals = s.replace("%teamcolour%", "");
                }
                return finals;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }
}
