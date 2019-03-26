package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.data.QueryEconomy;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class DatabaseBalance implements Balance {

    private QueryEconomy query;

    public DatabaseBalance(QueryEconomy query) {
        this.query = query;
    }

    @Override
    public void setBalance(Player player, int newBalance) {
        query.setBalance(player.getUniqueId(), newBalance);
    }

    @Override
    public void addBalance(Player player, int amount, boolean showMessage) {
        int newBalance = getBalance(player, true) + amount;
        setBalance(player, newBalance);
        if (showMessage) {
            player.sendMessage(Broadcast.TAG + String.format(Broadcast.get("shop.crystals-added"), amount));
        }
    }

    @Override
    public int getBalance(Player player, boolean createAccount) {
        return query.getBalance(player.getUniqueId());
    }
}