package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandClass extends CrystalQuestCommand {

    public CommandClass() {
        super("class", null, 0);

        addPermissions(
                "crystalquest.admin",
                "crystalquest.staff",
                "crystalquest.changeclass"
        );
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;

        // Check in game.
        if (!plugin.getArenaManager().isInGame(player)) {
            player.sendMessage(Broadcast.get("commands.not-in-game"));
            return;
        }

        // Check spectator
        if (plugin.getArenaManager().isSpectator(player)) {
            player.sendMessage(Broadcast.get("commands.not-in-game"));
            return;
        }

        // Open class selection menu.
        player.sendMessage(Broadcast.TAG + Broadcast.get("commands.class"));
        plugin.menuSelectClass.openMenu(player);
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
