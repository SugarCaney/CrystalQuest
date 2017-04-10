package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandShop extends CrystalQuestCommand {

    public CommandShop() {
        super("shop", null, 0);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;
        plugin.economy.getMainMenu().showMenu(player);
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
