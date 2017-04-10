package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.Help;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandHelp extends CrystalQuestCommand {

    public CommandHelp() {
        super("help", null, 0);

        addAutoCompleteMeta(0, AutoCompleteArgument.SETUP);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        // Setup
        if (arguments.length > 0 && arguments[0].equalsIgnoreCase("setup") &&
                sender.hasPermission("crystalquest.admin")) {
            Help.showSetup(sender);
            return;
        }

        // Default
        Help.showDefault(sender);
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
