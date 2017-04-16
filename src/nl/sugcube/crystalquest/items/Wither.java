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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author SugarCaney
 */
public class Wither extends ItemExecutor {

    private static final int WITHER_DURATION = 120;

    public Wither() {
        super(Material.SKULL_ITEM, (short)1);
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
        int duration = (int)(WITHER_DURATION * multiplier);

        // Sound & sacrifice of 2 hearts.
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_HURT, 10L, 10);
        player.setHealth(player.getHealth() - 4 < 0 ? 0 : player.getHealth() - 4);

        // Curse a team.
        for (Player target : Teams.getPlayersFromTeam(arena, targetTeam)) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, duration, 2));
            target.playEffect(EntityEffect.WOLF_SMOKE);
            target.playSound(target.getLocation(), Sound.ENTITY_WITHER_HURT, 10L, 10L);
        }

        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_HURT, 12F, 12F);

        // Send confirmation message
        player.sendMessage(Broadcast.get("item-use.wither")
                .replace("%team%", targetTeam.toString()));

        return true;
    }
}
