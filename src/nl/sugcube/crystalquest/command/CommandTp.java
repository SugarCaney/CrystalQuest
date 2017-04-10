package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author SugarCaney
 */
public class CommandTp extends CrystalQuestCommand {

    public CommandTp() {
        super("tp", "commands.tp-usage", 1);

        addPermissions(
                "crystalquest.admin",
                "crystalquest.staff",
                "crystalquest.tp"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;
        Arena arena;
        try {
            arena = plugin.am.getArena(Integer.parseInt(arguments[0]) - 1);
        }
        catch (Exception e) {
            arena = plugin.am.getArena(arguments[0]);
        }

        // Check if the arena exists.
        if (arena == null) {
            player.sendMessage(Broadcast.get("arena.no-exist"));
            return;
        }

        // Check if the player is not ingame.
        if (plugin.getArenaManager().isInGame(player)) {
            player.sendMessage(Broadcast.get("commands.tp-ingame"));
            return;
        }

        // Find spawn location.
        List<Location> spawns = arena.getPlayerSpawns();
        if (spawns.isEmpty()) {
            player.sendMessage(Broadcast.get("commands.tp-nospawns"));
            return;
        }

        // Teleport
        player.teleport(spawns.get(0));
        player.sendMessage(Broadcast.TAG + Broadcast.get("commands.tp-teleport")
                .replace("%a%", arguments[0]));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
