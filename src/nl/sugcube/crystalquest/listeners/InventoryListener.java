package nl.sugcube.crystalquest.listeners;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.Teams;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.Classes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class InventoryListener implements Listener {

    public static CrystalQuest plugin;

    public InventoryListener(CrystalQuest instance) {
        plugin = instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (plugin.am.isInGame((Player)e.getWhoClicked())) {
            e.setCancelled(true);
        }

        if (e.getInventory().getName().contains("Pick Team: ")) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getAmount() > 0) {
                    try {
                        e.setCancelled(true);
                        Player player = (Player)e.getWhoClicked();
                        player.closeInventory();

                        Arena a = plugin.am.getArena(e.getInventory().getName().replace("Pick Team: ", ""));
                        int teamId = 0;
                        String displayName = "";

                        if (e.getCurrentItem().hasItemMeta()) {
                            if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                                displayName = e.getCurrentItem().getItemMeta().getDisplayName();
                            }
                        }

                        if (displayName.contains(Teams.GREEN_NAME)) {
                            teamId = 0;
                        }
                        else if (displayName.contains(Teams.ORANGE_NAME)) {
                            teamId = 1;
                        }
                        else if (displayName.contains(Teams.YELLOW_NAME)) {
                            teamId = 2;
                        }
                        else if (displayName.contains(Teams.RED_NAME)) {
                            teamId = 3;
                        }
                        else if (displayName.contains(Teams.BLUE_NAME)) {
                            teamId = 4;
                        }
                        else if (displayName.contains(Teams.MAGENTA_NAME)) {
                            teamId = 5;
                        }
                        else if (displayName.contains(Teams.WHITE_NAME)) {
                            teamId = 6;
                        }
                        else if (displayName.contains(Teams.BLACK_NAME)) {
                            teamId = 7;
                        }
                        else if (displayName.contains("Random Team")) {
                            try {
                                if (a.getSmallestTeams().size() > 0) {
                                    Random ran = new Random();
                                    boolean isNotOk = true;
                                    while (isNotOk) {
                                        teamId = a.getSmallestTeams().get(ran.nextInt(a.getSmallestTeams().size()));
                                        if (a.getSmallestTeams().contains(teamId)) {
                                            isNotOk = false;
                                        }
                                    }
                                }
                            }
                            catch (Exception exep) {
                                exep.printStackTrace();
                            }
                        }

                        a.addPlayer(player, teamId, false);
                        plugin.menuPT.updateMenus();
                    }
                    catch (Exception exeption) {
                        exeption.printStackTrace();
                    }
                }
            }
        }
        else if (e.getInventory().getName() == "Pick a Class") {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                Player player = (Player)e.getWhoClicked();
                player.closeInventory();

                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Random Class")) {
                            plugin.im.playerClass.remove(player.getUniqueId());
                            player.sendMessage(Broadcast.TAG + Broadcast.get("arena.random-class"));
                        }
                        else {
                            String techName = plugin.menuSC.getTechnicalClassName(
                                    e.getCurrentItem().getItemMeta().getDisplayName());
                            if (Classes.hasPermission(player, techName)) {
                                plugin.im.setPlayerClass(player, techName);
                                player.sendMessage(Broadcast.TAG + Broadcast.get("arena.chosen-class")
                                        .replace("%class%", e.getCurrentItem().getItemMeta().getDisplayName()));
                            }
                            else {
                                player.sendMessage(Broadcast.get("arena.no-perm-class"));
                            }
                        }
                    }
                }
            }
        }
        else if (e.getInventory().getName().equalsIgnoreCase("Spectate an arena")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                Player player = (Player)e.getWhoClicked();
                player.closeInventory();

                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        Arena a;
                        try {
                            a = plugin.am.getArena(e.getCurrentItem().getItemMeta().getDisplayName()
                                    .replace(ChatColor.AQUA + "Spectate ", ""));
                            a.addPlayer((Player)e.getWhoClicked(), 0, true);
                        }
                        catch (Exception ignored) {
                        }
                    }
                }
            }
        }
        else if (e.getInventory().getName().contains(ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:")) {
            e.setCancelled(true);
        }
    }
}