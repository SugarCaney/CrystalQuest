package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class CrystalSpawner implements Runnable {

    public static CrystalQuest plugin;

    Random ran = new Random();

    public CrystalSpawner(CrystalQuest instance) {
        plugin = instance;
    }

    /*
     *	Ticks every 2 gameticks
     */
    public void run() {
        for (Arena a : plugin.getArenaManager().getArenas()) {
            if (!a.isInGame()) {
                continue;
            }

            if (a.getTimeLeft() <= 0) {
                continue;
            }

            for (Location loc : a.getCrystalSpawns()) {
                if (a.isEndGame() || a.getGameCrystalMap().containsValue(loc)) {
                    continue;
                }

                int chance = plugin.getConfig().getInt("arena.crystal-spawn-chance");
                if (ran.nextInt(chance * 10) == 0) {
                    try {
                        EnderCrystal ec = (EnderCrystal)Bukkit.getPlayer(a.getPlayers().get(0))
                                .getWorld()
                                .spawnEntity(loc.add(0, 0.05, 0), EntityType.ENDER_CRYSTAL);
                        ec.setShowingBottom(false);
                        a.getGameCrystals().add(ec);
                        a.getGameCrystalMap().put(ec, loc);
                    }
                    catch (Exception ignored) {
                    }
                }
            }
        }
    }
}