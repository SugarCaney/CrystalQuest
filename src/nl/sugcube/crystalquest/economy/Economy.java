package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.ArenaManager;
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

    public static CrystalQuest plugin;

    public static int COST_LVL_1 = 518;
    public static int COST_LVL_2 = 1090;
    public static int COST_LVL_3 = 2376;
    public static int COST_LVL_4 = 5180;
    public static int COST_LVL_5 = 11292;

    private Balance balance;
    private ShopMainMenu shopMainMenu;
    private ShopPowerup shopPowerupMenu;
    private ShopCrystals shopCrystals;
    private ShopClasses shopClasses;

    public Economy(CrystalQuest instance) {
        new Multipliers(plugin);
        plugin = instance;
        shopMainMenu = new ShopMainMenu(plugin, this);
        shopPowerupMenu = new ShopPowerup(plugin, this);
        shopCrystals = new ShopCrystals(plugin, this);
        shopClasses = new ShopClasses(plugin, this);

        if (instance.getConfig().getBoolean("mysql.enabled")) {
            balance = new DatabaseBalance(plugin.queryEconomy);
        }
        else {
            balance = new YamlBalance(plugin);
        }

        COST_LVL_1 = plugin.getConfig().getInt("shop.start-price");
        COST_LVL_2 = (int)(COST_LVL_1 * plugin.getConfig().getDouble("shop.multiplier"));
        COST_LVL_3 = (int)(COST_LVL_2 * plugin.getConfig().getDouble("shop.multiplier"));
        COST_LVL_4 = (int)(COST_LVL_3 * plugin.getConfig().getDouble("shop.multiplier"));
        COST_LVL_5 = (int)(COST_LVL_4 * plugin.getConfig().getDouble("shop.multiplier"));
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
        ItemStack is = new ItemStack(Material.EMERALD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Crystals: " + ChatColor.GOLD + ChatColor.BOLD + getBalance().getBalance(player, false));
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
     *         (int) The level to upgrade to.
     */
    public int getCosts(int lvl) {
        switch (lvl) {
            case 1:
                return COST_LVL_1;
            case 2:
                return COST_LVL_2;
            case 3:
                return COST_LVL_3;
            case 4:
                return COST_LVL_4;
            case 5:
                return COST_LVL_5;
            default:
                return 0;
        }
    }

    /**
     * Gets the level a player has for a certain upgrade.
     *
     * @param p
     *         (Player) The player to check for.
     * @param upgrade
     *         (String) The upgrade's name.
     * @return (int) The level of the player for a certain upgrade.
     */
    public int getLevel(Player p, String upgrade, String type) {
        String node = "shop." + type + "." + p.getUniqueId().toString() + "." + upgrade;
        if (plugin.getData().isSet(node)) {
            return plugin.getData().getInt(node);
        }
        else {
            plugin.getData().set(node, 0);
            return 0;
        }
    }

    /**
     * Gets the message to be sent when a player earns crystals
     *
     * @param p
     *         (Player) The targeted player.
     * @param amount
     *         (int) The amount of crystals to add.
     * @return (String) The message to be displayed to the player. OR null when there is no message
     * set.
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
