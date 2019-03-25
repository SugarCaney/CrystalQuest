package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class ItemSpawner implements Runnable {

    public static CrystalQuest plugin;
    Random ran = new Random();

    public ItemSpawner(CrystalQuest instance) {
        plugin = instance;
    }

    public void run() {
        for (Arena a : plugin.getArenaManager().getArenas()) {
            if (!a.isInGame()) {
                continue;
            }

            if (a.isEndGame() || a.getTimeLeft() <= 0) {
                continue;
            }

            for (Location loc : a.getItemSpawns()) {
                if (ran.nextInt(10 * plugin.getConfig().getInt("arena.item-spawn-chance")) == 0) {
                    loc.getWorld().dropItemNaturally(loc, plugin.itemHandler.getRandomItemStack());
                }

                if (ran.nextInt(15 * plugin.getConfig().getInt("arena.item-spawn-chance")) == 0) {
                    for (int i = 0; i <= ran.nextInt(3); i++) {
                        ExperienceOrb orb = loc.getWorld().spawn(loc.add(0, 0.2, 0), ExperienceOrb.class);
                        orb.setExperience(ran.nextInt(5) + 1);
                    }
                }
            }
        }
    }
}