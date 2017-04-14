package nl.sugcube.crystalquest.listeners;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import nl.sugcube.crystalquest.game.Teams;
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
                        CrystalQuestTeam team = null;
                        String displayName = "";

                        if (e.getCurrentItem().hasItemMeta()) {
                            if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                                displayName = e.getCurrentItem().getItemMeta().getDisplayName();
                            }
                        }

                        if (displayName.contains(Teams.GREEN_NAME)) {
                            team = CrystalQuestTeam.GREEN;
                        }
                        else if (displayName.contains(Teams.ORANGE_NAME)) {
                            team = CrystalQuestTeam.ORANGE;
                        }
                        else if (displayName.contains(Teams.YELLOW_NAME)) {
                            team = CrystalQuestTeam.YELLOW;
                        }
                        else if (displayName.contains(Teams.RED_NAME)) {
                            team = CrystalQuestTeam.RED;
                        }
                        else if (displayName.contains(Teams.BLUE_NAME)) {
                            team = CrystalQuestTeam.BLUE;
                        }
                        else if (displayName.contains(Teams.MAGENTA_NAME)) {
                            team = CrystalQuestTeam.MAGENTA;
                        }
                        else if (displayName.contains(Teams.WHITE_NAME)) {
                            team = CrystalQuestTeam.WHITE;
                        }
                        else if (displayName.contains(Teams.BLACK_NAME)) {
                            team = CrystalQuestTeam.BLACK;
                        }
                        else if (displayName.contains("Random Team")) {
                            try {
                                if (a.getSmallestTeams().size() > 0) {
                                    Random ran = new Random();
                                    boolean isNotOk = true;
                                    while (isNotOk) {
                                        team = a.getSmallestTeams().get(ran.nextInt(a.getSmallestTeams().size()));
                                        if (a.getSmallestTeams().contains(team)) {
                                            isNotOk = false;
                                        }
                                    }
                                }
                            }
                            catch (Exception exep) {
                                exep.printStackTrace();
                            }
                        }

                        a.addPlayer(player, team, false);
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
        else if (e.getInventory().getName().equalsIgnoreCase("Spectate an arenas")) {
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
                            a.addPlayer((Player)e.getWhoClicked(), CrystalQuestTeam.SPECTATOR, true);
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