package nl.sugcube.crystalquest.listeners;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.events.ArenaStartEvent;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.sba.SItem;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

/**
 * @author SugarCaney
 */
public class ArenaListener implements Listener {

    public static CrystalQuest plugin;

    public ArenaListener(CrystalQuest instance) {
        plugin = instance;
    }

    @EventHandler
    public void onArenaStart(ArenaStartEvent e) {
        Arena a = e.getArena();
        for (Team t : a.getScoreboardTeams()) {
            if (t.getPlayers().size() == a.getPlayers().size()) {
                for (UUID id : a.getPlayers()) {
                    Player p = Bukkit.getPlayer(id);
                    p.sendMessage(Broadcast.get("arena.not-start-teams"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause() == TeleportCause.ENDER_PEARL) {
            if (plugin.getArenaManager().isInGame(player)) {
                if (!plugin.protection.isInProtectedArena(event.getTo())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!plugin.getConfig().getBoolean("arena.blood") || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (!plugin.protection.isInProtectedArena(event.getEntity().getLocation())) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            Player p = (Player)event.getEntity();
            if (!plugin.getArenaManager().isInGame(p) || plugin.getArenaManager().isSpectator(p)) {
                return;
            }
        }

        event.getEntity().getWorld().playEffect(event.getEntity().getLocation().add(0, 1.5, 0),
                Effect.STEP_SOUND, SItem.toMaterial(plugin.getConfig().getString("arena.blood-material")));
    }

}
