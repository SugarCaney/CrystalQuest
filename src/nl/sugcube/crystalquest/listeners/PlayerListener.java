package nl.sugcube.crystalquest.listeners;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Teams;
import nl.sugcube.crystalquest.Update;
import nl.sugcube.crystalquest.economy.Multipliers;
import nl.sugcube.crystalquest.events.PlayerEarnCrystalsEvent;
import nl.sugcube.crystalquest.events.PlayerJoinArenaEvent;
import nl.sugcube.crystalquest.events.PlayerLeaveArenaEvent;
import nl.sugcube.crystalquest.events.TeamWinGameEvent;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.ArenaManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;

/**
 * @author SugarCaney
 */
public class PlayerListener implements Listener {

    public static CrystalQuest plugin;

    public PlayerListener(CrystalQuest instance) {
        plugin = instance;
    }

    /*
     * Make sure nobody is in the arena.
     */
    @EventHandler
    public void onPlayerInArena(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("crystalquest.admin") || p.hasPermission("crystalquest.staff")) {
            return;
        }

        if (plugin.prot.isInProtectedArena(p.getLocation())) {
            if (!plugin.getArenaManager().isInGame(p)) {
                p.teleport(plugin.getArenaManager().getLobby());
            }
        }
    }

    /*
     * SPECTATOR STUFF
     */
    @EventHandler
    public void onSpectatorDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            ArenaManager am = plugin.getArenaManager();
            if (am.isInGame(p)) {
                if (am.getArena(p.getUniqueId()).getSpectators().contains(p.getUniqueId())) {
                    p.setFireTicks(0);
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setExp(0);
                    p.setLevel(0);
                    p.setSaturation(20);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageBySpectator(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            if (plugin.getArenaManager().isInGame(p)) {
                if (plugin.getArenaManager().getArena(p.getUniqueId()).getSpectators().contains(p.getUniqueId())) {
                    e.setDamage(0);
                    e.setCancelled(true);
                }
            }
        }
        if (e.getDamager() instanceof Player) {
            Player p = (Player)e.getDamager();
            if (plugin.getArenaManager().isInGame(p)) {
                if (plugin.getArenaManager().getArena(p.getUniqueId()).getSpectators().contains(p.getUniqueId())) {
                    e.setDamage(0);
                    e.setCancelled(true);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onSpectatorInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (plugin.getArenaManager().isInGame(p)) {
            if (plugin.getArenaManager().getArena(p.getUniqueId()).getSpectators().contains(p.getUniqueId())) {
                e.setCancelled(true);
                p.getInventory().clear();
                p.updateInventory();
            }
        }
    }

    @EventHandler
    public void onCropsTrampled(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        Block block = event.getClickedBlock();
        if (!plugin.prot.isInProtectedArena(block.getLocation())) {
            return;
        }

        if (block.getType() != Material.SOIL) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onSpectatorPickupItem(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (plugin.getArenaManager().isInGame(p)) {
            if (plugin.getArenaManager().getArena(p.getUniqueId()).getSpectators().contains(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMoveOutsideTheArena(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (plugin.getArenaManager().isInGame(p)) {
            Location loc = e.getTo();
            if (plugin.getArenaManager().getArena(p.getUniqueId()).getSpectators().contains(p.getUniqueId())) {
                if (!plugin.prot.isInProtectedArena(loc)) {
                    e.setCancelled(true);
                }
            }
        }
    }
    /*
     * END SPECTATOR STUFF
	 */

    @EventHandler
    public void onTeamWinGame(TeamWinGameEvent e) {
        Team[] teams = e.getTeams();
        Arena a = e.getArena();
        int score = a.getScore(Teams.getTeamIdFromName(e.getTeamName()));
        int verschil = 9999999;

        int i = 0;
        for (Team t : teams) {
            if (i == e.getTeamCount()) {
                break;
            }

            if (t != null) {
                if (a.getScore(i) >= 0) {
                    if (Math.abs(score - a.getScore(i)) < verschil) {
                        verschil = Math.abs(score - a.getScore(i));
                    }
                }
            }
            i++;
        }

        int crystals = 25;
        int extrac = 0;
        extrac = (int)((((double)score) - ((double)verschil)) / ((double)score)) * 25;
        if (extrac > 25) {
            extrac = 25;
        }
        crystals += extrac;

        for (UUID id : e.getPlayers()) {
            Player p = Bukkit.getPlayer(id);
            int vip = 1;
            if (p.hasPermission("crystalquest.doublecash") ||
                    p.hasPermission("crystalquest.admin") ||
                    p.hasPermission("crystalquest.staff")) {
                vip = 2;
            }

            int money = (int)(crystals * plugin.getConfig().getDouble("shop.crystal-multiplier") * vip);

            double extra = Multipliers.getMultiplier("win",
                    plugin.economy.getLevel(p, "win", "crystals"), false);

            int moneyEarned = (int)(money * extra);


            // Call Event
            PlayerEarnCrystalsEvent event = new PlayerEarnCrystalsEvent(p, a, moneyEarned);
            Bukkit.getPluginManager().callEvent(event);

            String message = plugin.economy.getCoinMessage(p, event.getAmount());
            ;

            if (!event.isCancelled()) {
                plugin.economy.getBalance().addCrystals(p, event.getAmount(), false);

                if (event.showMessage() && message != null) {
                    p.sendMessage(message);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (plugin.getArenaManager().isInGame(player)) {
            if (plugin.getArenaManager().getArena(player.getUniqueId()).canDoubleJump()) {
                if (!plugin.getArenaManager().getArena(player.getUniqueId()).getSpectators().contains(player.getUniqueId())) {
                    if (player.getGameMode() != GameMode.CREATIVE) {

                        //Check for ability doublejump_boost
                        double addedVelocity = 0.9518;
                        if (plugin.ab.getAbilities().containsKey(player.getUniqueId())) {
                            if (plugin.ab.getAbilities().get(player.getUniqueId()).contains
                                    ("doublejump_boost")) {
                                addedVelocity = 1.0982;
                            }
                        }

                        player.setVelocity(new Vector(player.getVelocity().getX(), addedVelocity, player.getVelocity().getZ()));
                        player.setVelocity(player.getVelocity().add(player.getLocation().getDirection().multiply(0.3018)));
                        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1F, 1F);
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.getArenaManager().isInGame(player)) {
            if (plugin.getArenaManager().getArena(player.getUniqueId()).canDoubleJump()) {
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    if (event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                        event.getPlayer().setAllowFlight(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (plugin.getArenaManager().isInGame(p)) {
            plugin.getArenaManager().getArena(p.getUniqueId()).removePlayer(p);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (plugin.getConfig().getBoolean("updates.check-for-updates")) {
            if (plugin.getConfig().getBoolean("updates.show-admin")) {
                Update uc = new Update(69421, plugin.getDescription().getVersion());
                if (uc.query()) {
                    if (e.getPlayer().hasPermission("crystalquest.admin")) {
                        e.getPlayer().sendMessage(Broadcast.TAG + "A new version of CrystalQuest is available!");
                        e.getPlayer().sendMessage(Broadcast.TAG + "Get it at the BukkitDev-page!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ArenaManager arenaManager = plugin.getArenaManager();
        boolean allowed = false;

        if (!plugin.getConfig().getBoolean("arena.chat-silence")) {
            allowed = true;
        }

        if (player.isOp()) {
            allowed = true;
        }

        if (player.hasPermission("crystalquest.admin")) {
            allowed = true;
        }

        if (player.hasPermission("crystalquest.staff")) {
            allowed = true;
        }

        if (!arenaManager.isInGame(player)) {
            allowed = true;
        }

        Arena arena = arenaManager.getArena(player.getUniqueId());

        if (arena == null) {
            return;
        }

        if (!arena.isInGame()) {
            allowed = true;
        }

        if (arena.isEndGame()) {
            allowed = true;
        }

        if (!allowed) {
            player.sendMessage(Broadcast.get("arena.chat-silence"));
            event.setMessage(null);
            event.setCancelled(true);
        }
        else {
            if (plugin.getConfig().getBoolean("arena.team-colour-prefix")) {
                if (plugin.getArenaManager().isInGame(player)) {
                    if (!plugin.getArenaManager().getArena(player.getUniqueId()).getSpectators()
                            .contains(player.getUniqueId())) {
                        event.setMessage(Teams.getTeamChatColour(plugin.getArenaManager().getTeam(player)) +
                                event.getMessage());
                    }
                    else {
                        event.setMessage(ChatColor.BLUE + "[Spec] " + event.getMessage());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeaveArena(PlayerLeaveArenaEvent e) {
        Player p = e.getPlayer();
        Arena a = e.getArena();

        for (Wolf w : a.getGameWolfs()) {
            if (w.getOwner() == p) {
                w.setHealth(0);
            }
        }

        if (a.isInGame()) {
            for (Team t : a.getScoreboardTeams()) {
                if (t.getPlayers().size() == a.getPlayers().size()) {
                    a.declareWinner();
                    a.setEndGame(true);
                }
            }
        }
        else {
            if (a.isCounting()) {
                if (a.getPlayers().size() < a.getMinPlayers()) {
                    a.setIsCounting(false);
                    a.setCountdown(plugin.getConfig().getInt("arena.countdown"));
                }
            }
        }
        plugin.signHandler.updateSigns();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            if (plugin.am.isInGame(p)) {
                if (!plugin.am.getArena(p.getUniqueId()).isInGame() || plugin.am.getArena(p.getUniqueId()).isEndGame()) {
                    e.setCancelled(true);
                }
                else {
                    if (e.getCause() == DamageCause.FALL) {
                        e.setCancelled(true);
                    }
                    else if (e.getCause() == DamageCause.LIGHTNING) {
                        p.setFireTicks(80);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            if (plugin.am.isInGame(p)) {
                p.setFoodLevel(20);
                p.setSaturation(20F);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (plugin.am.isInGame(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (plugin.am.isInGame(p)) {
            p.getInventory().clear();
            plugin.im.setClassInventory(p);
            Random ran = new Random();
            Arena a = plugin.getArenaManager().getArena(p.getUniqueId());

			/*
             * Teleports to team spawn if there are team spawns set.
			 * Otherwise, the normal playerspawns count.
			 */
            if (a.getTeamSpawns().get(a.getTeamCount() - 1).size() > 0) {
                int team = a.getTeam(p);
                e.setRespawnLocation(a.getTeamSpawns().get(team).get(ran.nextInt(a.getTeamSpawns().get(team).size())));
            }
            else {
                e.setRespawnLocation((a.getPlayerSpawns().get(ran.nextInt(a.getPlayerSpawns().size()))));
            }

            p.setLevel(0);
            p.setExp(0);

            if (plugin.ab.getAbilities().containsKey(p)) {
                if (plugin.ab.getAbilities().get(p).contains("health_boost")) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 36000, 0));
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player target = (Player)e.getDamager();
            if (plugin.am.isInGame((Player)e.getDamager())) {
                if (plugin.getArenaManager().getArena(target.getUniqueId()).isEndGame()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoinArena(PlayerJoinArenaEvent e) {
        if (e.getPlayer().hasPermission("crystalquest.arena." + e.getArena().getName()) ||
                e.getPlayer().hasPermission("crystalquest.arena." + e.getArena().getId()) ||
                e.getPlayer().hasPermission("crystalquest.arena.*") ||
                e.getPlayer().hasPermission("crystalquest.staff") ||
                e.getPlayer().hasPermission("crystalquest.admin")) {
            if (e.getArena().getPlayers().size() == e.getArena().getMaxPlayers() && !e.isSpectating()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(Broadcast.get("arena.full"));
                return;
            }

            if (e.getArena().isInGame() && !e.isSpectating()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(Broadcast.get("arena.already-started"));
                return;
            }

            if (e.getArena().getPlayers().size() + 1 == e.getArena().getMinPlayers() && !e.isSpectating()) {
                e.getArena().setIsCounting(true);
                e.getArena().setCountdown(plugin.getConfig().getInt("arena.countdown"));
                plugin.signHandler.updateSigns();
                plugin.menuPT.updateMenu(e.getArena());
            }
        }
        else {
            e.getPlayer().sendMessage(Broadcast.get("arena.no-permission"));
            e.setCancelled(true);
        }
    }

    /*
     * Block commands while in-game.
     */
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if (!e.getPlayer().hasPermission("crystalquest.admin") && !e.getPlayer().hasPermission("crystalquest.staff")) {
            if (!e.getMessage().equalsIgnoreCase("/cq quit") && !e.getMessage().equalsIgnoreCase("/cq leave") &&
                    !e.getMessage().equalsIgnoreCase("/cq class")) {
                if (plugin.am.isInGame(e.getPlayer())) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(Broadcast.get("arena.no-commands"));
                }
            }
        }
    }

    /*
     * Block in-game block breaking
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (plugin.am.isInGame(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    /*
     * Block in-game block placement.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (plugin.am.isInGame(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    /*
     * Allow certain items in-game
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            if (plugin.am.isInGame(p)) {
                e.setCancelled(true);
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            if (plugin.am.isInGame(p)) {
                if (p.getInventory().getItemInMainHand().getType() == Material.TNT) {
                    Location loc = p.getLocation().add(0, 1, 0);
                    TNTPrimed tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
                    tnt.setFuseTicks(40);
                    tnt.setYield(0);

                    int amount = p.getInventory().getItemInMainHand().getAmount();
                    if (amount == 1) {
                        p.getInventory().remove(p.getInventory().getItemInMainHand());
                    }
                    else {
                        p.getInventory().getItemInMainHand().setAmount(amount - 1);
                    }
                }
            }
        }


    }
}