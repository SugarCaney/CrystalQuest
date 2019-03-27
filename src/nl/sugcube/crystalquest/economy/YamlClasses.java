package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author SugarCaney
 */
public class YamlClasses implements Classes {

    private CrystalQuest plugin;

    public YamlClasses(CrystalQuest plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasClass(Player player, String classId) {
        String node = getNode(player);
        return plugin.getData().getStringList(node).contains(classId);
    }

    @Override
    public void registerClass(Player player, String classId) {
        String node = getNode(player);
        List<String> kits = plugin.getData().getStringList(node);
        if (!kits.contains(classId)) {
            kits.add(classId);
        }
        plugin.getData().set(node, kits);
    }

    @Override
    public Set<String> getAllClasses(Player player) {
        String node = getNode(player);
        return new HashSet<>(plugin.getData().getStringList(node));
    }

    private String getNode(Player player) {
        return "shop.classes." + player.getUniqueId();
    }
}
