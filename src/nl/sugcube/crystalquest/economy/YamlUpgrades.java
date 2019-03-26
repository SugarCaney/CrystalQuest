package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class YamlUpgrades implements Upgrades {

    private CrystalQuest plugin;

    public YamlUpgrades(CrystalQuest plugin) {
        this.plugin = plugin;
    }

    @Override
    public int getLevel(Player player, ShopUpgrade upgrade) {
        String node = "shop." + upgrade.getType() + "." + player.getUniqueId().toString() + "." + upgrade.getId();
        if (plugin.getData().isSet(node)) {
            return plugin.getData().getInt(node);
        }
        else {
            plugin.getData().set(node, 0);
            return 0;
        }
    }

    @Override
    public void setLevel(Player player, ShopUpgrade upgrade, int newLevel) {
        plugin.getData().set("shop." + upgrade.getType() + "." + player.getUniqueId().toString() + "." + upgrade.getId(), newLevel);
    }
}