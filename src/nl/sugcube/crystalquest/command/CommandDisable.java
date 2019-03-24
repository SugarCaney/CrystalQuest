package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandDisable extends CrystalQuestCommand {

    public CommandDisable() {
        super("disable", "commands.disable-usage", 1);

        addPermissions(
                "crystalquest.admin",
                "crystalquest.staff",
                "crystalquest.enable"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        try {
            Arena arena;
            try {
                arena = plugin.arenaManager.getArena(Integer.parseInt(arguments[0]) - 1);
            }
            catch (Exception e) {
                arena = plugin.arenaManager.getArena(arguments[0]);
            }
            
            arena.setEnabled(false);
            sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.disable-succeed")
                    .replace("%arena%", arguments[0]));
        }
        catch (Exception e) {
            sender.sendMessage(Broadcast.get("commands.disable-error"));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
