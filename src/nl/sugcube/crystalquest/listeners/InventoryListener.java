package nl.sugcube.crystalquest.listeners;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.ClassUtils;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
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

    private CrystalQuest plugin;
    private Random ran = new Random();

    public InventoryListener(CrystalQuest instance) {
        plugin = instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (plugin.arenaManager.isInGame((Player)event.getWhoClicked())) {
            event.setCancelled(true);
        }
        if (event.getCurrentItem() == null) {
            return;
        }

        String inventoryName = event.getView().getTitle();
        if (inventoryName.contains("Pick Team: ")) {
            pickTeam(event);
        }
        else if ("Pick a Class".equals(inventoryName)) {
            pickClass(event);
        }
        else if (inventoryName.equalsIgnoreCase("Spectate an arenas")) {
            spectate(event);
        }
        else if (inventoryName.contains(ChatColor.LIGHT_PURPLE + "CrystalQuest Shop:")) {
            event.setCancelled(true);
        }
    }

    public void spectate(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player)event.getWhoClicked();
        player.closeInventory();

        if (event.getCurrentItem().hasItemMeta()) {
            if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                Arena a = plugin.arenaManager.getArena(event.getCurrentItem().getItemMeta().getDisplayName()
                        .replace(ChatColor.AQUA + "Spectate ", ""));
                a.addPlayer((Player)event.getWhoClicked(), CrystalQuestTeam.SPECTATOR, true);
            }
        }
    }

    public void pickClass(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player)event.getWhoClicked();
        player.closeInventory();

        if (event.getCurrentItem().hasItemMeta()) {
            if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Random Class")) {
                    plugin.inventoryManager.playerClass.remove(player.getUniqueId());
                    player.sendMessage(Broadcast.TAG + Broadcast.get("arena.random-class"));
                }
                else {
                    String techName = plugin.menuSelectClass.getTechnicalClassName(
                            event.getCurrentItem().getItemMeta().getDisplayName());
                    if (ClassUtils.hasPermission(player, techName)) {
                        plugin.inventoryManager.setPlayerClass(player, techName);
                        player.sendMessage(Broadcast.TAG + Broadcast.get("arena.chosen-class")
                                .replace("%class%", event.getCurrentItem().getItemMeta().getDisplayName()));
                    }
                    else {
                        player.sendMessage(Broadcast.get("arena.no-perm-class"));
                    }
                }
            }
        }
    }

    public void pickTeam(InventoryClickEvent event) {
        if (event.getCurrentItem().getAmount() <= 0) {
            return;
        }

        try {
            event.setCancelled(true);
            Player player = (Player)event.getWhoClicked();
            player.closeInventory();

            String arenaName = event.getView().getTitle().replace("Pick Team: ", "");
            Arena arena = plugin.arenaManager.getArena(arenaName);
            String displayName = "";
            CrystalQuestTeam team = null;

            if (event.getCurrentItem().hasItemMeta()) {
                if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                    displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                }
            }

            if (displayName.contains("Random Team")) {
                try {
                    if (arena.getSmallestTeams().size() > 0) {
                        boolean isNotOk = true;
                        while (isNotOk) {
                            team = arena.getSmallestTeams().get(ran.nextInt(arena.getSmallestTeams().size()));
                            if (arena.getSmallestTeams().contains(team)) {
                                isNotOk = false;
                            }
                        }
                    }
                }
                catch (Exception exep) {
                    exep.printStackTrace();
                }
            }
            else {
                team = CrystalQuestTeam.valueOfName(displayName.replace("Join ", ""));
            }

            if (plugin.arenaManager.isInGame(player)) {
                player.sendMessage(Broadcast.get("commands.lobby-already-ingame"));
                return;
            }

            arena.addPlayer(player, team, false);
            plugin.menuPickTeam.updateMenus();
        }
        catch (Exception exeption) {
            exeption.printStackTrace();
        }
    }
}