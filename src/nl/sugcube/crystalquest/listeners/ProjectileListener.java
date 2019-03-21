package nl.sugcube.crystalquest.listeners;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.ParticleHandler;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.events.PlayerEarnCrystalsEvent;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class ProjectileListener implements Listener {

    public static CrystalQuest plugin;

    public ProjectileListener(CrystalQuest instance) {
        plugin = instance;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Snowball)) {
            return;
        }

        Snowball ball = (Snowball)e.getDamager();
        if (!(ball.getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player)ball.getShooter();
        if (!plugin.getArenaManager().isInGame(shooter)) {
            return;
        }

        if (plugin.getArenaManager().getArena(shooter.getUniqueId()).getSpectators().contains(shooter.getUniqueId())) {
            return;
        }

        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity len = (LivingEntity)e.getEntity();
        if (len == null) {
            return;
        }

        len.setHealth(0);
        len.getWorld().playSound(len.getLocation(), Sound.ENTITY_BLAZE_DEATH, 20F, 20F);

        Random ran = new Random();
        double chance = Multipliers.getMultiplier("blood",
                plugin.economy.getLevel(shooter, "blood", "crystals"), false);
        int multiplier = 1;

        if (ran.nextInt(100) <= chance * 100 && chance != 0) {
            multiplier = 2;
        }

        //Adds crystals to shooter's balance
        int vip = 1;
        if (shooter.hasPermission("crystalquest.triplecash") ||
                shooter.hasPermission("crystalquest.admin") ||
                shooter.hasPermission("crystalquest.staff")) {
            vip = 3;
        }
        else if (shooter.hasPermission("crystalquest.doublecash")) {
            vip = 2;
        }
        int money = (int)(1 * plugin.getConfig().getDouble("shop.crystal-multiplier"));

        //Call Event
        int moneyEarned = money * multiplier * vip;

        PlayerEarnCrystalsEvent event = new PlayerEarnCrystalsEvent(shooter,
                plugin.getArenaManager().getArena(shooter.getUniqueId()), moneyEarned);
        Bukkit.getPluginManager().callEvent(event);

        String message = plugin.economy.getCoinMessage(shooter, event.getAmount());

        if (!event.isCancelled()) {
            plugin.economy.getBalance().addCrystals(shooter, event.getAmount(), false);

            if (event.showMessage() && message != null) {
                shooter.sendMessage(message);
            }
        }

        if (shooter instanceof Player) {
            FireworkEffect fireworkEffect = FireworkEffect.builder()
                    .with(Type.CREEPER)
                    .withColor(plugin.getArenaManager().getTeam(shooter).getColour())
                    .build();
            try {
                plugin.particleHandler.playFirework(len.getLocation().add(0, 4, 0), fireworkEffect);
            }
            catch (Exception ignored) {
            }

            return;
        }

        FireworkEffect fe = FireworkEffect.builder()
                .with(Type.CREEPER)
                .withColor(Color.WHITE)
                .build();
        try {
            plugin.particleHandler.playFirework(len.getLocation().add(0, 4, 0), fe);
        }
        catch (Exception ignored) {
        }
    }

    @EventHandler
    public void onProjectileDamageSpectator(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Projectile)) {
            return;
        }

        Player p = (Player)e.getEntity();
        Projectile proj = (Projectile)e.getDamager();

        if (!plugin.getArenaManager().isSpectator(p)) {
            return;
        }

        e.setCancelled(true);
        proj.setVelocity(proj.getVelocity());
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Egg) {
            Egg egg = (Egg)e.getEntity();
            if (!(egg.getShooter() instanceof Player)) {
                return;
            }

            Player pl = (Player)egg.getShooter();
            if (!plugin.getArenaManager().isInGame(pl)) {
                return;
            }

            double multiplier = Multipliers.getMultiplier("explosive",
                    plugin.economy.getLevel(pl, "explosive", "upgrade"), false);

            Location loc = egg.getLocation();
            loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), (float)(3.0 * multiplier), false, false);
        }
        else if (e.getEntity() instanceof Snowball) {
            ParticleHandler.balls.remove(e.getEntity());
        }
        else if (e.getEntity() instanceof Fireball) {
            if (!(e.getEntity().getShooter() instanceof Player)) {
                return;
            }

            Player pl = (Player)e.getEntity().getShooter();
            if (!plugin.getArenaManager().isInGame(pl)) {
                return;
            }
            
            double multiplier = Multipliers.getMultiplier("lightning",
                    plugin.economy.getLevel(pl, "explosive", "upgrade"), false);
            World w = e.getEntity().getLocation().getWorld();
            w.createExplosion(e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY(),
                    e.getEntity().getLocation().getZ(), (float)(1 + (multiplier - 0.5)), false, false);
        }
    }
}