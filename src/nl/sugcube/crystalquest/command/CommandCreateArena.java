package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandCreateArena extends CrystalQuestCommand {

    public CommandCreateArena() {
        super("createarena", null, 0);

        addPermissions(
                "crystalquest.admin"
        );
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        boolean isFound = false;
        int i = 0;
        while (!isFound) {
            if (plugin.getArenaManager().getArena(i) == null) {
                isFound = true;
            }
            i++;
        }
        int arenaId = plugin.getArenaManager().createArena() + 1;
        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.createarena")
                .replace("%arena%", Integer.toString(arenaId)));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
