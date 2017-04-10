package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandMoney extends CrystalQuestCommand {

    public CommandMoney() {
        super("money", "commands.usage-money", 3);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.MONEY);
    }

    private void set(CrystalQuest plugin, CommandSender sender, Player player, int amount) {
        plugin.economy.getBalance().setBalance(player, amount);
        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.money")
                .replace("%player%", player.getName())
                .replace("%amount%", Integer.toString(amount)));
    }

    private void give(CrystalQuest plugin, CommandSender sender, Player player, int amount) {
        int previousBalance = plugin.economy.getBalance().getBalance(player, false);
        int newBalance = previousBalance + amount;
        plugin.economy.getBalance().setBalance(player, newBalance);
        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.money-add")
                .replace("%player%", player.getName())
                .replace("%amount%", Integer.toString(amount)));
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        // Parse player
        Player player = Bukkit.getPlayer(arguments[1]);
        if (player == null) {
            sender.sendMessage(Broadcast.get("commands.couldnt-find-player-var")
                    .replace("%p%", arguments[1]));
            return;
        }

        // Parse amount
        int amount;
        try {
            amount = Integer.parseInt(arguments[2]);
        }
        catch (NumberFormatException nfe) {
            sender.sendMessage(Broadcast.get("commands.parse-number-error")
                    .replace("%n%", arguments[2]));
            return;
        }

        // Set
        if (arguments[0].equalsIgnoreCase("set")) {
            set(plugin, sender, player, amount);
        }
        // Give
        else if (arguments[0].equalsIgnoreCase("give") ||
                arguments[0].equalsIgnoreCase("add")) {
            give(plugin, sender, player, amount);
        }
        // Not supported.
        else {
            sender.sendMessage(Broadcast.get(usageNode));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
