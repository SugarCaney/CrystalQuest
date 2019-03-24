package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandList extends CrystalQuestCommand {

    public CommandList() {
        super("list", null, 0);
    }

    protected CommandList(String alias) {
        super(alias, null, 0);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        StringBuilder arenas = new StringBuilder();
        boolean first = false;
        for (Arena a : plugin.arenaManager.arenas) {
            if (!first) {
                arenas.append(a.getName()).append("[").append(a.getId() + 1).append("]");
                first = true;
            }
            else {
                arenas.append(", ").append(a.getName()).append("[").append(a.getId() + 1).append("]");
            }
        }
        sender.sendMessage(Broadcast.TAG + arenas);
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
