package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Teams;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author SugarCaney
 */
public class Blooper extends ItemExecutor {

    private static final int BLOOPER_DURATION = 118;

    public Blooper() {
        super(Material.INK_SACK);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        double multiplier = Multipliers.getMultiplier(
                "debuff",
                plugin.economy.getLevel(player, "debuff", "upgrade"),
                false
        );
        int duration = (int)(BLOOPER_DURATION * multiplier);

        PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, duration, 14);
        PotionEffect effect2 = new PotionEffect(PotionEffectType.SPEED, duration, 0);

        Arena arena = plugin.getArenaManager().getArena(player.getUniqueId());
        int targetTeam = Teams.getRandomTeamToHit(player);

        if (arena.getPlayers().size() <= 1) {
            return true;
        }

        // Bloop a team.
        for (Player target : Teams.getPlayersFromTeam(arena, targetTeam)) {
            target.addPotionEffect(effect);
            target.addPotionEffect(effect2);
            target.playSound(target.getLocation(), Sound.BLOCK_SLIME_STEP, 12F, 12F);
        }

        player.playSound(player.getLocation(), Sound.BLOCK_SLIME_STEP, 12F, 12F);

        // Send confirmation message
        player.sendMessage(Broadcast.get("item-use.blooper")
                .replace("%team%", Teams.getTeamNameById(targetTeam)));

        return true;
    }
}
