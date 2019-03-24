package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandDoubleJump extends CrystalQuestCommand {

    public CommandDoubleJump() {
        super("doublejump", "commands.doublejump-usage", 1);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
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

        arena.setDoubleJump(!arena.canDoubleJump());
        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.doublejump-set")
                .replace("%arena%", arguments[0])
                .replace("%canjump%", arena.canDoubleJump() + ""));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
