package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class Balance {

    public static CrystalQuest plugin;
    public static Economy economy;

    public Balance(CrystalQuest instance, Economy eco) {
        plugin = instance;
        economy = eco;
    }

    /**
     * Sets the balance of a certain player.
     *
     * @param p
     *         (Player) The player you want to set the balance for.
     * @param newBalance
     *         (int) The new balance (greather than zero).
     */
    public void setBalance(Player p, int newBalance) {
        if (newBalance < 0) {
            newBalance = 0;
        }

        plugin.getData().set("shop.balance." + p.getUniqueId().toString(), newBalance);
        plugin.saveData();
        //plugin.getData().set("economy.balance." + p.getName(), newBalance);
        //plugin.saveData();
    }

    /**
     * Adds or removes an amount of crystals to/from someone's balance.
     *
     * @param p
     *         (Player) The player to edit the balance.
     * @param amount
     *         (int) The amount of crystals to add (negative number to remove).
     * @param showMessage
     *         (boolean) When true, a notification will be sent to the player.
     */
    public void addCrystals(Player p, int amount, boolean showMessage) {
        int balance = plugin.getData().getInt("shop.balance." + p.getUniqueId().toString());
        //int balance = plugin.getData().getInt("economy.balance." + p.getName());
        balance += amount;
        plugin.getData().set("shop.balance." + p.getUniqueId().toString(), balance);
        //plugin.getData().set("economy.balance." + p.getName(), balance);
        if (showMessage) {
            p.sendMessage(Broadcast.TAG + ChatColor.GRAY + amount + " Crystals " + ChatColor.YELLOW + "have been" +
                    "added to your account!");
        }
        plugin.saveData();
    }

    /**
     * Checks if the player can afford something.
     *
     * @param p
     *         (Player) The player you want to check for.
     * @param amount
     *         (int) The amount of money to spend.
     * @return (boolean) True if so, false if not.
     */
    public boolean canAfford(Player p, int amount) {
        return getBalance(p, false) >= amount;
    }

    /**
     * Gets the amount of crystals of the player.
     *
     * @param p
     *         (Player) The player you want to check for.
     * @return (int) The amount of crystals a player got.
     */
    public int getBalance(Player p, boolean createAccount) {
        if (plugin.getData().isSet("shop.balance." + p.getUniqueId().toString())) {
            return plugin.getData().getInt("shop.balance." + p.getUniqueId().toString());
        }
        else if (createAccount) {
            plugin.getData().set("shop.balance." + p.getUniqueId().toString(), 0);
            return 0;
        }
        else {
            return 0;
        }
    }
}