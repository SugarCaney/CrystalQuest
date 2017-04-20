package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import nl.sugcube.crystalquest.game.Teams;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author SugarCaney
 */
public class Glue extends ItemExecutor {

    private static final int GLUE_DURATION = 100;

    public Glue() {
        super(Material.SLIME_BALL);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        double multiplier = Multipliers.getMultiplier(
                "debuff",
                plugin.economy.getLevel(player, "debuff", "upgrade"),
                false
        );
        int duration = (int)(GLUE_DURATION * multiplier);

        PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, duration, 14);

        Arena arena = plugin.getArenaManager().getArena(player.getUniqueId());
        CrystalQuestTeam targetTeam = Teams.getRandomTeamToHit(player);

        if (arena.getPlayers().size() <= 1) {
            return true;
        }

        // Bloop a team.
        for (Player target : Teams.getPlayersFromTeam(arena, targetTeam)) {
            target.addPotionEffect(effect);
            target.playSound(target.getLocation(), Sound.BLOCK_SLIME_STEP, 12F, 12F);
        }

        player.playSound(player.getLocation(), Sound.BLOCK_SLIME_STEP, 12F, 12F);

        // Send confirmation message
        player.sendMessage(Broadcast.get("item-use.glue")
                .replace("%team%", targetTeam.toString()));

        return true;
    }
}
