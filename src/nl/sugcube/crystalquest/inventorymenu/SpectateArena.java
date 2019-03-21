package nl.sugcube.crystalquest.inventorymenu;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SugarCaney
 */
public class SpectateArena {

    public static CrystalQuest plugin;
    private Inventory menu;

    public SpectateArena(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Opens the spectate-menu
     *
     * @param p
     *         (Player) The player to show the menu to
     */
    public void showMenu(Player p) {
        this.updateMenu();
        p.openInventory(menu);
    }

    /**
     * Updates the menu
     */
    public void updateMenu() {
        ArenaManager am = plugin.getArenaManager();
        int arenas = am.getArenas().size();

        if (menu == null) {
            menu = Bukkit.createInventory(null, 9, "Spectate an arenas");
        }

        if (arenas > menu.getSize()) {
            int invSize = 9;
            for (int i = 54; i >= 9; i -= 9) {
                if (arenas <= i) {
                    invSize = i;
                }
            }

            if (menu != null) {
                if (menu.getSize() < arenas) {
                    menu = Bukkit.createInventory(null, invSize, "Spectate an arenas");
                }
            }
            else {
                menu = Bukkit.createInventory(null, invSize, "Spectate an arenas");
            }
        }

        menu.clear();
        for (Arena a : am.getArenas()) {
            if (a.isEnabled()) {
                menu.addItem(this.getItem(a));
            }
        }
    }

    /**
     * Gives you the item stack representing the arenas.
     *
     * @param a
     *         (Arena) The arenas to retrieve the data from.
     * @return (ItemStack) The wool representing the arenas.
     */
    public ItemStack getItem(Arena a) {
        ItemStack is;
        String status;

        if (a.isEndGame()) {
            is = new ItemStack(Material.MAGENTA_WOOL, 1);
            status = ChatColor.DARK_PURPLE + "Restarting";
        }
        else if (a.isInGame()) {
            is = new ItemStack(Material.RED_WOOL, 1);
            status = ChatColor.DARK_RED + "In Game";
        }
        else if (a.isCounting()) {
            is = new ItemStack(Material.ORANGE_WOOL, 1);
            status = ChatColor.GOLD + "Starting";
        }
        else {
            is = new ItemStack(Material.LIME_WOOL, 1);
            status = ChatColor.GREEN + "In Lobby";
        }

        String displayName = ChatColor.AQUA + "Spectate " + a.getName();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Currently " + status);
        lore.add(ChatColor.YELLOW + "Players: " + a.getPlayers().size() + "/" + a.getMaxPlayers());

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(displayName);
        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }
}
