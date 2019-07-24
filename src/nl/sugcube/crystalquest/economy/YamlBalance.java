package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class YamlBalance implements Balance {

    private CrystalQuest plugin;

    public YamlBalance(CrystalQuest instance) {
        plugin = instance;
    }

    public void setBalance(Player p, int newBalance) {
        if (newBalance < 0) {
            newBalance = 0;
        }

        plugin.getData().set("shop.balance." + p.getUniqueId().toString(), newBalance);
        plugin.saveData();
    }

    public void addBalance(Player p, int amount, boolean showMessage) {
        int balance = plugin.getData().getInt("shop.balance." + p.getUniqueId().toString());
        balance += amount;
        plugin.getData().set("shop.balance." + p.getUniqueId().toString(), balance);
        if (showMessage) {
            String messageString = String.format(Broadcast.get("shop.added-to-account"), amount);
            p.sendMessage(Broadcast.TAG + messageString);
        }
        plugin.saveData();
    }

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