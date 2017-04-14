package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandMaxPlayers extends CrystalQuestCommand {

    public CommandMaxPlayers() {
        super("maxplayers", "commands.maxplayers-usage", 2);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        try {
            Arena arena;
            try {
                arena = plugin.am.getArena(Integer.parseInt(arguments[0]) - 1);
            }
            catch (Exception e) {
                arena = plugin.am.getArena(arguments[0]);
            }

            // Check if the arena exists.
            if (arena == null) {
                sender.sendMessage(Broadcast.get("arena.no-exist"));
                return;
            }

            arena.setMaxPlayers(Integer.parseInt(arguments[1]));
            sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.maxplayers-set")
                    .replace("%arena%", arguments[0])
                    .replace("%amount%", arguments[1]));
        }
        catch (Exception e) {
            sender.sendMessage(Broadcast.get("commands.maxplayers-error"));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
