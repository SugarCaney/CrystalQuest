package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.data.QueryEconomy;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class DatabaseUpgrades implements Upgrades {

    private QueryEconomy query;

    public DatabaseUpgrades(QueryEconomy query) {
        this.query = query;
    }

    @Override
    public int getLevel(Player player, ShopUpgrade upgrade) {
        return query.getUpgradeLevel(player.getUniqueId(), upgrade);
    }

    @Override
    public void setLevel(Player player, ShopUpgrade upgrade, int newLevel) {
        query.setUpgradeLevel(player.getUniqueId(), upgrade, newLevel);
    }
}
