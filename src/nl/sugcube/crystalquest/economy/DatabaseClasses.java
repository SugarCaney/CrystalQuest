package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.data.QueryEconomy;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author SugarCaney
 */
public class DatabaseClasses implements Classes {

    private QueryEconomy query;

    public DatabaseClasses(QueryEconomy query) {
        this.query = query;
    }

    @Override
    public boolean hasClass(Player player, String classId) {
        return query.hasClass(player.getUniqueId(), classId);
    }

    @Override
    public void registerClass(Player player, String classId) {
        query.registerClass(player.getUniqueId(), classId);
    }

    @Override
    public Set<String> getAllClasses(Player player) {
        return query.getAllClasses(player.getUniqueId());
    }
}
