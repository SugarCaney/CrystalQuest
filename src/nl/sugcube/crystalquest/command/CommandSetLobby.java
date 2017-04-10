package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandSetLobby extends CrystalQuestCommand {

    public CommandSetLobby() {
        super("setlobby", null, 0);

        addPermissions(
                "crystalquest.admin"
        );
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        plugin.am.setLobby(((Player)sender).getLocation());
        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.lobbyspawn"));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}