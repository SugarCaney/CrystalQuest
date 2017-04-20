package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandForceStart extends CrystalQuestCommand {

    public CommandForceStart() {
        super("forcestart", "commands.forcestart-usage", 0);

        addPermissions(
                "crystalquest.admin",
                "crystalquest.staff",
                "crystalquest.forcestart"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        // Force start current arena.
        if (arguments.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                Arena arena = plugin.getArenaManager().getArena(player);
                if (arena != null) {
                    arena.setIsCounting(true);
                    sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.forcestart-succeed")
                            .replace("%arena%", arena.getName()));
                    return;
                }
            }
        }

        try {
            Arena arena;
            try {
                arena = plugin.am.getArena(Integer.parseInt(arguments[0]) - 1);
            }
            catch (Exception e) {
                arena = plugin.am.getArena(arguments[0]);
            }

            arena.setIsCounting(true);
            sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.forcestart-succeed")
                    .replace("%arena%", arguments[0]));
        }
        catch (Exception e) {
            sender.sendMessage(Broadcast.get("commands.forcestart-error"));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}