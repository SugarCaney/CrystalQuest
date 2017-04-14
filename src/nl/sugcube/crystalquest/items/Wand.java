package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onWandUse(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (plugin.getArenaManager().isInGame(e.getPlayer())) {
                Player p = e.getPlayer();
                if (p.getInventory().getItemInMainHand() != null) {
                    /*
                     * WAND: MAGMA
					 */
                    if (getWandType(p.getInventory().getItemInMainHand()) == WandType.MAGMA) {
                        if (p.getInventory().getItemInMainHand().getDurability() == (short)0) {
                            Fireball fb = p.launchProjectile(Fireball.class);
                            fb.setVelocity(p.getLocation().getDirection().multiply(3));

                            for (UUID id : plugin.getArenaManager().getArena(p.getUniqueId()).getPlayers()) {
                                Player pl = Bukkit.getPlayer(id);
                                if (pl != p && plugin.getArenaManager().getTeam(pl) != plugin.getArenaManager().getTeam(p)) {
                                    double pX = p.getLocation().getX();
                                    double pZ = p.getLocation().getZ();
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

                            p.getInventory().getItemInMainHand().setDurability(WandType.MAGMA.getDurability());
                        }
                    }
                    /*
                     * WAND: TELEPORT
					 */
                    else if (getWandType(p.getInventory().getItemInMainHand()) == WandType.TELEPORT) {
                        if (p.getInventory().getItemInMainHand().getDurability() == (short)0) {
                            Set<Material> targetSet = null;
                            Location loc = p.getTargetBlock(targetSet, 64).getLocation().add(0, 1, 0);
                            if (loc.getBlock().getType() == Material.AIR) {
                                if (plugin.prot.isInProtectedArena(loc)) {
                                    boolean isInBounds = false;
                                    double dX = p.getLocation().getX() - loc.getX();
                                    double dY = p.getLocation().getY() - loc.getY();
                                    double dZ = p.getLocation().getZ() - loc.getZ();

                                    if (Math.sqrt(dX * dX + dY * dY + dZ * dZ) <= 40) {
                                        isInBounds = true;
                                    }

                                    if (isInBounds) {
                                        float yaw = p.getLocation().getYaw();
                                        p.playEffect(p.getLocation(), Effect.ENDER_SIGNAL, null);
                                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
                                        loc.setYaw(yaw);
                                        p.teleport(loc);
                                        p.playEffect(p.getLocation(), Effect.ENDER_SIGNAL, null);
                                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
                                        p.getInventory().getItemInMainHand().setDurability(WandType.TELEPORT.getDurability());
                                    }
                                }
                            }
                        }
                    }
                    /*
					 * WAND: HEAL
					 */
                    else if (getWandType(p.getInventory().getItemInMainHand()) == WandType.HEAL) {
                        if (p.getInventory().getItemInMainHand().getDurability() == (short)0) {
                            CrystalQuestTeam team = plugin.getArenaManager().getTeam(p);
                            for (OfflinePlayer pl : plugin.getArenaManager().getArena(p
                                    .getUniqueId()).getScoreboardTeams()[team.getId()].getPlayers()) {
                                Player player = Bukkit.getPlayer(pl.getName());

                                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 8, 5));

                                for (int i = 0; i < 3; i++) {
                                    Wolf w = player.getWorld().spawn(player.getLocation(), Wolf.class);
                                    w.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000, 100));
                                    w.playEffect(EntityEffect.WOLF_HEARTS);
                                    w.playEffect(EntityEffect.WOLF_HEARTS);
                                    w.remove();
                                }
                            }

                            p.getInventory().getItemInMainHand().setDurability(WandType.HEAL.getDurability());
                        }
                    }
					/*
					 * WAND: FREEZE
					 */
                    else if (getWandType(p.getInventory().getItemInMainHand()) == WandType.FREEZE) {
                        if (p.getInventory().getItemInMainHand().getDurability() == (short)0) {
                            for (final UUID id : plugin.getArenaManager().getArena(p.getUniqueId()).getPlayers()) {
                                Player pl = Bukkit.getPlayer(id);
                                if (pl != p && plugin.getArenaManager().getTeam(pl) != plugin.getArenaManager().getTeam(p)) {
                                    double pX = p.getLocation().getX();
                                    double pZ = p.getLocation().getZ();
                                    double plX = pl.getLocation().getX();
                                    double plZ = pl.getLocation().getZ();

                                    if (plX > pX - 12 && plX < pX + 12) {
                                        if (plZ > pZ - 12 && plZ < pZ + 12) {
                                            pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 150, 10));
                                            pl.playEffect(pl.getLocation(), Effect.EXTINGUISH, null);
                                        }
                                    }
                                }
                            }
                            p.getInventory().getItemInMainHand().setDurability(WandType.FREEZE.getDurability());
                        }
                    }
					/*
					 * WAND: WITHER
					 */
                    else if (getWandType(p.getInventory().getItemInMainHand()) == WandType.WITHER) {
                        if (p.getInventory().getItemInMainHand().getDurability() == (short)0) {
                            WitherSkull ws = p.launchProjectile(WitherSkull.class);
                            ws.setVelocity(p.getLocation().getDirection().multiply(3));

                            for (UUID id : plugin.getArenaManager().getArena(p.getUniqueId()).getPlayers()) {
                                Player pl = Bukkit.getPlayer(id);
                                if (pl != p && plugin.getArenaManager().getTeam(pl) != plugin.getArenaManager().getTeam(p)) {
                                    double pX = p.getLocation().getX();
                                    double pZ = p.getLocation().getZ();
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

                            p.getInventory().getItemInMainHand().setDurability(WandType.WITHER.getDurability());
                        }
                    }
                }
            }
        }
    }

}
