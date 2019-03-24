package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.util.BukkitUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandKick extends CrystalQuestCommand {

    public CommandKick() {
        super("kick", "commands.kick-usage", 1);

        addPermissions(
                "crystalquest.admin",
                "crystalquest.staff",
                "crystalquest.kick"
        );
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        // Get player.
        Player player = BukkitUtil.getPlayerByName(arguments[0]);
        if (player == null) {
            sender.sendMessage(Broadcast.get("commands.kick-not-online")
                    .replace("%player%", arguments[0]));
            return;
        }

        // Check in game.
        if (!plugin.arenaManager.isInGame(player)) {
            sender.sendMessage(Broadcast.get("commands.kick-not-ingame")
                    .replace("%player%", player.getName()));
            return;
        }

        // Kick player.
        plugin.arenaManager.getArena(player.getUniqueId()).removePlayer(player);
        player.sendMessage(Broadcast.TAG + Broadcast.get("commands.kick-kicked"));
        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.kick-you-kicked")
                .replace("%player%", player.getName()));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
