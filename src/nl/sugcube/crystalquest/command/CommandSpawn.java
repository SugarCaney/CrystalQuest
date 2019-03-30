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
public class CommandSpawn extends CrystalQuestCommand {

    public CommandSpawn() {
        super("spawn", "commands.spawn-usage", 1);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
        addAutoCompleteMeta(1, AutoCompleteArgument.CLEAR_OR_REMOVE_LAST);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Arena arena;
        try {
            arena = plugin.arenaManager.getArena(Integer.parseInt(arguments[0]) - 1);
        }
        catch (Exception e) {
            arena = plugin.arenaManager.getArena(arguments[0]);
        }

        // Check if the arenas exists.
        if (arena == null) {
            sender.sendMessage(Broadcast.get("arena.no-exist"));
            return;
        }

        // Clear or pop last.
        if (arguments.length >= 2) {
            if (arguments[1].equalsIgnoreCase("clear")) {
                try {
                    arena.clearPlayerSpawns();
                    sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.spawn-removeall")
                            .replace("%arena%", arguments[1]));
                }
                catch (Exception e) {
                    sender.sendMessage(Broadcast.get("commands.spawn-removeall-error"));
                }
            }
            else if (arguments[1].equalsIgnoreCase("undo")) {
                try {
                    List<Location> spawns = arena.getPlayerSpawns();
                    if (spawns.isEmpty()) {
                        sender.sendMessage(Broadcast.get("commands.spawn-undo-empty"));
                    }
                    else {
                        spawns.remove(spawns.size() - 1);
                        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.spawn-undo")
                                .replace("%amount%", Integer.toString(spawns.size())));
                    }
                }
                catch (Exception e) {
                    sender.sendMessage(Broadcast.get("commands.spawn-undo-error"));
                }
            }
        }
        // Add spawn
        else if (arguments.length == 1) {
            try {
                arena.addPlayerSpawn(((Player)sender).getLocation());
                sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.spawn-added")
                        .replace("%no%", Integer.toString(arena.getPlayerSpawns().size()))
                        .replace("%arena%", arguments[0]));
            }
            catch (Exception e) {
                sender.sendMessage(Broadcast.get("commands.spawn-added-error"));
            }
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
