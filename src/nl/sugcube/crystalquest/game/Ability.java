package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.economy.Multipliers;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SugarCaney
 */
public class Ability implements Listener {

    public static CrystalQuest plugin;

    public ConcurrentHashMap<UUID, List<String>> abilities;

	/*
     * ABILITIES:
	 * doublejump_boost
	 * agility
	 * super_speed
	 * strength
	 * poison
	 * explosive_arrows
	 * resistance
	 * jump_boost
	 * health_boost
	 * last_revenge
	 * less_death_crystals
	 * return_arrow
	 * drain
	 * slowness
	 * weakness
	 * water_healing
	 * corroding
	 * magical_aura
	 * power_loss
	 */

    public Ability(CrystalQuest instance) {
        plugin = instance;
        this.abilities = new ConcurrentHashMap<UUID, List<String>>();
    }

    public ConcurrentHashMap<UUID, List<String>> getAbilities() {
        return this.abilities;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = (Player)e.getEntity();
        if (plugin.getArenaManager().isInGame(p)) {
            if (plugin.ab.getAbilities().containsKey(p.getUniqueId())) {
                if (plugin.ab.getAbilities().get(p.getUniqueId()).contains("last_revenge")) {
                    Arena a = plugin.getArenaManager().getArena(p.getUniqueId());
                    CrystalQuestTeam targetTeam = Teams.getRandomTeamToHit(p);

                    if (a.getPlayers().size() > 1) {
                        double lightning = Multipliers.getMultiplier("lightning",
                                plugin.economy.getLevel(p, "explosive", "upgrade"), false);

                        double multiplier = Multipliers.getMultiplier("debuff",
                                plugin.economy.getLevel(p, "debuff", "upgrade"), false);
                        int duration = (int)(218 * multiplier);

                        for (OfflinePlayer olTarget : a.getTeamObject(targetTeam).getPlayers()) {
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (onlinePlayer == olTarget) {
                                    if (lightning > 0) {
                                        World w = onlinePlayer.getLocation().getWorld();
                                        w.createExplosion(onlinePlayer.getLocation().getX(), onlinePlayer.getLocation().getY(),
                                                onlinePlayer.getLocation().getZ(), (float)lightning, false, false);
                                    }

                                    onlinePlayer.getWorld().strikeLightning(onlinePlayer.getLocation());
                                    onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration, 1));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow)) {
            return;
        }

        Arrow a = (Arrow)e.getEntity();
        if (!(a.getShooter() instanceof Player)) {
            return;
        }

        Player p = (Player)a.getShooter();
        if (!plugin.getArenaManager().isInGame(p)) {
            return;
        }

        if (!plugin.ab.getAbilities().containsKey(p.getUniqueId())) {
            return;
        }

        if (!plugin.ab.getAbilities().get(p.getUniqueId()).contains("explosive_arrows")) {
            return;
        }

        World w = e.getEntity().getWorld();
        Location loc = e.getEntity().getLocation();
        if (!plugin.prot.isInProtectedArena(loc)) {
            return;
        }

        w.createExplosion(loc.getX(), loc.getY(), loc.getZ(), 1.6F, false, false);
        a.remove();
    }

    @EventHandler
    public void onPlayerMoveEffect(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (plugin.getArenaManager().isInGame(p)) {
            if (!plugin.getArenaManager().getArena(p.getUniqueId()).isCounting()) {
                List<String> ability = plugin.ab.getAbilities().get(p.getUniqueId());
                if (plugin.ab.getAbilities().containsKey(p.getUniqueId())) {
                    if (ability.contains("agility")) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 0));
                    }
                    if (ability.contains("super_speed")) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 1));
                    }
                    if (ability.contains("strength")) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 0));
                    }
                    if (ability.contains("resistance")) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 160, 0));
                    }
                    if (ability.contains("jump_boost")) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 160, 1));
                    }
                    if (plugin.ab.getAbilities().get(p.getUniqueId()).contains("health_boost")) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 36000, 0));
                    }
                    if (plugin.ab.getAbilities().get(p.getUniqueId()).contains("slowness")) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 160, 0));
                    }
                    if (plugin.ab.getAbilities().get(p.getUniqueId()).contains("weakness")) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 160, 0));
                    }
                    if (plugin.ab.getAbilities().get(p.getUniqueId()).contains("water_healing")) {
                        if (p.getLocation().getBlock().getType() == Material.WATER ||
                                p.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 2));
                        }
                    }
                    if (plugin.ab.getAbilities().get(p.getUniqueId()).contains("corroding")) {
                        if (p.getLocation().getBlock().getType() == Material.WATER ||
                                p.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity)e.getEntity();
            if (e.getDamager() instanceof Player) {
                Player damager = (Player)e.getDamager();
                if (plugin.getArenaManager().isInGame(damager)) {
                    if (plugin.ab.getAbilities().containsKey(damager.getUniqueId())) {
                        if (plugin.ab.getAbilities().get(damager.getUniqueId()).contains("poison")) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
                        }
                        else if (plugin.ab.getAbilities().get(damager.getUniqueId()).contains("drain")) {
                            if (target instanceof Player) {
                                if (plugin.getArenaManager().getTeam((Player)target) ==
                                        plugin.getArenaManager().getTeam(damager)) {
                                }
                            }
                        }
                    }
                }
            }
            else if (e.getDamager() instanceof Arrow) {
                if (((Arrow)e.getDamager()).getShooter() instanceof Player) {
                    Player damager = (Player)((Arrow)e.getDamager()).getShooter();

                    if (plugin.getArenaManager().isInGame(damager)) {
                        if (plugin.ab.getAbilities().containsKey(damager.getUniqueId())) {
                            if (plugin.ab.getAbilities().get(damager.getUniqueId()).contains("poison")) {
                                target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
                            }
                            else if (plugin.ab.getAbilities().get(damager.getUniqueId()).contains("return_arrow")) {
                                if (target instanceof Player) {
                                    if (plugin.getArenaManager().getTeam((Player)target) ==
                                            plugin.getArenaManager().getTeam(damager)) {
                                        return;
                                    }
                                }
                                else if (target instanceof Wolf) {
                                    return;
                                }

                                damager.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                                e.getDamager().remove();
                            }
                            else {
                                if (target instanceof Player) {
                                    if (plugin.getArenaManager().getTeam((Player)target) ==
                                            plugin.getArenaManager().getTeam(damager)) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
