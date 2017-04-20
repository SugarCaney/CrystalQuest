package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandSetCount extends CrystalQuestCommand {

    public CommandSetCount() {
        super("setcount", "commands.setcount-usage", 1);

        addPermissions(
                "crystalquest.admin",
                "crystalquest.staff",
                "crystalquest.setcount"
        );
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;
        Arena arena = plugin.getArenaManager().getArena(player);

        if (arena == null) {
            return;
        }

        try {
            arena.setCountdown(Math.max(0, Integer.parseInt(arguments[0])));
            sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.setcount-set")
                    .replace("%arena%", arena.getName())
                    .replace("%seconds%", arguments[0]));
        }
        catch (NumberFormatException nfe) {
            sender.sendMessage(Broadcast.get("commands.setcount-usage"));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}