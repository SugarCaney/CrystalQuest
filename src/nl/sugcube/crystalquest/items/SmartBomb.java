package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author SugarCaney
 */
public class SmartBomb extends ItemExecutor {

    private static Random random = new Random();

    public SmartBomb() {
        super(Material.CLAY_BALL);
    }

    @Override
    boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack) {
        Arena arena = plugin.getArenaManager().getArena(player.getUniqueId());

        // Summon creeper
        World world = player.getWorld();
        Location targetLocation = player.getLocation().clone();

        LivingEntity victim = findTarget(arena, player);
        if (victim == player) {
            targetLocation.add(Math.random() * 16 - 8, Math.random() * 3, Math.random() * 16 - 8);
        }

        Silverfish bomb = world.spawn(targetLocation, Silverfish.class);
        bomb.setTarget(victim);

        bomb.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 3), true);
        player.playSound(targetLocation, Sound.ENTITY_TNT_PRIMED, 10L, 10L);

        // Register creeper
        arena.getGameSmartBombs().add(bomb);

        return true;
    }

    /**
     * Determines the target for the smart bomb.
     */
    private LivingEntity findTarget(Arena arena, Player summoner) {
        List<Player> players = arena.getPlayers().stream().map(Bukkit::getPlayer).collect(Collectors.toList());
        if (players.size() == 0) {
            throw new IllegalStateException("There are no players in-game!");
        }

        // When there is only one player, target the one player.
        Player firstPlayer = players.get(0);
        CrystalQuestTeam summonerTeam = arena.getTeam(summoner);
        if (players.size() == 1) {
            return firstPlayer;
        }

        // When there are only people in one team (for debugging purposes).
        if (players.stream().allMatch(p -> arena.getTeam(p) == summonerTeam)) {
            return players.stream()
                    .filter(p -> !p.equals(summoner))
                    .min(Comparator.comparingDouble(p -> p.getLocation().distance(summoner.getLocation())))
                    .orElse(firstPlayer);
        }

        // Otherwise, choose the closest player of a different team.
        return players.stream()
                .filter(p -> arena.getTeam(p) != summonerTeam)
                .min(Comparator.comparingDouble(p -> p.getLocation().distance(summoner.getLocation())))
                .orElse(firstPlayer);
    }
}
