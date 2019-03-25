package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.util.BukkitUtil;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author SugarCaney
 */
public class CommandItem extends CrystalQuestCommand {

    public CommandItem() {
        super("item", "commands.item-usage", 1);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ITEMS);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;

        ItemStack item = plugin.itemHandler.getItemByKey(arguments[0]);
        if (item == null) {
            player.sendMessage(Broadcast.get("commands.item-unknown"));
            return;
        }

        // Args > 2, target another player.
        // Args == 1, target the executor.
        if (arguments.length >= 2) {
            Player target = BukkitUtil.getPlayerByName(arguments[1]);
            if (target != null) {
                if (item.getType() == Material.TOTEM_OF_UNDYING) {
                    target.getInventory().setItemInOffHand(item);
                }
                else {
                    target.getInventory().addItem(item);
                }
                return;
            }
        }

        if (item.getType() == Material.TOTEM_OF_UNDYING) {
            player.getInventory().setItemInOffHand(item);
        }
        else {
            player.getInventory().addItem(item);
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
