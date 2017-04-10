package nl.sugcube.crystalquest.inventorymenu;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.Teams;
import nl.sugcube.crystalquest.game.Arena;
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
     * @param a
     *         (Arena) The arena the menu is used for.
     */
    @SuppressWarnings("deprecation")
    public void updateMenu(Arena a) {
        Inventory inv = a.getTeamMenu();
        if (inv == null) {
            return;
        }

        inv.clear();

        if (plugin.getConfig().getBoolean("arena.force-even-teams")) {
            for (int i : a.getSmallestTeams()) {
                inv.addItem(this.getWool(i));
            }
        }
        else {
            for (int i = 0; i < a.getTeamCount(); i++) {
                inv.addItem(this.getWool(i));
            }
        }

        ItemStack[] contents = inv.getContents();
        contents[8] = getRandom(a.getId());
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
     *         (int) The arena
     */
    public void showMenu(Player player, Arena a) {
        player.openInventory(a.getTeamMenu());
    }

    /**
     * Get the item representing the Choose Random Team-item
     *
     * @param arenaId
     *         (int) The ID of the arena.
     * @return (ItemStack) The Random-team item
     */
    public ItemStack getRandom(int arenaId) {
        ItemStack is = new ItemStack(Material.REDSTONE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("menu.random-team"));
        is.setItemMeta(im);
        return is;
    }

    /**
     * Gets the team-selection wool
     *
     * @param teamId
     *         (int) The ID of the team's wool.
     * @return (ItemStack) The wool beloning to the given team.
     */
    public ItemStack getWool(int teamId) {
        short damageValue = 0;

        switch (teamId) {
            case 0:
                damageValue = 5;
                break;
            case 1:
                damageValue = 1;
                break;
            case 2:
                damageValue = 4;
                break;
            case 3:
                damageValue = 14;
                break;
            case 4:
                damageValue = 3;
                break;
            case 5:
                damageValue = 2;
                break;
            case 6:
                damageValue = 0;
                break;
            case 7:
                damageValue = 15;
                break;
        }

        ItemStack is = new ItemStack(Material.WOOL, 1, damageValue);
        ItemMeta im = is.getItemMeta();

        switch (damageValue) {
            case 5:
                im.setDisplayName(Broadcast.get("menu.join") + Teams.GREEN_NAME);
                break;
            case 1:
                im.setDisplayName(Broadcast.get("menu.join") + Teams.ORANGE_NAME);
                break;
            case 4:
                im.setDisplayName(Broadcast.get("menu.join") + Teams.YELLOW_NAME);
                break;
            case 14:
                im.setDisplayName(Broadcast.get("menu.join") + Teams.RED_NAME);
                break;
            case 3:
                im.setDisplayName(Broadcast.get("menu.join") + Teams.BLUE_NAME);
                break;
            case 2:
                im.setDisplayName(Broadcast.get("menu.join") + Teams.MAGENTA_NAME);
                break;
            case 0:
                im.setDisplayName(Broadcast.get("menu.join") + Teams.WHITE_NAME);
                break;
            case 15:
                im.setDisplayName(Broadcast.get("menu.join") + Teams.BLACK_NAME);
                break;
        }

        is.setItemMeta(im);

        return is;
    }

}