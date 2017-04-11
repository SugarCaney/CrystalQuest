package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.ArenaManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import static java.lang.Math.*;

/**
 * @author SugarCaney
 */
public class ForceField extends ItemExecutor {

    private static final double FORCE_LIMIT = 1.5;

    public ForceField() {
        super(Material.GLASS);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        final World world = player.getWorld();
        final Location source = player.getLocation();
        final ArenaManager arenaManager = plugin.getArenaManager();
        final Arena arena = arenaManager.getArena(player.getUniqueId());
        final int teamId = arena.getTeam(player);

        world.getEntities().stream()
                .filter(ent -> {
                    // No null entities.
                    if (ent == null) {
                        return false;
                    }

                    // Don't nudge your own species.
                    if (ent instanceof Player) {
                        if (arena.getTeam((Player)ent) == teamId) {
                            return false;
                        }
                    }

                    // Don't nudge your own wolfs.
                    if (ent instanceof Wolf) {
                        if (player.equals(((Wolf)ent).getOwner())) {
                            return false;
                        }
                    }

                    // We only want to target living basta'ds.
                    return ent instanceof LivingEntity;
                })
                .forEach(ent -> applyForce(source, (LivingEntity)ent));

        player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 12F, 12F);

        return true;
    }

    private void applyForce(Location source, LivingEntity target) {
        final Location location = target.getLocation();
        final double sx = source.getX();
        final double sz = source.getZ();
        final double tx = location.getX();
        final double tz = location.getZ();

        // Determine velocity boost.
        double x = 1d / (tx - sx == 0 ? 0.0001 : ((tx - sx) * 1.25));
        double z = 1d / (tz - sz == 0 ? 0.0001 : ((tz - sz) * 1.25));

        // Apply limits.
        x = (x < 0 ? max(-FORCE_LIMIT, x) : max(FORCE_LIMIT, x)) / 1.2d;
        z = (z < 0 ? max(-FORCE_LIMIT, z) : max(FORCE_LIMIT, z)) / 1.2d;

        // Determine y velocity to make a 45 degree swoop.
        double y = sqrt(x * x + z * z);

        // Apply velocity
        Vector velocity = new Vector(x, y, z);
        target.setVelocity(velocity);

        // And damage a bit
        double damage = 8 - location.distance(source);
        if (damage > 0) {
            target.damage(damage);
        }
    }
}
