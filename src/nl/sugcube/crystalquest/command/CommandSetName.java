package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandSetName extends CrystalQuestCommand {

    public CommandSetName() {
        super("setname", "commands.setname-usage", 2);

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
                arena = plugin.arenaManager.getArena(Integer.parseInt(arguments[0]) - 1);
            }
            catch (Exception e) {
                arena = plugin.arenaManager.getArena(arguments[0]);
            }

            String oldname = arena.getName();
            boolean bool = arena.setName(arguments[1]);

            if (bool) {
                for (Location loc : plugin.signHandler.getSignLocations()) {
                    Sign s = (Sign)loc.getBlock().getState();
                    if (s.getLine(1).equalsIgnoreCase(oldname)) {
                        s.setLine(1, arena.getName());
                        s.update(true);
                    }
                }

                sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.setname-succeed")
                        .replace("%arena%", Integer.toString(arena.getId() + 1))
                        .replace("%name%", arena.getName()));
            }
            else {
                sender.sendMessage(Broadcast.get("commands.wrong-name"));
            }
        }
        catch (Exception e) {
            sender.sendMessage(Broadcast.get("commands.setname-failed"));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
