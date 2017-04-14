package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Teams;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.ArenaManager;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class WolfHeart extends ItemExecutor {

    private static Random random = new Random();

    public WolfHeart() {
        super(Material.BONE);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        // Get wolf buffs.
        int strengthLevel = (int)Multipliers.getMultiplier(
                "wolfstrength",
                plugin.economy.getLevel(player, "wolf", "upgrade"),
                false
        ) - 1;
        int resistanceLevel = (int)Multipliers.getMultiplier(
                "wolfresistance",
                plugin.economy.getLevel(player, "wolf", "upgrade"),
                false
        ) - 1;

        // Spawn wolfie
        ArenaManager arenaManager = plugin.getArenaManager();
        Arena arena = arenaManager.getArena(player.getUniqueId());
        int teamId = arenaManager.getTeam(player);

        World world = player.getWorld();
        Wolf wolf = world.spawn(player.getLocation(), Wolf.class);
        wolf.setOwner(player);
        wolf.setAdult();
        wolf.setCustomName(Teams.getTeamChatColour(teamId) + getWolfName());
        wolf.setCollarColor(Teams.getTeamDyeColour(teamId));
        wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);

        // Add potion effects
        wolf.addPotionEffect(
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, strengthLevel));

        if (resistanceLevel >= 0) {
            wolf.addPotionEffect(
                    new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999, resistanceLevel));
        }

        // Random effects
        wolf.playEffect(EntityEffect.WOLF_HEARTS);
        world.playSound(wolf.getLocation(), Sound.ENTITY_WOLF_GROWL, 3L, 3L);
        arena.getGameWolfs().add(wolf);

        return true;
    }

    /**
     * Just get a random name from a huge list of names :)
     *
     * @return (String) The random name
     */
    private String getWolfName() {
        return ItemHandler.dogNames.get(random.nextInt(ItemHandler.dogCount));
    }
}
