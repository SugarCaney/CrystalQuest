package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class Anvil extends ItemExecutor {

    private static final int RADIUS = 8;
    private static final int ANVIL_AMOUNT = 20;
    private static final Random RANDOM = new Random();

    public Anvil() {
        super(Material.ANVIL);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        int totalAnvils = ANVIL_AMOUNT + RANDOM.nextInt(20);
        for (int j = 0; j < totalAnvils; j++) {
            Location location = player.getLocation();

            location.setX(RADIUS - (RANDOM.nextInt(RADIUS * 2)) + location.getX());
            location.setZ(RADIUS - (RANDOM.nextInt(RADIUS * 2)) + location.getZ());

            for (int i = 0; i < 32; i++) {
                location.add(0, 1, 0);
                if (location.getBlock().getType() != Material.AIR) {
                    if (location.clone().add(0, -2, 0).getBlock().getType() == Material.AIR) {
                        location.add(0, -1, 0);
                        break;
                    }
                }
            }

            if (location.getBlock().getType() == Material.AIR) {
                location.getBlock().setType(Material.ANVIL);
            }
        }

        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 12F, 12F);

        return true;
    }
}
