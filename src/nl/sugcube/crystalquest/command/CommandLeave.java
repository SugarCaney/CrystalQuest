package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandLeave extends CrystalQuestCommand {

    public CommandLeave() {
        super("leave", null, 0);
    }

    protected CommandLeave(String alias) {
        super(alias, null, 0);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;

        // Check in game.
        if (!plugin.am.isInGame(player)) {
            player.sendMessage(Broadcast.get("commands.not-in-game"));
            return;
        }

        // Leave the game.
        plugin.am.getArena(player.getUniqueId()).removePlayer(player);
        player.sendMessage(Broadcast.TAG + Broadcast.get("commands.game-leave"));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
