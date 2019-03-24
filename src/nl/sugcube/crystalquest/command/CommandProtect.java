package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandProtect extends CrystalQuestCommand {

    public CommandProtect() {
        super("protect", "commands.protect-usage", 1);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
        addAutoCompleteMeta(1, AutoCompleteArgument.RESET);
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

        // Remove
        if (arguments.length >= 2) {
            if (arguments[1].equalsIgnoreCase("remove")) {
                arena.setProtection(null);
                sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.protect-remove")
                        .replace("%arena%", arguments[1]));
                return;
            }
        }

        // Protect
        try {
            plugin.protection.protectArena(arena);
            sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.protect-succeed")
                    .replace("%arena%", arguments[1]));
        }
        catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(Broadcast.get("commands.protect-error"));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
