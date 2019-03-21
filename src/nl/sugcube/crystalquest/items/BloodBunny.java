package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author SugarCaney
 */
public class BloodBunny extends ItemExecutor {

    private static final Set<PotionEffect> RABBIT_BUFF = Collections.unmodifiableSet(
            new HashSet<PotionEffect>() {{
                add(new PotionEffect(PotionEffectType.JUMP, 9999, 0));
                add(new PotionEffect(PotionEffectType.SPEED, 9999, 1));
                add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, 1));
                add(new PotionEffect(PotionEffectType.REGENERATION, 9999, 0));
                add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999, 1));
            }});

    public BloodBunny() {
        super(Material.CARROT);
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
        // Pick target player.
        Arena arena = plugin.getArenaManager().getArena(player.getUniqueId());
        Player target = arena.getRandomPlayer(player);
        if (target == null) {
            target = player;
        }

        // Summon rabbit.
        World world = player.getWorld();
        Rabbit rabbit = world.spawn(target.getLocation().add(0, 2, 0), Rabbit.class);
        rabbit.setRabbitType(Type.THE_KILLER_BUNNY);
        rabbit.setCustomName("Killer Rabbit of Caerbannog");
        rabbit.setTarget(target);
        rabbit.addPotionEffects(RABBIT_BUFF);
        rabbit.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20f);

        player.playSound(player.getLocation(), Sound.ENTITY_RABBIT_DEATH, 12F, 12F);

        // Send confirmation message
        player.sendMessage(Broadcast.get("item-use.rabbit")
                .replace("%player%", target.getName()));

        return true;
    }
}
