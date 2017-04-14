package nl.sugcube.crystalquest.inventorymenu;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author SugarCaney
 */
public class PickTeam {

    public static CrystalQuest plugin;
    public Inventory menu;

    /**
     * CONSTRUCTOR
     * Passes through the instance of the plugin.
     *
     * @param instance
     *         (CrystalQuest) The instance of the plugin.
     */
    public PickTeam(CrystalQuest instance) {
        plugin = instance;
        this.menu = Bukkit.createInventory(null, 9, "Pick Team");
    }

    /**
     * Updates all the pick-team menus.
     */
    public void updateMenus() {
        for (Arena a : plugin.getArenaManager().getArenas()) {
            this.updateMenu(a);
        }
    }

    /**
     * Updates the menu to the new amount of available teams.
     *
     * @param arena
     *         The arenas the menu is used for.
     */
    public void updateMenu(Arena arena) {
        Inventory inv = arena.getTeamMenu();
        if (inv == null) {
            return;
        }

        inv.clear();

        // Force even teams.
        if (plugin.getConfig().getBoolean("arena.force-even-teams")) {
            for (CrystalQuestTeam team : arena.getSmallestTeams()) {
                inv.addItem(getWool(team));
            }
        }
        // Pick between all teams.
        else {
            for (CrystalQuestTeam team : arena.getTeams()) {
                inv.addItem(getWool(team));
            }
        }

        ItemStack[] contents = inv.getContents();
        contents[8] = getRandom();
        inv.setContents(contents);

        for (HumanEntity hem : inv.getViewers()) {
            ((Player)hem).updateInventory();
        }
    }

    /**
     * Shows the menu to pick a team to a player
     *
     * @param player
     *         (Player) The target player.
     * @param a
     *         (int) The arenas
     */
    public void showMenu(Player player, Arena a) {
        player.openInventory(a.getTeamMenu());
    }

    /**
     * Get the item representing the Choose Random Team-item
     *
     * @return The Random-team item
     */
    public ItemStack getRandom() {
        ItemStack is = new ItemStack(Material.REDSTONE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("menu.random-team"));
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the team-selection wool.
     *
     * @param team
     *         The team to get a wool block of.
     * @return The wool beloning to the given team.
     */
    public ItemStack getWool(CrystalQuestTeam team) {
        short damageValue = team.getDataValueWool();
        ItemStack is = new ItemStack(Material.WOOL, 1, damageValue);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("menu.join") + team.toString());
        is.setItemMeta(im);
        return is;
    }

}