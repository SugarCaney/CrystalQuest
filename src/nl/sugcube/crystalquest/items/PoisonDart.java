package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author SugarCaney
 */
public class PoisonDart extends ItemExecutor {

    private static final PotionEffect POISON = new PotionEffect(PotionEffectType.POISON, 99999, 1);

    public PoisonDart() {
        super(Material.SUGAR_CANE);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        TippedArrow arrow = player.launchProjectile(TippedArrow.class);
        arrow.addCustomEffect(POISON, true);
        arrow.setVelocity(arrow.getVelocity().multiply(0.6));
        player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 12F, 12F);

        return true;
    }
}
