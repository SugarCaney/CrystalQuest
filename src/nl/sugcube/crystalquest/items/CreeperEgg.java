package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.Set;

/**
 * @author SugarCaney
 */
public class CreeperEgg extends ItemExecutor {

    private static Random random = new Random();

    public CreeperEgg() {
        super(Material.MONSTER_EGG);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        // Summon creeper
        World world = player.getWorld();
        Location targetLocation = player.getTargetBlock((Set<Material>)null, 64).getLocation();
        targetLocation = targetLocation.add(0, 1, 0);
        Creeper creeper = world.spawn(targetLocation, Creeper.class);
        player.playSound(targetLocation, Sound.ENTITY_CREEPER_PRIMED, 10L, 10L);

        // Handle upgrades
        double chance = Multipliers.getMultiplier(
                "creeper",
                (plugin.economy.getLevel(player, "creepers", "upgrade")),
                false
        );

        // Charged
        if (random.nextInt(1000) <= chance * 1000) {
            creeper.setPowered(true);
        }

        player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 12F, 12F);

        // Register creeper
        Arena arena = plugin.getArenaManager().getArena(player.getUniqueId());
        arena.getGameCreepers().add(creeper);

        return true;
    }
}
