package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandCreateArena extends CrystalQuestCommand {

    public CommandCreateArena() {
        super("createarena", "commands.createarena-usage", 0);

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

        if (arguments.length >= 1) {
            String name = arguments[0];
            Arena arena = plugin.getArenaManager().getArena(arenaId - 1);
            boolean succeeded = arena.setName(name);
            if (succeeded) {
                sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.createarena-named")
                        .replace("%arena%", Integer.toString(arenaId))
                        .replace("%name%", name)
                );
            }
            else {
                sender.sendMessage(Broadcast.get("commands.wrong-name"));
            }
            return;
        }

        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.createarena")
                .replace("%arena%", Integer.toString(arenaId)));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
