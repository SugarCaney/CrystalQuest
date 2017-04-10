package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.Teams;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.events.ArenaTickEvent;
import nl.sugcube.crystalquest.items.WandType;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;
import java.util.UUID;

/**
 * @author SugarCaney
 */
public class GameLoop implements Runnable {

    public CrystalQuest plugin;
    public ArenaManager am;
    public Random ran;
    private String winningTeam;

    public GameLoop(CrystalQuest instance, ArenaManager arenaManager) {
        this.plugin = instance;
        this.am = arenaManager;
        this.ran = new Random();
    }

    public void run() {

        for (Arena a : am.arena) {
            if (a.isEnabled()) {

                if (a.isCounting()) {
                    /*
                     * Arena Countdown
					 */
                    ArenaTickEvent event = new ArenaTickEvent(a, a.getCountdown(), a.getCountdown() - 1, false);
                    Bukkit.getPluginManager().callEvent(event);

                    for (UUID id : a.getPlayers()) {
                        Player pl = Bukkit.getPlayer(id);
                        pl.setLevel(a.getCountdown());
                    }

                    if (a.getCountdown() == 120) {
                        for (UUID id : a.getPlayers()) {
                            Player pl = Bukkit.getPlayer(id);
                            pl.sendMessage(Broadcast.TAG + Broadcast.get("arena.start")
                                    .replace("%time%", "2 " + Broadcast.get("arena.minutes")));
                        }
                    }
                    else if (a.getCountdown() == 60) {
                        for (UUID id : a.getPlayers()) {
                            Player pl = Bukkit.getPlayer(id);
                            pl.sendMessage(Broadcast.TAG + Broadcast.get("arena.start")
                                    .replace("%time%", "1 " + Broadcast.get("arena.minute")));
                        }
                    }
                    else if (a.getCountdown() == 30) {
                        for (UUID id : a.getPlayers()) {
                            Player pl = Bukkit.getPlayer(id);
                            pl.sendMessage(Broadcast.TAG + Broadcast.get("arena.start")
                                    .replace("%time%", "30 " + Broadcast.get("arena.seconds")));
                        }
                    }
                    else if (a.getCountdown() == 10) {
                        for (UUID id : a.getPlayers()) {
                            Player pl = Bukkit.getPlayer(id);
                            pl.sendMessage(Broadcast.TAG + Broadcast.get("arena.start")
                                    .replace("%time%", "10 " + Broadcast.get("arena.seconds")));
                        }
                    }
                    else if (a.getCountdown() <= 5 && a.getCountdown() > 0) {
                        for (UUID id : a.getPlayers()) {
                            Player pl = Bukkit.getPlayer(id);
                            pl.sendMessage(Broadcast.TAG + Broadcast.get("arena.start")
                                    .replace("%time%", a.getCountdown() + " " + Broadcast.get("arena.seconds")));
                            pl.playSound(pl.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 20F, 20F);
                        }
                    }
                    else if (a.getCountdown() <= 0) {
                        a.setIsCounting(false);
                        a.setCountdown(plugin.getConfig().getInt("arena.countdown") + 1);
                        a.startGame();
                    }

                    a.setCountdown(a.getCountdown() - 1);
                }
                else if (a.isInGame() && !a.isEndGame()) {

				/*
                 * GAME LOOP DURING THE GAME
				 */
                    if (a.getTimeLeft() > 0) {

                        ArenaTickEvent event = new ArenaTickEvent(a, a.getTimeLeft(), a.getTimeLeft() - 1, true);
                        Bukkit.getPluginManager().callEvent(event);

                        for (UUID id : a.getPlayers()) {
                            Player p = Bukkit.getPlayer(id);
                            if (a.getTimeLeft() % 10 == 0) {
                                p.setFoodLevel(20);
                                p.setSaturation(4);
                            }

                            for (ItemStack is : p.getInventory().getContents()) {
                                if (is != null) {
                                    if (is.getType() == Material.GLASS_BOTTLE) {
                                        p.getInventory().remove(is);
                                    }
                                    if (is.hasItemMeta()) {
                                        if (is.getItemMeta().hasDisplayName()) {
                                            if (is.getItemMeta().getDisplayName()
                                                    .equalsIgnoreCase(Broadcast.get("items.crystal-shard"))) {
                                                p.getInventory().remove(is);
                                            }
                                            else if (is.getItemMeta().getDisplayName()
                                                    .equalsIgnoreCase(Broadcast.get("items.small-crystal"))) {
                                                p.getInventory().remove(is);
                                            }
                                            else if (is.getItemMeta().getDisplayName()
                                                    .equalsIgnoreCase(Broadcast.get("items.shiny-crystal"))) {
                                                p.getInventory().remove(is);
                                            }
                                        }
                                    }
                                }
                            }

                            if (p.getLevel() > 0) {
                                int extraPoints = (int)Multipliers.getMultiplier("xp",
                                        plugin.economy.getLevel(p, "xp", "crystals"), false) - 1;

                                a.addScore(plugin.getArenaManager().getTeam(p), p.getLevel() + extraPoints);
                                p.setLevel(0);
                            }

                            for (ItemStack is : p.getInventory().getContents()) {
                                if (plugin.wand.getWandType(is) != null) {
                                    if (is.getDurability() != 0) {
                                        double multiplier = 1;
                                        if (plugin.ab.getAbilities().containsKey(p.getUniqueId())) {
                                            if (plugin.ab.getAbilities().get(p.getUniqueId()).contains("magical_aura")) {
                                                multiplier = 2.1;
                                            }
                                            else if (plugin.ab.getAbilities().get(p.getUniqueId()).contains("power_loss")) {
                                                multiplier = 0.6;
                                            }
                                        }
                                        else {
                                            multiplier = 1;
                                        }

                                        WandType type = plugin.wand.getWandType(is);
                                        short addedDura = (short)(type.getDurability() / plugin.getConfig().getInt(type.getRegenConfig()) * multiplier);
                                        short newDura;
                                        if (is.getDurability() - addedDura < 0) {
                                            newDura = 0;
                                        }
                                        else {
                                            newDura = (short)(is.getDurability() - addedDura);
                                        }
                                        is.setDurability(newDura);
                                    }
                                }
                            }
                        }

                        a.setTimeLeft(a.getTimeLeft() - 1);
                        a.updateTimer();
                    }
                    else {
                        try {
                            this.winningTeam = a.declareWinner();
                            a.setAfterCount(plugin.getConfig().getInt("arena.after-count"));
                            a.setEndGame(true);
                            a.setIsCounting(false);
                            a.setTimeLeft(plugin.getConfig().getInt("arena.game-length"));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

				/*
                 * AFTER GAME
				 */
                if (a.isEndGame()) {
                    if (a.getAfterCount() <= 0) {
                        a.setEndGame(false);
                        a.resetArena(false);
                        plugin.signHandler.updateSigns();
                    }
                    else {
                        a.setAfterCount(a.getAfterCount() - 1);

                        for (UUID id : a.getPlayers()) {
                            Player p = Bukkit.getPlayer(id);
                            try {
                                if (!a.getSpectators().contains(p.getUniqueId())) {
                                    if (a.getTeam(p) == Teams.getTeamIdFromNAME(this.winningTeam)) {
                                        Firework f = p.getLocation().getWorld().spawn(p.getLocation().add(0, 2, 0), Firework.class);
                                        FireworkMeta fm = f.getFireworkMeta();
                                        fm.setPower(1);
                                        FireworkEffect fe = FireworkEffect.builder()
                                                .flicker(true)
                                                .withColor(plugin.im.getTeamColour(a.getTeam(p)))
                                                .with(Type.STAR)
                                                .build();
                                        fm.clearEffects();
                                        fm.addEffect(fe);
                                        f.setFireworkMeta(fm);
                                    }
                                }
                            }
                            catch (Exception ignored) {
                            }
                        }
                    }
                }
            }
        }
    }
}