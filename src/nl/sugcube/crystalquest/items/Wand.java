package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.ArenaManager;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import nl.sugcube.crystalquest.util.BukkitUtil;
import org.bukkit.*;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.Set;
import java.util.UUID;

/**
 * @author SugarCaney
 */
public class Wand implements Listener {

    public CrystalQuest plugin;

    public Wand(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Gets the item representing the wand.
     *
     * @param type
     *         (WandType) The type of wand you choose
     * @return (ItemStack) The ItemStack-variant of the wand.
     */
    public ItemStack getWand(WandType type) {
        ItemStack is = new ItemStack(type.getMaterial(), 1);
        is = type.setDisplayName(is, type);
        return is;
    }

    /**
     * Checks what the type of wand the give ItemStack is.
     *
     * @param is
     *         (ItemStack) The itemstack to check for.
     * @return (WandType) The type of wand. null if it isn't a valid type.
     */
    public WandType getWandType(ItemStack is) {
        if (is != null) {
            if (is.hasItemMeta()) {
                if (is.getItemMeta().hasDisplayName()) {
                    String name = is.getItemMeta().getDisplayName();
                    for (WandType wt : WandType.values()) {
                        if (Broadcast.get(wt.getDisplayName()).equalsIgnoreCase(name)) {
                            return wt;
                        }
                    }
                }
            }
        }
        return null;
    }

    @EventHandler
    public void onWandUse(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        ArenaManager arenaManager = plugin.getArenaManager();

        if (!arenaManager.isInGame(e.getPlayer())) {
            return;
        }

        Player player = e.getPlayer();
        Arena arena = arenaManager.getArena(player.getUniqueId());

        if (player.getInventory().getItemInMainHand() == null) {
            return;
        }

        /*
         * WAND: MAGMA
         */
        if (getWandType(player.getInventory().getItemInMainHand()) == WandType.MAGMA) {
            if (((Damageable)player.getInventory().getItemInMainHand().getItemMeta()).getDamage() == 0) {
                Fireball fb = player.launchProjectile(Fireball.class);
                fb.setVelocity(player.getLocation().getDirection().multiply(3));

                for (UUID id : arenaManager.getArena(player.getUniqueId()).getPlayers()) {
                    Player pl = Bukkit.getPlayer(id);
                    if (pl != player && arenaManager.getTeam(pl) != arenaManager.getTeam(player)) {
                        double pX = player.getLocation().getX();
                        double pZ = player.getLocation().getZ();
                        double plX = pl.getLocation().getX();
                        double plZ = pl.getLocation().getZ();

                        if (plX > pX - 6 && plX < pX + 6) {
                            if (plZ > pZ - 6 && plZ < pZ + 6) {
                                pl.setFireTicks(100);
                                pl.playEffect(pl.getLocation(), Effect.MOBSPAWNER_FLAMES, null);
                                pl.playSound(pl.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1F, 1F);
                            }
                        }
                    }
                }

                resetDurability(player.getInventory().getItemInMainHand(), WandType.MAGMA);
            }

            return;
        }

        /*
         * WAND: TELEPORT
         */
        if (getWandType(player.getInventory().getItemInMainHand()) == WandType.TELEPORT) {
            if (((Damageable)player.getInventory().getItemInMainHand().getItemMeta()).getDamage() == 0) {
                Set<Material> targetSet = null;
                Location loc = player.getTargetBlock(targetSet, 64).getLocation().add(0, 1, 0);
                if (loc.getBlock().getType() == Material.AIR) {
                    if (plugin.protection.isInProtectedArena(loc)) {
                        boolean isInBounds = false;
                        double dX = player.getLocation().getX() - loc.getX();
                        double dY = player.getLocation().getY() - loc.getY();
                        double dZ = player.getLocation().getZ() - loc.getZ();

                        if (Math.sqrt(dX * dX + dY * dY + dZ * dZ) <= 40) {
                            isInBounds = true;
                        }

                        if (isInBounds) {
                            float yaw = player.getLocation().getYaw();
                            player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                            loc.setYaw(yaw);
                            player.teleport(loc);
                            player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                            resetDurability(player.getInventory().getItemInMainHand(), WandType.TELEPORT);
                        }
                    }
                }
            }

            return;
        }

        /*
         * WAND: HEAL
         */
        if (getWandType(player.getInventory().getItemInMainHand()) == WandType.HEAL) {
            if (((Damageable)player.getInventory().getItemInMainHand().getItemMeta()).getDamage() != 0) {
                return;
            }

            CrystalQuestTeam team = arenaManager.getTeam(player);
            Team scoreboardTeam = arena.getTeamObject(team);

            for (OfflinePlayer pl : scoreboardTeam.getPlayers()) {
                Player target = BukkitUtil.getPlayerByName(pl.getName());

                target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 8, 5));

                for (int i = 0; i < 3; i++) {
                    Wolf w = target.getWorld().spawn(target.getLocation(), Wolf.class);
                    w.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000, 100));
                    w.playEffect(EntityEffect.WOLF_HEARTS);
                    w.playEffect(EntityEffect.WOLF_HEARTS);
                    w.remove();
                }
            }

            resetDurability(player.getInventory().getItemInMainHand(), WandType.HEAL);

            return;
        }

        /*
         * WAND: FREEZE
         */
        if (getWandType(player.getInventory().getItemInMainHand()) == WandType.FREEZE) {
            if (((Damageable)player.getInventory().getItemInMainHand().getItemMeta()).getDamage() != 0) {
                return;
            }

            for (final UUID id : arenaManager.getArena(player.getUniqueId()).getPlayers()) {
                Player pl = Bukkit.getPlayer(id);
                if (pl == player || arenaManager.getTeam(pl) == arenaManager.getTeam(player)) {
                    continue;
                }

                double pX = player.getLocation().getX();
                double pZ = player.getLocation().getZ();
                double plX = pl.getLocation().getX();
                double plZ = pl.getLocation().getZ();

                if (plX > pX - 12 && plX < pX + 12) {
                    if (plZ > pZ - 12 && plZ < pZ + 12) {
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 150, 10));
                        pl.playEffect(pl.getLocation(), Effect.EXTINGUISH, null);
                    }
                }
            }
            resetDurability(player.getInventory().getItemInMainHand(), WandType.FREEZE);

            return;
        }

        /*
         * WAND: WITHER
         */
        if (getWandType(player.getInventory().getItemInMainHand()) == WandType.WITHER) {
            if (((Damageable)player.getInventory().getItemInMainHand().getItemMeta()).getDamage() == 0) {
                WitherSkull ws = player.launchProjectile(WitherSkull.class);
                ws.setVelocity(player.getLocation().getDirection().multiply(3));

                for (UUID id : arenaManager.getArena(player.getUniqueId()).getPlayers()) {
                    Player pl = Bukkit.getPlayer(id);
                    if (pl != player && arenaManager.getTeam(pl) != arenaManager.getTeam(player)) {
                        double pX = player.getLocation().getX();
                        double pZ = player.getLocation().getZ();
                        double plX = pl.getLocation().getX();
                        double plZ = pl.getLocation().getZ();

                        if (plX > pX - 5 && plX < pX + 5) {
                            if (plZ > pZ - 5 && plZ < pZ + 5) {
                                pl.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 150, 0));
                                pl.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 150, 0));
                                pl.getWorld().playEffect(pl.getLocation(), Effect.SMOKE, 16);
                                pl.playSound(pl.getLocation(), Sound.ENTITY_WITHER_HURT, 1F, 1F);
                            }
                        }
                    }
                }

                resetDurability(player.getInventory().getItemInMainHand(), WandType.WITHER);
            }
        }
    }

    private void resetDurability(ItemStack wandItem, WandType wandType) {
        ItemMeta meta = wandItem.getItemMeta();
        ((Damageable)meta).setDamage(wandType.getDurability());
        wandItem.setItemMeta(meta);
    }
}