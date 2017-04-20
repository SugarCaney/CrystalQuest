package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import nl.sugcube.crystalquest.listeners.DeathMessages;
import nl.sugcube.crystalquest.util.ChangeMarker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class ItemListener implements Listener {

    public static CrystalQuest plugin;

    public ItemListener(CrystalQuest instance) {
        plugin = instance;
        Random ran = new Random();
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Fireball && e.getEntity() instanceof LivingEntity) {
            LivingEntity lentity = (LivingEntity)e.getEntity();
            Fireball ball = (Fireball)e.getDamager();
            if (ball.getShooter() instanceof Player) {
                Player shooter = (Player)ball.getShooter();
                if (plugin.getArenaManager().isInGame(shooter)) {
                    Arena a = plugin.getArenaManager().getArena(shooter.getUniqueId());
                    if (lentity instanceof Player) {
                        Player target = (Player)lentity;
                        if (a.getTeam(target) == a.getTeam(shooter)) {
                            e.setCancelled(true);
                        }
                        else {
                            target.setFireTicks(150);
                        }
                    }
                    else {
                        lentity.setFireTicks(150);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (!plugin.getArenaManager().isInGame(p)) {
            return;
        }

        if (plugin.getArenaManager().getArena(p.getUniqueId()).getSpectators().contains(p.getUniqueId())) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getInventory().getItemInMainHand() == null) {
                return;
            }

            if (plugin.getArenaManager().getArena(p.getUniqueId()).isEndGame()) {
                return;
            }

            // Check executors
            ItemStack clickedItem = p.getInventory().getItemInMainHand();
            Material clickedMaterial = clickedItem.getType();

            ChangeMarker executed = new ChangeMarker();
            plugin.itemHandler.getExecutors().stream()
                    // Only handle actions with corresponding trigger material.
                    .filter(exe -> exe.getTriggerMaterial().equals(clickedMaterial) &&
                            exe.getTriggerDurability() == clickedItem.getDurability())
                    // Execute all actions.
                    .forEach(exe -> {
                        // Execute
                        if (!exe.execute(plugin, p, clickedItem)) {
                            return;
                        }

                        exe.removeItem(plugin, p, clickedItem);
                        executed.markChange();
                    });

            // When an action has been executed: cancel the event.
            if (executed.isChanged()) {
                e.setCancelled(true);
            }
        }
        /*
         * CHECKS FOR LANDMINES
         */
        else if (e.getAction() == Action.PHYSICAL) {
            if (e.getClickedBlock().getType() == Material.STONE_PLATE) {
                if (!plugin.getArenaManager().getArena(p.getUniqueId()).getSpectators().contains(p.getUniqueId())) {
                    final Block b = e.getClickedBlock();
                    b.setType(Material.AIR);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            b.setType(Material.AIR);
                        }
                    });

                    Location loc = p.getLocation();
                    b.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 2.0F, false, false);
                    Arena a = plugin.getArenaManager().getArena(p.getUniqueId());
                    if (a != null) {
                        a.getGameBlocks().remove(b);
                        DeathMessages.fired = true;
                        a.sendDeathMessage(p, " stood on a landmine");
                        if (p.getHealth() > 0) {
                            p.setHealth(0);
                        }

                        if (a.getLandmines().containsKey(b.getLocation())) {
                            Player killer = Bukkit.getPlayer(a.getLandmines().get(b.getLocation()));
                            a.getLandmines().remove(b.getLocation());

                            if (killer != null && !a.isEndGame() && killer != p) {
                                Random ran = new Random();
                                double chance = Multipliers.getMultiplier("blood",
                                        plugin.economy.getLevel(killer, "blood", "crystals"), false);
                                int multiplier = 1;

                                if (ran.nextInt(100) <= chance * 100 && chance != 0) {
                                    multiplier = 2;
                                }

                                //Adds crystals to their balance
                                int money = (int)(1 * plugin.getConfig().getDouble("shop.crystal-multiplier"));
                                int vip = 1;
                                if (killer.hasPermission("crystalquest.triplecash") ||
                                        killer.hasPermission("crystalquest.admin") ||
                                        killer.hasPermission("crystalquest.staff")) {
                                    vip = 3;
                                }
                                else if (killer.hasPermission("crystalquest.doublecash")) {
                                    vip = 2;
                                }
                                plugin.economy.getBalance().addCrystals(killer, money * multiplier * vip, false);
                                String message = plugin.economy.getCoinMessage(killer, money * multiplier * vip);
                                if (message != null) {
                                    killer.sendMessage(message);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAnvilFall(EntityChangeBlockEvent event) {
        if (event.getEntityType().equals(EntityType.FALLING_BLOCK)) {
            FallingBlock fb = (FallingBlock)event.getEntity();
            if (!event.getBlock().getLocation().add(0, -0.01, 0).getBlock().getType().equals(Material.AIR)) {
                if (fb.getMaterial().equals(Material.ANVIL)) {
                    if (plugin.prot.isInProtectedArena(fb.getLocation())) {
                        fb.getLocation().getWorld().playSound(fb.getLocation(), Sound.BLOCK_ANVIL_LAND,
                                2F, 2F);
                        fb.remove();
                        event.setCancelled(true);
                    }
                }
                else if (fb.getMaterial().equals(Material.LAVA)) {
                    if (plugin.prot.isInProtectedArena(fb.getLocation())) {
                        fb.remove();
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (plugin.getArenaManager().isInGame(e.getPlayer())) {
            if (!plugin.getArenaManager().getArena(e.getPlayer().getUniqueId()).getSpectators().contains(e.getPlayer().getUniqueId())) {
                Player player = e.getPlayer();
                Arena a = plugin.getArenaManager().getArena(player.getUniqueId());
                ItemStack is = e.getItem().getItemStack();
                if (is.hasItemMeta()) {
                    if (is.getItemMeta().hasDisplayName()) {
                        ItemMeta im = is.getItemMeta();
                        String name = im.getDisplayName();
                        Random ran = new Random();

						/*
                         * GRENADES-RAILGUN-HAMMER-LANDMINE-ANVIL-TNT
						 */
                        if (name.contains(Broadcast.get("items.grenade")) || name.contains(Broadcast.get("items.railgun")) ||
                                name.contains(Broadcast.get("items.landmine")) || name.contains(Broadcast.get("items.hammer")) ||
                                name.contains(Broadcast.get("items.tnt")) || name.contains(Broadcast.get("items.anvil")) ||
                                name.contains(Broadcast.get("items.poison-dart"))) {
                            double chance = Multipliers.getMultiplier("ammo",
                                    plugin.economy.getLevel(player, "weaponry", "upgrade"), false);
                            if (chance > 0) {
                                int random = ran.nextInt((int)(10 - (chance * 10)));
                                if (random <= chance * 10) {
                                    e.setCancelled(true);
                                    e.getItem().remove();
                                    if (is.getType() == Material.DIAMOND_AXE) {
                                        ItemStack newIs = new ItemStack(is.getType(), 1, (short)(1561 - (ran.nextInt(3) + 1)));
                                        newIs.addUnsafeEnchantments(is.getEnchantments());
                                        ItemMeta itemM = newIs.getItemMeta();
                                        itemM.setDisplayName(Broadcast.get("items.hammer"));
                                        newIs.setItemMeta(itemM);
                                        player.getInventory().addItem(newIs);
                                    }
                                    else {
                                        ItemStack newIs = new ItemStack(is.getType(), is.getAmount() * 2);
                                        if (newIs.getType() == Material.EGG) {
                                            ItemMeta im1 = newIs.getItemMeta();
                                            im1.setDisplayName(Broadcast.get("items.grenade"));
                                            newIs.setItemMeta(im1);
                                        }
                                        else if (newIs.getType() == Material.IRON_HOE) {
                                            ItemMeta im1 = newIs.getItemMeta();
                                            im1.setDisplayName(Broadcast.get("items.railgun"));
                                            newIs.setItemMeta(im1);
                                        }
                                        else if (newIs.getType() == Material.STONE_PLATE) {
                                            ItemMeta im1 = newIs.getItemMeta();
                                            im1.setDisplayName(Broadcast.get("items.landmine"));
                                            newIs.setItemMeta(im1);
                                        }
                                        else if (newIs.getType() == Material.TNT) {
                                            ItemMeta im1 = newIs.getItemMeta();
                                            im1.setDisplayName(Broadcast.get("items.tnt"));
                                            newIs.setItemMeta(im1);
                                        }
                                        else if (newIs.getType() == Material.ANVIL) {
                                            ItemMeta im1 = newIs.getItemMeta();
                                            im1.setDisplayName(Broadcast.get("items.anvil"));
                                            newIs.setItemMeta(im1);
                                        }
                                        else if (newIs.getType() == Material.SUGAR_CANE) {
                                            ItemMeta im1 = newIs.getItemMeta();
                                            im1.setDisplayName(Broadcast.get("items.poison-dart"));
                                            newIs.setItemMeta(im1);
                                        }
                                        player.getInventory().addItem(newIs);
                                    }
                                }
                            }
                        }
                        /*
                         * SPEED POTION
						 */
                        if (name.contains(Broadcast.get("items.speed"))) {
                            double multiplier = Multipliers.getMultiplier("buff",
                                    plugin.economy.getLevel(player, "buff", "upgrade"), false);
                            PotionMeta pm = (PotionMeta)is.getItemMeta();
                            PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, (int)(360 * multiplier), 1);
                            pm.removeCustomEffect(PotionEffectType.SPEED);
                            pm.addCustomEffect(effect, true);
                            is.setItemMeta(pm);
                        }
                        /*
                         * STRENGTH POTION
						 */
                        if (name.contains(Broadcast.get("items.strength"))) {
                            double multiplier = Multipliers.getMultiplier("buff",
                                    plugin.economy.getLevel(player, "buff", "upgrade"), false);
                            PotionMeta pm = (PotionMeta)is.getItemMeta();
                            PotionEffect effect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int)(240 * multiplier), 1);
                            pm.removeCustomEffect(PotionEffectType.INCREASE_DAMAGE);
                            pm.addCustomEffect(effect, true);
                            is.setItemMeta(pm);
                        }
                        /*
                         * STRENGTH POTION
						 */
                        if (name.contains(Broadcast.get("items.health"))) {
                            int lvl = plugin.economy.getLevel(player, "buff", "upgrade");
                            PotionMeta pm = (PotionMeta)is.getItemMeta();
                            PotionEffect effect = new PotionEffect(PotionEffectType.HEAL, 1, lvl);
                            pm.removeCustomEffect(PotionEffectType.HEAL);
                            pm.addCustomEffect(effect, true);
                            is.setItemMeta(pm);
                        }
                        /*
                         * CRYSTAL SHARD
						 */
                        if (name.contains(Broadcast.get("items.crystal-shard"))) {
                            CrystalQuestTeam team = plugin.getArenaManager().getTeam(player);
                            a.setScore(team, a.getScore(team) + e.getItem().getItemStack().getAmount());
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 20, 20);
                        }
                        /*
                         * SMALL CRYSTAL
						 */
                        else if (name.contains(Broadcast.get("items.small-crystal"))) {
                            CrystalQuestTeam team = plugin.getArenaManager().getTeam(player);
                            a.setScore(team, a.getScore(team) + e.getItem().getItemStack().getAmount() * 2);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 20, 20);
                        }
                        /*
                         * SHINY CRYSTAL
						 */
                        else if (name.contains(Broadcast.get("items.shiny-crystal"))) {
                            CrystalQuestTeam team = plugin.getArenaManager().getTeam(player);
                            a.setScore(team, a.getScore(team) + e.getItem().getItemStack().getAmount() * 3);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 20, 20);
                        }
                        /*
                         * SHIELD
						 */
                        else if (name.contains(Broadcast.get("items.shield"))) {
                            double multiplier = Multipliers.getMultiplier("buff",
                                    plugin.economy.getLevel(player, "buff", "upgrade"), false);
                            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int)(618 * multiplier),
                                    0));
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 3L, 3L);
                            e.getItem().remove();
                            e.setCancelled(true);
                        }
                        else {
                            e.getItem().remove();
                        }
                    }
                    else {
                        e.setCancelled(true);
                        e.getItem().remove();
                    }
                }
                else {
                    e.setCancelled(true);
                    e.getItem().remove();
                }
            }
        }
    }
}