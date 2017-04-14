package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Teams;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author SugarCaney
 */
public class LightningBolt extends ItemExecutor {

    private static final int NAUSEA_DURATION = 200;

    public LightningBolt() {
        super(Material.FEATHER);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        // Fetch upgrade levels.
        double explosion = Multipliers.getMultiplier(
                "lightning",
                plugin.economy.getLevel(player, "explosive", "upgrade"),
                false
        );

        double multiplier = Multipliers.getMultiplier(
                "debuff",
                plugin.economy.getLevel(player, "debuff", "upgrade"),
                false
        );
        int duration = (int)(NAUSEA_DURATION * multiplier);

        PotionEffect effect = new PotionEffect(PotionEffectType.CONFUSION, duration, 1);

        Arena arena = plugin.getArenaManager().getArena(player.getUniqueId());
        int targetTeam = Teams.getRandomTeamToHit(player);

        if (arena.getPlayers().size() <= 1) {
            return true;
        }

        // Bloop a team.
        for (Player target : Teams.getPlayersFromTeam(arena, targetTeam)) {
            World world = target.getWorld();
            Location location = target.getLocation();

            // Explosion when upgrade is enabled.
            if (explosion > 0) {
                world.createExplosion(
                        location.getX(), location.getY(), location.getZ(),
                        (float)explosion, false, false
                );
            }

            // Strike
            world.strikeLightning(location);
            target.addPotionEffect(effect);
        }

        // Send confirmation message
        player.sendMessage(Broadcast.get("item-use.lightning")
                .replace("%team%", Teams.getTeamNameById(targetTeam)));

        return true;
    }
}
