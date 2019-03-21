package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import nl.sugcube.crystalquest.game.Teams;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class Curse extends ItemExecutor {

    private static final Random random = new Random();

    public Curse() {
        super(Material.LEGACY_SKULL_ITEM, (short)0);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        // Determine target
        Arena arena = plugin.getArenaManager().getArena(player.getUniqueId());
        CrystalQuestTeam targetTeam = Teams.getRandomTeamToHit(player);

        if (arena.getPlayers().size() <= 1) {
            return true;
        }

        // Determine duration.
        double multiplier = Multipliers.getMultiplier(
                "debuff",
                plugin.economy.getLevel(player, "debuff", "upgrade"),
                false
        );
        int duration = (int)(20 * 8 + random.nextInt(5) * multiplier);

        // Curse a team.
        for (Player target : Teams.getPlayersFromTeam(arena, targetTeam)) {
            plugin.itemHandler.cursed.put(target, duration);
            target.playEffect(EntityEffect.WOLF_SMOKE);
            target.playEffect(EntityEffect.WITCH_MAGIC);
            target.playSound(target.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 10L, 10L);
            target.sendMessage(Broadcast.get("messages.cursed"));
        }

        // Send confirmation message
        player.sendMessage(Broadcast.get("item-use.cursed")
                .replace("%team%", targetTeam.toString()));

        return true;
    }
}
