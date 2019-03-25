package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author SugarCaney
 */
public class Sugar extends ItemExecutor {

    private static final double DURATION_IN_SECONDS = 7;
    private static final int SPEED_AMPLIFIER = 6;
    private static final int JUMP_AMPLIFIER = 4;

    public Sugar() {
        super(Material.SUGAR);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, (int)(DURATION_IN_SECONDS * 20), SPEED_AMPLIFIER);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(speed, true);

        PotionEffect jumpBoost = new PotionEffect(PotionEffectType.JUMP, (int)(DURATION_IN_SECONDS * 20), JUMP_AMPLIFIER);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.addPotionEffect(jumpBoost, true);

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 12F, 12F);

        return true;
    }
}
