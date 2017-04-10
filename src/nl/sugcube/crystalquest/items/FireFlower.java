package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author SugarCaney
 */
public class FireFlower extends ItemExecutor {

    public FireFlower() {
        super(Material.RED_ROSE);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        Fireball ball = player.launchProjectile(Fireball.class);
        ball.setVelocity(player.getLocation().getDirection().multiply(3));
        ball.setYield(0);

        return true;
    }
}
