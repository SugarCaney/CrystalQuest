package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandHowdey extends CrystalQuestCommand {

    public CommandHowdey() {
        super("howdey", null, 0);
    }

    protected CommandHowdey(String alias) {
        super(alias, null, 0);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        sender.sendMessage(Broadcast.TAG + Broadcast.HOWDEY);
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
