package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandLobby extends CrystalQuestCommand {

    public CommandLobby() {
        super("lobby", null, 0);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;

        // Check in game.
        if (plugin.am.isInGame(player)) {
            sender.sendMessage(Broadcast.get("commands.lobby-already-ingame"));
            return;
        }

        player.teleport(plugin.am.getLobby());
        player.sendMessage(Broadcast.TAG + Broadcast.get("commands.lobby-tp"));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
