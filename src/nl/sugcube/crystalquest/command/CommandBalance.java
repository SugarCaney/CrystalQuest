package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandBalance extends CrystalQuestCommand {

    public CommandBalance() {
        super("balance", "commands.usage-balance", 0);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = null;

        // Self, when arguments length == 0.
        if (arguments.length == 0) {
            if (sender instanceof Player) {
                player = (Player)sender;
            }
            else {
                sender.sendMessage(Broadcast.ONLY_IN_GAME);
                return;
            }
        }

        // Parse player
        if (player == null) {
            player = Bukkit.getPlayer(arguments[0]);
            if (player == null) {
                sender.sendMessage(Broadcast.get("commands.couldnt-find-player-var")
                        .replace("%p%", arguments[0]));
                return;
            }
        }

        // Display balance
        int balance = plugin.economy.getBalance().getBalance(player, false);
        if (balance < 0) {
            sender.sendMessage(Broadcast.get("commands.couldnt-find-player"));
            return;
        }

        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.balance")
                .replace("%player%", player.getName())
                .replace("%balance%", Integer.toString(balance)));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
