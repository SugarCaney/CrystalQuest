package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author SugarCaney
 */
public class Landmine extends ItemExecutor {

    private static final Set<Material> PLACEABLE_MATERIALS = new HashSet<>();
    static {
        Collections.addAll(PLACEABLE_MATERIALS,
                Material.LEGACY_LONG_GRASS, Material.LEGACY_STATIONARY_WATER, Material.WATER,
                Material.DEAD_BUSH, Material.LEGACY_YELLOW_FLOWER, Material.LEGACY_RED_ROSE,
                Material.RAIL, Material.DETECTOR_RAIL, Material.POWERED_RAIL
        );
    }

    public Landmine() {
        super(Material.STONE_PRESSURE_PLATE);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        Arena arena = plugin.getArenaManager().getArena(player.getUniqueId());
        Location location = player.getLocation();

        // When the player has air at their feet.
        if (location.getBlock().getType().equals(Material.AIR)) {
            Block block = location.clone().add(0, -1, 0).getBlock();
            if (block.getType() == Material.AIR) {
                return true;
            }

            // Place landmine
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    new PlaceLandmine(location.getBlock(), arena, player),
                    40L
            );
            arena.getLandmines().put(location.getBlock().getLocation(), player.getUniqueId());

            return true;
        }

        // When it is obstructed.
        if (!PLACEABLE_MATERIALS.contains(location.getBlock().getType())) {
            return true;
        }

        Block blockOnTop = location.clone().add(0, 1, 0).getBlock();
        if (!blockOnTop.getType().equals(Material.AIR)) {
            return true;
        }

        player.playSound(player.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 12F, 12F);

        // Place landmine.
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin,
                new PlaceLandmine(blockOnTop, arena, player),
                40L
        );
        arena.getLandmines().put(blockOnTop.getLocation(), player.getUniqueId());

        return true;
    }
}
