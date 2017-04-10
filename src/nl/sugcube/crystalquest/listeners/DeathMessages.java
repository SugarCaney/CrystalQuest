package nl.sugcube.crystalquest.listeners;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.events.PlayerEarnCrystalsEvent;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class DeathMessages implements Listener {

    public static CrystalQuest plugin;
    public static boolean fired = false;

    public DeathMessages(CrystalQuest instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = (Player)e.getEntity();
        plugin.itemHandler.cursed.remove(p);

        if (plugin.getArenaManager().isInGame(p)) {
            e.setDeathMessage(null);
            e.setDroppedExp((int)p.getExp());
            e.getDrops().clear();

            Random ran = new Random();
            int crystals = ran.nextInt(7) + 2;
            for (int i = 0; i < crystals; i++) {
                int chance = 0;
                if (plugin.ab.getAbilities().containsKey(p.getUniqueId())) {
                    if (plugin.ab.getAbilities().get(p.getUniqueId()).contains("less_death_crystals")) {
                        chance = 1;
                    }
                }

                if (ran.nextInt(3) > chance) {
                    p.getWorld().dropItem(p.getLocation(), plugin.itemHandler.getItemByName(Broadcast.get("items.crystal-shard")));
                }
                plugin.getArenaManager().getArena(p.getUniqueId()).addScore(plugin.getArenaManager().getTeam(p), -1);
            }

            ArenaManager am = plugin.getArenaManager();
            if (am.isInGame(p)) {
                LivingEntity len = p.getKiller();
                Arena a = am.getArena(p.getUniqueId());
                EntityDamageEvent damageEvent = p.getLastDamageCause();
                DamageCause cause = null;
                if (damageEvent != null) {
                    cause = damageEvent.getCause();
                }

                try {
                    if (!fired && cause != null) {
                        if (cause == DamageCause.DROWNING) {
                            a.sendDeathMessage(p, " drowned");
                        }
                        else if (cause == DamageCause.ENTITY_EXPLOSION || cause == DamageCause.BLOCK_EXPLOSION) {
                            a.sendDeathMessage(p, " exploded");
                        }
                        else if (cause == DamageCause.CONTACT) {
                            a.sendDeathMessage(p, " has been pricked to death");
                        }
                        else if (cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK) {
                            a.sendDeathMessage(p, " burnt away");
                        }
                        else if (cause == DamageCause.LAVA) {
                            a.sendDeathMessage(p, " became obsidian");
                        }
                        else if (cause == DamageCause.LIGHTNING) {
                            a.sendDeathMessage(p, " got electrocuted");
                        }
                        else if (cause == DamageCause.MAGIC) {
                            a.sendDeathMessage(p, " disappeared");
                        }
                        else if (cause == DamageCause.FALLING_BLOCK) {
                            a.sendDeathMessage(p, " has been squashed by an anvil");
                        }
                        else if (cause == DamageCause.PROJECTILE) {
                            if (damageEvent.getEntity() instanceof Player) {
                                Player shooter = (Player)p.getKiller();
                                if (shooter != null) {
                                    a.sendDeathMessage(p, shooter, "shot");

                                    if (shooter.hasPermission("crystalquest.fireworkkill") || shooter.hasPermission("crystalquest.staff")
                                            || shooter.hasPermission("crystalquest.admin")) {
                                        plugin.particleHandler.playFirework(p.getLocation(), FireworkEffect.builder()
                                                .withColor(plugin.im.getTeamColour(plugin.getArenaManager().getTeam(shooter)))
                                                .with(Type.BURST).build());
                                    }

                                    double chance = Multipliers.getMultiplier("blood",
                                            plugin.economy.getLevel(p, "blood", "crystals"), false);
                                    int multiplier = 1;

                                    if (ran.nextInt(100) <= chance * 100 && chance != 0) {
                                        multiplier = 2;
                                    }

                                    //Adds crystals to their balance
                                    int money = (int)(1 * plugin.getConfig().getDouble("shop.crystal-multiplier"));
                                    int vip = 1;
                                    if (shooter.hasPermission("crystalquest.triplecash") ||
                                            shooter.hasPermission("crystalquest.admin") ||
                                            shooter.hasPermission("crystalquest.staff")) {
                                        vip = 3;
                                    }
                                    else if (shooter.hasPermission("crystalquest.doublecash")) {
                                        vip = 2;
                                    }

                                    //Call Event
                                    int moneyEarned = money * multiplier * vip;

                                    PlayerEarnCrystalsEvent event = new PlayerEarnCrystalsEvent(shooter, a, moneyEarned);
                                    Bukkit.getPluginManager().callEvent(event);

                                    String message = plugin.economy.getCoinMessage(shooter, event.getAmount());

                                    if (!event.isCancelled()) {
                                        plugin.economy.getBalance().addCrystals(shooter, event.getAmount(), false);

                                        if (event.showMessage() && message != null) {
                                            shooter.sendMessage(message);
                                        }
                                    }
                                }
                            }
                        }
                        else if (cause == DamageCause.SUFFOCATION) {
                            a.sendDeathMessage(p, " suffocated");
                        }
                        else if (cause == DamageCause.THORNS) {
                            if (damageEvent.getEntity() instanceof Player && len != null) {
                                if (len instanceof Player) {
                                    a.sendDeathMessage(p, (Player)len, "pricked");
                                }
                            }
                        }
                        else if (cause == DamageCause.VOID) {
                            a.sendDeathMessage(p, " fell out of the world");
                        }
                        else if (cause == DamageCause.WITHER) {
                            a.sendDeathMessage(p, " withered away");
                        }
                        else if (cause == DamageCause.ENTITY_ATTACK) {
                            if (len instanceof Player) {
                                a.sendDeathMessage(p, (Player)len);

                                double chance = Multipliers.getMultiplier("blood",
                                        plugin.economy.getLevel((Player)len, "blood", "crystals"), false);
                                int multiplier = 1;

                                if (ran.nextInt(100) <= chance * 100 && chance != 0) {
                                    multiplier = 2;
                                }

                                Player shooter = (Player)len;
                                if (shooter.hasPermission("crystalquest.fireworkkill") || shooter.hasPermission("crystalquest.staff")
                                        || shooter.hasPermission("crystalquest.admin")) {
                                    plugin.particleHandler.playFirework(p.getLocation(), FireworkEffect.builder()
                                            .withColor(plugin.im.getTeamColour(plugin.getArenaManager().getTeam(shooter)))
                                            .with(Type.BURST).build());
                                }

                                //Adds crystals to their balance
                                int money = (int)(1 * plugin.getConfig().getDouble("shop.crystal-multiplier"));
                                int vip = 1;
                                if (((Player)len).hasPermission("crystalquest.triplecash") ||
                                        ((Player)len).hasPermission("crystalquest.admin") ||
                                        ((Player)len).hasPermission("crystalquest.staff")) {
                                    vip = 3;
                                }
                                else if (((Player)len).hasPermission("crystalquest.doublecash")) {
                                    vip = 2;
                                }

                                //Call event
                                Player moneyPlayer = (Player)len;
                                int moneyEarned = money * multiplier * vip;

                                PlayerEarnCrystalsEvent event = new PlayerEarnCrystalsEvent(moneyPlayer, a, moneyEarned);
                                Bukkit.getPluginManager().callEvent(event);

                                String message = plugin.economy.getCoinMessage(moneyPlayer, event.getAmount());

                                if (!event.isCancelled()) {
                                    plugin.economy.getBalance().addCrystals(moneyPlayer, event.getAmount(), false);

                                    if (event.showMessage() && message != null) {
                                        moneyPlayer.sendMessage(message);
                                    }
                                }
                            }
                            else if (len instanceof Rabbit) {
                                a.sendDeathMessage(p, " has been slain by The Killer Rabbit");
                            }
                            else {
                                a.sendDeathMessage(p, " has been slain");
                            }
                        }
                    }
                    else {
                        fired = false;
                    }
                }
                catch (Exception ex) {
                    fired = false;
                }
            }
        }
    }

}