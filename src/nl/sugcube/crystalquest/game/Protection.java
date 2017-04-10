package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author SugarCaney
 */
public class Protection implements Listener {

    public static CrystalQuest plugin;
    public static ArenaManager am;

    public Location pos1;
    public Location pos2;

    public Protection(CrystalQuest instance) {
        plugin = instance;
        am = plugin.getArenaManager();
    }

    /**
     * Checks if the given location is protected
     *
     * @param loc
     *         (Location) The location to check for
     * @return (boolean) True if within, false if not
     */
    public boolean isInProtectedArena(Location loc) {
        for (Arena a : am.getArenas()) {
            if (a.getProtection() != null) {
                if (a.getProtection()[0] != null && a.getProtection()[1] != null) {
                    Location[] prot = a.getProtection();
                    double p1X = prot[0].getX();
                    double p1Y = prot[0].getY();
                    double p1Z = prot[0].getZ();
                    double p2X = prot[1].getX();
                    double p2Y = prot[1].getY();
                    double p2Z = prot[1].getZ();

                    if (!prot[0].getWorld().equals(loc.getWorld())) {
                        return false;
                    }

                    if ((loc.getX() + 1 >= p1X && loc.getX() - 1 <= p2X) || (loc.getX() - 1 <= p1X && loc.getX() + 1 >= p2X)) {
                        if ((loc.getY() + 1 >= p1Y && loc.getY() - 1 <= p2Y) || (loc.getY() - 1 <= p1Y && loc.getY() + 1 >= p2Y)) {
                            if ((loc.getZ() + 1 >= p1Z && loc.getZ() - 1 <= p2Z) || (loc.getZ() - 1 <= p1Z && loc.getZ() + 1 >= p2Z)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean protectArena(Arena a) {
        if (this.pos1 == null || this.pos2 == null) {
            return false;
        }
        else {
            Location[] locs = new Location[2];
            locs[0] = pos1;
            locs[1] = pos2;
            a.setProtection(locs);
            return true;
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed)e.getEntity();
            if (isInProtectedArena(tnt.getLocation())) {
                e.setCancelled(true);
                Location l = e.getLocation();
                tnt.getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), 5.0F, false, false);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("crystalquest.admin")) {
            return;
        }
        Block b = e.getBlock();
        if (isInProtectedArena(b.getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("crystalquest.admin")) {
            return;
        }
        Block b = e.getBlock();
        if (isInProtectedArena(b.getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        if (isInProtectedArena(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (isInProtectedArena(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        if (isInProtectedArena(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getInventory().getItemInMainHand() != null) {
            if (p.getInventory().getItemInMainHand().getType() == Material.STICK) {
                ItemStack item = p.getInventory().getItemInMainHand();
                if (item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta.hasDisplayName()) {
                        if (meta.getDisplayName().contains(Broadcast.TAG + "Wand")) {
                            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                                this.pos1 = p.getLocation();
                                p.sendMessage(Broadcast.TAG + Broadcast.get("commands.pos-set")
                                        .replace("%pos%", "1")
                                        .replace("%coords%", String.format("(%.1f, %.1f, %.1f)",
                                                plugin.prot.pos1.getX(),
                                                plugin.prot.pos1.getY(),
                                                plugin.prot.pos1.getZ()
                                        )));
                            }
                            else {
                                this.pos2 = p.getLocation();
                                p.sendMessage(Broadcast.TAG + Broadcast.get("commands.pos-set")
                                        .replace("%pos%", "2")
                                        .replace("%coords%", String.format("(%.1f, %.1f, %.1f)",
                                                plugin.prot.pos2.getX(),
                                                plugin.prot.pos2.getY(),
                                                plugin.prot.pos2.getZ()
                                        )));
                            }
                        }
                    }
                }
            }
        }
    }
}
