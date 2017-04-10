package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.ParticleHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;

/**
 * @author SugarCaney
 */
public class Railgun extends ItemExecutor {

    public Railgun() {
        super(Material.IRON_HOE);
    }

    @Override
    public void removeItem(CrystalQuest plugin, Player player, ItemStack itemStack) {
        if (itemStack.getAmount() == 1) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    plugin, () -> player.getInventory().removeItem(itemStack), 1L);
        }
        else {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        // Play sound.
        Location location = player.getLocation();
        player.playSound(location, Sound.BLOCK_ANVIL_LAND, 20f, 20f);

        // Shoot bullet
        final Snowball ball = player.launchProjectile(Snowball.class);
        ball.setVelocity(player.getLocation().getDirection().multiply(15));
        ParticleHandler.balls.add(ball);
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                plugin,
                () -> ParticleHandler.balls.remove(ball),
                60L
        );

        return true;
    }
}
