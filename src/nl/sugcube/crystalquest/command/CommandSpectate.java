package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandSpectate extends CrystalQuestCommand {

    public CommandSpectate() {
        super("spectate", "commands.spectate-usage", 1);

        addPermissions(
                "crystalquest.admin",
                "crystalquest.staff",
                "crystalquest.spectate"
        );
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;
        try {
            Arena arena;
            try {
                arena = plugin.arenaManager.getArena(Integer.parseInt(arguments[0]) - 1);
            }
            catch (Exception e) {
                arena = plugin.arenaManager.getArena(arguments[0]);
            }
            arena.addPlayer(player, null, true);
        }
        catch (Exception e) {
            sender.sendMessage(Broadcast.get("commands.spectate-error"));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
