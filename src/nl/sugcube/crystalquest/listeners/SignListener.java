package nl.sugcube.crystalquest.listeners;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import nl.sugcube.crystalquest.sba.SMethods;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

/**
 * @author SugarCaney
 */
public class SignListener implements Listener {

    public static CrystalQuest plugin;

    public SignListener(CrystalQuest instance) {
        plugin = instance;
    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.WALL_SIGN || e.getBlock().getType() == Material.SIGN) {
            Sign sign = (Sign)e.getBlock().getState();
            plugin.signHandler.getSigns().remove(sign);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player player = e.getPlayer();

        if (e.getLine(0).equalsIgnoreCase("[CrystalQuest]") || e.getLine(0).equalsIgnoreCase("[CQ]")) {
            e.setLine(0, "[CrystalQuest]");

            if (player.hasPermission("crystalquest.admin")) {
                if (e.getLine(1).isEmpty() && e.getLine(3).isEmpty() && (e.getLine(2).equalsIgnoreCase("class") || e.getLine(2).isEmpty())) {
                    e.setLine(0, "CrystalQuest");
                    e.setLine(2, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + Broadcast.get("sign.pick-class"));
                    player.sendMessage(Broadcast.TAG + Broadcast.get("sign.succesful").replace("%sign%", Broadcast.get("sign.class-sign")));
                }
                else if (e.getLine(1).isEmpty() && e.getLine(3).isEmpty() && e.getLine(2).equalsIgnoreCase("random")) {
                    e.setLine(0, "CrystalQuest");
                    e.setLine(2, ChatColor.GREEN + "" + ChatColor.BOLD + Broadcast.get("sign.random"));
                    player.sendMessage(Broadcast.TAG + Broadcast.get("sign.succesful").replace("%sign%", Broadcast.get("sign.random-sign")));
                }
                else if (e.getLine(1).isEmpty() && e.getLine(3).isEmpty() && e.getLine(2).equalsIgnoreCase("shop")) {
                    e.setLine(0, "CrystalQuest");
                    e.setLine(2, ChatColor.YELLOW + "" + ChatColor.BOLD + Broadcast.get("sign.open-shop"));
                    player.sendMessage(Broadcast.TAG + Broadcast.get("sign.succesful").replace("%sign%", Broadcast.get("sign.shop-sign")));
                }
                else if (e.getLine(1).isEmpty() && e.getLine(3).isEmpty() && e.getLine(2).equalsIgnoreCase("lobby")) {
                    e.setLine(0, "CrystalQuest");
                    e.setLine(2, ChatColor.DARK_RED + "" + ChatColor.BOLD + Broadcast.get("sign.lobby-sign-word"));
                    player.sendMessage(Broadcast.TAG + Broadcast.get("sign.succesful").replace("%sign%", Broadcast.get("sign.lobby-sign")));
                }
                else if (e.getLine(1).isEmpty() && e.getLine(3).isEmpty() && e.getLine(2).equalsIgnoreCase("spectate")) {
                    e.setLine(0, "CrystalQuest");
                    e.setLine(2, ChatColor.AQUA + "" + ChatColor.BOLD + Broadcast.get("sign.spectate"));
                    player.sendMessage(Broadcast.TAG + Broadcast.get("sign.succesful").replace("%sign%", Broadcast.get("sign.spectate-sign")));
                }
                else {
                    try {
                        Arena a = plugin.arenaManager.getArena(e.getLine(1).replace(ChatColor.ITALIC + "", ""));

                        if (a.isEnabled()) {
                            if (a.isCounting()) {
                                e.setLine(3, ChatColor.YELLOW + Broadcast.get("arena.starting"));
                                e.setLine(2, a.getPlayers().size() + "/" + a.getMaxPlayers() + " | " +
                                        a.getCountdown() + "s");
                                e.setLine(1, ChatColor.ITALIC + a.getName());
                                e.setLine(0, ChatColor.GREEN + "" + ChatColor.BOLD + "CQ-Join");
                            }
                            else if (a.isInGame()) {
                                e.setLine(3, ChatColor.DARK_RED + Broadcast.get("arena.in-game"));
                                e.setLine(2, SMethods.toTime(a.getTimeLeft()) + " left");
                                e.setLine(1, ChatColor.ITALIC + a.getName());
                                e.setLine(0, ChatColor.AQUA + "" + ChatColor.BOLD + "CQ-Spectate");
                            }
                            else if (a.isEndGame()) {
                                e.setLine(3, ChatColor.DARK_PURPLE + Broadcast.get("arena.restarting-status"));
                                e.setLine(2, "");
                                e.setLine(1, ChatColor.ITALIC + a.getName());
                                e.setLine(0, ChatColor.DARK_RED + "" + ChatColor.BOLD + "Unjoinable");
                            }
                            else {
                                e.setLine(3, ChatColor.GREEN + Broadcast.get("arena.in-lobby"));
                                e.setLine(2, a.getPlayers().size() + "/" + a.getMaxPlayers());
                                e.setLine(1, ChatColor.ITALIC + a.getName());
                                e.setLine(0, ChatColor.GREEN + "" + ChatColor.BOLD + "CQ-Join");
                            }

                        }
                        else {
                            e.setLine(0, "");
                            e.setLine(1, ChatColor.ITALIC + a.getName());
                            e.setLine(2, ChatColor.DARK_RED + "" + ChatColor.BOLD + Broadcast.get("commands.disabled"));
                            e.setLine(3, "");
                        }

                        Sign s = (Sign)e.getBlock().getState();
                        s.setLine(0, e.getLine(0));
                        s.setLine(1, e.getLine(1));
                        s.setLine(2, e.getLine(2));
                        s.setLine(3, e.getLine(3));
                        s.update();

                        plugin.signHandler.getSigns().add(s.getLocation());

                        player.sendMessage(Broadcast.TAG + Broadcast.get("sign.succesful").replace("%sign%", Broadcast.get("sign.arena-sign")));
                    }
                    catch (Exception ex) {
                        e.setLine(0, Broadcast.get("sign.invalid"));
                        e.setLine(1, Broadcast.get("sign.invalid"));
                        e.setLine(2, Broadcast.get("sign.invalid"));
                        e.setLine(3, Broadcast.get("sign.invalid"));
                    }
                }
            }
            else {
                e.setLine(0, Broadcast.get("sign.no-perms"));
                e.setLine(1, Broadcast.get("sign.no-perms"));
                e.setLine(2, Broadcast.get("sign.no-perms"));
                e.setLine(3, Broadcast.get("sign.no-perms"));
            }
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //Checks Right mouse button
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            //Checks if sign.
            if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN) {
                //Checks if the sign contains [CrystalQuest]
                Sign s = (Sign)e.getClickedBlock().getState();
                if (s.getLine(0).contains("CrystalQuest")) {

                    e.setCancelled(true);
                    /*
                     * Checks if the sign is a Class-pick sign and opens the class-choice menu if so.
					 */
                    if (s.getLine(2).equalsIgnoreCase(ChatColor.DARK_PURPLE + Broadcast.get("sign.pick-class")) ||
                            s.getLine(2).equalsIgnoreCase(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + Broadcast.get("sign.pick-class"))) {
                        plugin.menuSelectClass.openMenu(e.getPlayer());

                    }
					/*
					 * Checks if the sign is a Spectate-sign and opens the spectate menu if so.
					 */
                    else if (s.getLine(2).equalsIgnoreCase(ChatColor.AQUA + Broadcast.get("sign.spectate")) ||
                            s.getLine(2).equalsIgnoreCase(ChatColor.AQUA + "" + ChatColor.BOLD + Broadcast.get("sign.spectate"))) {
                        if (e.getPlayer().hasPermission("crystalquest.admin") ||
                                e.getPlayer().hasPermission("crystalquest.staff") ||
                                e.getPlayer().hasPermission("crystalquest.spectate")) {
                            plugin.menuSpectate.showMenu(e.getPlayer());
                        }
                        else {
                            e.getPlayer().sendMessage(Broadcast.get("sign.no-permission"));
                        }
                    }
					/*
					 * Checks if the sign is a Shop-sign and opens the shop menu if so.
					 */
                    else if (s.getLine(2).equalsIgnoreCase(ChatColor.YELLOW + Broadcast.get("sign.open-shop")) ||
                            s.getLine(2).equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + Broadcast.get("sign.open-shop"))) {
                        plugin.economy.getMainMenu().showMenu(e.getPlayer());

                    }
					/*
					 * Checks if the sign is a Lobby-sign and let you leave the game/teleport to lobby if so.
					 */
                    else if (s.getLine(2).equalsIgnoreCase(ChatColor.DARK_RED + Broadcast.get("sign.lobby-sign-word")) ||
                            s.getLine(2).equalsIgnoreCase(ChatColor.DARK_RED + "" + ChatColor.BOLD + Broadcast.get("sign.lobby-sign-word"))) {
                        if (plugin.getArenaManager().isInGame(e.getPlayer())) {
                            plugin.arenaManager.getArena(e.getPlayer().getUniqueId()).removePlayer(e.getPlayer());
                            e.getPlayer().sendMessage(Broadcast.TAG + Broadcast.get("sign.left"));
                        }
                        else {
                            e.getPlayer().teleport(plugin.getArenaManager().getLobby());
                            e.getPlayer().sendMessage(Broadcast.TAG + Broadcast.get("sign.lobby"));
                        }

                    }
					/*
					 * Checks if the sign is a Random Arena-pick sign and picks a random arenas.
					 */
                    else if (s.getLine(2).equalsIgnoreCase(ChatColor.GREEN + Broadcast.get("sign.random")) ||
                            s.getLine(2).equalsIgnoreCase(ChatColor.GREEN + "" + ChatColor.BOLD + Broadcast.get("sign.random"))) {
                        Arena a = null;
                        Random ran = new Random();
                        boolean nothingAvaiable = true;

                        for (int i = 0; i < plugin.getArenaManager().getArenas().size(); i++) {
                            if (!plugin.getArenaManager().getArena(i).isInGame()) {
                                if (!plugin.getArenaManager().getArena(i).isFull()) {
                                    if (plugin.getArenaManager().getArena(i).isEnabled()) {
                                        nothingAvaiable = false;
                                        break;
                                    }
                                }
                            }
                        }

                        if (nothingAvaiable) {
                            e.getPlayer().sendMessage(Broadcast.get("sign.no-available"));
                        }
                        else {
                            int count = 0;
                            while (a == null) {
                                a = plugin.getArenaManager().getArena(ran.nextInt(plugin.getArenaManager().getArenas().size()));
                                if (a.isInGame() || a.isFull() || !a.isEnabled()) {
                                    a = null;
                                }

                                if (a != null) {
                                    plugin.menuPickTeam.updateMenu(a);
                                    plugin.menuPickTeam.showMenu(e.getPlayer(), a);
                                    break;
                                }

                                if (count > 999) {
                                    e.getPlayer().sendMessage(Broadcast.get("sign.no-available"));
                                    break;
                                }

                                count++;
                            }
                        }


                    }
                }
				/*
				 * Checks if the sign is indeed an Arena-sign and opens the team-choice menu,
				 * only if the arenas is joinable.
				 */
                else if (s.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "" + ChatColor.BOLD + "CQ-Join")) {
                    if (!plugin.signHandler.getSigns().contains(s.getLocation())) {
                        plugin.signHandler.getSigns().add(s.getLocation());
                        e.getPlayer().sendMessage(Broadcast.TAG + Broadcast.get("sign.registered"));
                    }
                    else {
                        Arena a = plugin.getArenaManager().getArena(s.getLine(1).replace(ChatColor.ITALIC + "", ""));
                        if (a != null) {
                            if (plugin.getArenaManager().isInGame(e.getPlayer())) {
                                e.getPlayer().sendMessage(Broadcast.get("commands.lobby-already-ingame"));
                            }
                            else if (!a.isFull()) {
                                plugin.menuPickTeam.updateMenu(a);
                                plugin.menuPickTeam.showMenu(e.getPlayer(), a);
                            }
                            else {
                                e.getPlayer().sendMessage(Broadcast.get("arena.full"));
                            }
                        }
                        else {
                            e.getPlayer().sendMessage(Broadcast.get("arena.no-exist"));
                        }
                    }

                }
				/*
				 * Lets the player spectate when the arenas is full.
				 */
                else if (s.getLine(0).equalsIgnoreCase(ChatColor.AQUA + "" + ChatColor.BOLD + "CQ-Spectate")) {
                    if (!plugin.signHandler.getSigns().contains(s.getLocation())) {
                        plugin.signHandler.getSigns().add(s.getLocation());
                        e.getPlayer().sendMessage(Broadcast.TAG + Broadcast.get("sign.registered"));
                    }
                    else {
                        Arena a = plugin.getArenaManager().getArena(s.getLine(1).replace(ChatColor.ITALIC + "", ""));
                        if (a != null) {
                            a.addPlayer(e.getPlayer(), CrystalQuestTeam.SPECTATOR, true);
                        }
                        else {
                            e.getPlayer().sendMessage(Broadcast.get("arena.no-exist"));
                        }
                    }
                }
            }
        }
    }
}