package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.Enumeration;
import java.util.Random;

/**
 * @author SugarCaney
 */
public class CurseListener implements Listener, Runnable {

    public CrystalQuest plugin;

    private Random ran;
    private int clean;

    public CurseListener(CrystalQuest instance) {
        plugin = instance;
        ran = new Random();
        clean = 100;
    }

    @Override
    public void run() {
        Enumeration<Entity> en = plugin.itemHandler.cursed.keys();
        while (en.hasMoreElements()) {
            Entity ent = en.nextElement();
            plugin.itemHandler.cursed.put(ent, plugin.itemHandler.cursed.get(ent) - 1);
            if (plugin.itemHandler.cursed.get(ent) <= 0) {
                plugin.itemHandler.cursed.remove(ent);

                if (ent instanceof Player) {
                    Player player = (Player)ent;
                    if (player.isOnline()) {
                        player.sendMessage(ChatColor.GRAY + "The curse has been lifted.");
                    }
                }
            }

            addRandomEffect(ent);
        }

        if (clean-- <= 0) {
            clean = 100;
            plugin.itemHandler.cursed.clear();
        }
    }

    public void addRandomEffect(Entity ent) {
        if (ent instanceof Player) {
            Player player = (Player)ent;
            if (!player.isOnline()) {
                return;
            }
        }

        if (ran.nextFloat() < 0.2) {
            if (ran.nextFloat() < 0.2) {
                Location loc = ent.getLocation().clone();
                float yaw = loc.getYaw() - 60 + 120 * ran.nextFloat();
                float pitch = loc.getPitch() - 60 + 120 * ran.nextFloat();
                loc.setYaw(yaw);
                loc.setPitch(pitch);
                ent.teleport(loc);
            }
            if (ran.nextFloat() < 0.25) {
                ent.setVelocity(ent.getVelocity().add(new Vector(0, 0.94, 0)));
            }
            else if (ran.nextFloat() < 0.33) {
                ent.setFireTicks((int)(45 * ran.nextFloat()) + 25);
            }
            else if (ran.nextBoolean()) {
                double x = ent.getVelocity().getX() - 5 + 10 * ran.nextDouble();
                double z = ent.getVelocity().getZ() - 5 + 10 * ran.nextDouble();
                Vector vec = ent.getVelocity().clone().add(new Vector(x, 0.2, z));
                ent.setVelocity(vec);
            }
            else {
                double x = ent.getLocation().getX();
                double y = ent.getLocation().getY() + 0.5;
                double z = ent.getLocation().getZ();
                ent.getWorld().createExplosion(x, y, z, 0.8f, false, false);
            }
        }
    }

}
