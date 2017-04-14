package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandCrystalSpawn extends CrystalQuestCommand {

    public CommandCrystalSpawn() {
        super("crystalspawn", "commands.crystalspawn-usage", 1);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
        addAutoCompleteMeta(1, AutoCompleteArgument.CLEAR);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Arena arena;
        try {
            arena = plugin.am.getArena(Integer.parseInt(arguments[0]) - 1);
        }
        catch (Exception e) {
            arena = plugin.am.getArena(arguments[0]);
        }

        // Check if the arenas exists.
        if (arena == null) {
            sender.sendMessage(Broadcast.get("arena.no-exist"));
            return;
        }

        // Clear
        if (arguments.length >= 2) {
            if (arguments[1].equalsIgnoreCase("clear")) {
                try {
                    arena.clearCrystalSpawns();
                    sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.crystalspawn-removeall")
                            .replace("%arena%", arguments[0]));
                }
                catch (Exception e) {
                    sender.sendMessage(Broadcast.get("commands.crystalspawn-removeall-error"));
                }
            }
        }
        // Add spawn
        else if (arguments.length == 1) {
            try {
                arena.addCrystalSpawn(((Player)sender).getLocation().add(0, -0.6, 0));
                sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.crystalspawn-added")
                        .replace("%no%", Integer.toString(arena.getCrystalSpawns().size()))
                        .replace("%arena%", arguments[0]));
            }
            catch (Exception e) {
                sender.sendMessage(Broadcast.get("commands.crystalspawn-added-error"));
            }
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
