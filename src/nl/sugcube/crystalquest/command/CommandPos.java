package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandPos extends CrystalQuestCommand {

    public CommandPos() {
        super("pos", "commands.pos-usage", 1);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.POSITIONS);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;
        String position = arguments[0];

        // Position 1.
        if ("1".equalsIgnoreCase(position)) {
            plugin.prot.pos1 = player.getLocation();
            sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.pos-set")
                    .replace("%pos%", arguments[0])
                    .replace("%coords%", String.format("(%.1f, %.1f, %.1f)",
                            plugin.prot.pos1.getX(),
                            plugin.prot.pos1.getY(),
                            plugin.prot.pos1.getZ()
                    )));
        }
        // Position 2.
        else if ("2".equalsIgnoreCase(position)) {
            plugin.prot.pos2 = player.getLocation();
            sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.pos-set")
                    .replace("%pos%", arguments[0])
                    .replace("%coords%", String.format("(%.1f, %.1f, %.1f)",
                            plugin.prot.pos2.getX(),
                            plugin.prot.pos2.getY(),
                            plugin.prot.pos2.getZ()
                    )));
        }
        // Unknown position.
        else {
            sender.sendMessage(Broadcast.get(usageNode));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
