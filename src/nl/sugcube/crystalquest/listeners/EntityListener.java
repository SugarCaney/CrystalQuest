package nl.sugcube.crystalquest.listeners;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import nl.sugcube.crystalquest.game.Teams;
import nl.sugcube.crystalquest.util.Materials;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class EntityListener implements Listener {

    public static CrystalQuest plugin;

    public EntityListener(CrystalQuest instance) {
        plugin = instance;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity ent = e.getEntity();
        if (ent instanceof Creeper) {
            Creeper creeper = (Creeper)ent;
            boolean isGameCreeper = false;
            for (Arena a : plugin.getArenaManager().getArenas()) {
                for (Creeper creep : a.getGameCreepers()) {
                    if (creep == creeper) {
                        isGameCreeper = true;
                    }
                }
            }

            if (isGameCreeper) {
                double amount = 1;
                if (e.getEntity().getKiller() != null) {
                    Player p = e.getEntity().getKiller();
                    amount = Multipliers.getMultiplier("creepergem", plugin.economy.getLevel(p, "creepers", "upgrade"), false);
                }
                for (double i = 0; i < amount; i++) {
                    ItemStack shards = plugin.itemHandler.getItemByName(Broadcast.get("items.crystal-shard"));
                    creeper.getWorld().dropItem(creeper.getLocation(), shards);
                }
            }
        }

        if (plugin.protection.isInProtectedArena(ent.getLocation())) {
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof Fireball) {
            if (plugin.protection.isInProtectedArena(e.getEntity().getLocation())) {
                e.setCancelled(true);
            }
        }

        if (e.getEntity() instanceof EnderCrystal) {
            if (plugin.protection.isInProtectedArena(e.getEntity().getLocation())) {
                e.setCancelled(true);
                Location loc = e.getLocation();
                loc.getWorld().createExplosion(loc.getX(), loc.getY() + 2, loc.getZ(), 5.0F, false, false);
                Entity toRemove = null;
                for (Arena a : plugin.getArenaManager().getArenas()) {
                    for (Entity ent : a.getGameCrystals()) {
                        if (ent == e.getEntity()) {
                            toRemove = ent;
                        }
                    }
                    if (toRemove != null) {
                        a.getGameCrystals().remove(toRemove);
                    }
                }
            }

            for (Arena a : plugin.getArenaManager().getArenas()) {
                Entity toRemove = null;
                for (Entity ent : a.getGameCrystals()) {
                    if (ent == e.getEntity()) {
                        e.setCancelled(true);
                        Location loc = e.getLocation();
                        loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 5.0F, false, false);
                        toRemove = ent;
                    }
                }
                if (toRemove != null) {
                    a.getGameCrystals().remove(toRemove);
                }
            }

            if (e.getEntity().getLocation().distance(plugin.getArenaManager().getLobby()) <= 200) {
                e.setCancelled(true);
            }
        }
        else if (e.getEntity() instanceof Creeper) {
            if (plugin.protection.isInProtectedArena(e.getEntity().getLocation())) {
                e.setCancelled(true);
                Location loc = e.getLocation();
                loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4.0F, false, false);
                Creeper toRemove = null;
                for (Arena a : plugin.getArenaManager().getArenas()) {
                    for (Creeper c : a.getGameCreepers()) {
                        if (c == e.getEntity()) {
                            toRemove = c;
                        }
                    }
                    if (toRemove != null) {
                        a.getGameCreepers().remove(toRemove);
                    }
                }
            }

            for (Arena a : plugin.getArenaManager().getArenas()) {
                Creeper toRemove = null;
                for (Creeper c : a.getGameCreepers()) {
                    if (c == e.getEntity() || plugin.protection.isInProtectedArena(c.getLocation())) {
                        e.setCancelled(true);
                        Location loc = e.getLocation();
                        loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4.0F, false, false);
                        toRemove = c;
                    }
                }
                if (toRemove != null) {
                    a.getGameCreepers().remove(toRemove);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        /*
         * Block friendly Dog-fire
		 */
        if (e.getEntity() instanceof Wolf) {
            Wolf w = (Wolf)e.getEntity();
            DyeColor collar = w.getCollarColor();
            CrystalQuestTeam wolfTeam = Teams.getDyeColourTeam(collar);
            if (e.getDamager() instanceof Player) {
                CrystalQuestTeam damagerTeam = plugin.getArenaManager().getTeam((Player)e.getDamager());
                if (wolfTeam == damagerTeam) {
                    e.setCancelled(true);
                }
            }
            else if (e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow)e.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    CrystalQuestTeam damagerTeam = plugin.getArenaManager().getTeam((Player)arrow.getShooter());
                    if (wolfTeam == damagerTeam) {
                        e.setCancelled(true);
                    }
                }
            }
        }

        if (e.getEntity() instanceof EnderCrystal) {
            boolean isForGame = false;
            boolean isValid = true;
            if (e.getDamager() instanceof Projectile) {
                if (((Projectile)e.getDamager()).getShooter() instanceof Player) {
                    Projectile proj = (Projectile)e.getDamager();
                    Player player = (Player)proj.getShooter();
                    if (plugin.arenaManager.isInGame(player)) {
                        isForGame = true;
                        isValid = false;
                    }
                }
            }
            else if (e.getDamager() instanceof Player) {
                if (plugin.arenaManager.isInGame(((Player)e.getDamager()))) {
                    isForGame = true;
                    isValid = true;
                }
            }
            else if (e.getDamager() instanceof Creeper) {
                e.setCancelled(true);
            }

            if (isForGame) {
                e.setCancelled(true);
                if (e.getDamager() instanceof Player) {
                    Player pl = (Player)e.getDamager();
                    plugin.getArenaManager().getArena(pl.getUniqueId()).getGameCrystals().remove(e.getEntity());
                    plugin.getArenaManager().getArena(pl.getUniqueId()).getCrystalLocations().remove(e.getEntity());

                    if (isValid) {
                        if (isOwnTeamCrystal((EnderCrystal)e.getEntity(), pl)) {
                            pl.sendMessage(Broadcast.get("arena.own-crystals")
                                    .replace("%colour%", "" + plugin.getArenaManager().getTeam(pl)
                                            .getChatColour()));
                            return;
                        }

                        double chance = Multipliers.getMultiplier("smash",
                                plugin.economy.getLevel(pl, "smash", "crystals"), false);
                        double multi = 3;

                        if (chance > 0) {
                            Random ran = new Random();
                            if (ran.nextInt(100) <= chance * 100) {
                                multi = 6;
                            }
                        }

                        plugin.arenaManager.getArena(pl.getUniqueId()).addScore(plugin.arenaManager.getTeam(pl), (int)multi);
                        e.getEntity().remove();

                        Firework f = e.getEntity().getLocation().getWorld().spawn(e.getEntity().getLocation(), Firework.class);
                        FireworkMeta fm = f.getFireworkMeta();
                        fm.setPower(0);
                        FireworkEffect fe = FireworkEffect.builder()
                                .flicker(true)
                                .withColor(plugin.arenaManager.getTeam(pl).getColour())
                                .build();
                        fm.clearEffects();
                        fm.addEffect(fe);
                        f.setFireworkMeta(fm);
                    }
                }
            }
        }
    }

    /**
     * Checks if the crystal is not marked as a defence-crystal.
     * This means that the own team shouldn't smash these!
     *
     * @param ec
     *         (EnderCrystal) The crystal that has been smashed.
     * @param pl
     *         (Player) The player who has smashed the crystal.
     * @return (boolean) True if the crystal is team-specified.
     */
    public boolean isOwnTeamCrystal(EnderCrystal ec, Player pl) {
        Location loc = ec.getLocation().add(0, -1, 0);
        Material blockBelow = loc.getBlock().getType();
        return Materials.isWool(blockBelow) && Teams.getTeamFromWoolMaterial(blockBelow) == plugin.getArenaManager().getTeam(pl);
    }
}