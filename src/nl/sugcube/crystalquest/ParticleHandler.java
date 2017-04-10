package nl.sugcube.crystalquest;

import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SugarCaney
 */
public class ParticleHandler implements Runnable {

    public static CrystalQuest plugin;

    public static List<Snowball> balls = new ArrayList<>();

    public ParticleHandler(CrystalQuest instance) {
        plugin = instance;
    }

    public void run() {
        try {
            for (Snowball ball : balls) {
                FireworkEffect fe = FireworkEffect
                        .builder()
                        .withColor(
                                plugin.im.getTeamColour(plugin
                                        .getArenaManager().getTeam(
                                                (Player)ball.getShooter())))
                        .with(Type.BURST).build();
                try {
                    playFirework(ball.getLocation(), fe);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
        }
    }

    public void playFirework(Location location, FireworkEffect effect) {
        final Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fMeta = firework.getFireworkMeta();
        fMeta.addEffect(effect);
        firework.setFireworkMeta(fMeta);
        new BukkitRunnable() {
            public void run() {
                firework.detonate();
            }
        }.runTaskLater(plugin, 1);
    }

}