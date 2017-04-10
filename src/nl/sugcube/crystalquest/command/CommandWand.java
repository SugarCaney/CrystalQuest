package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SugarCaney
 */
public class CommandWand extends CrystalQuestCommand {

    public CommandWand() {
        super("wand", null, 0);

        addPermissions(
                "crystalquest.admin"
        );
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;

        ItemStack is = new ItemStack(Material.STICK, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.TAG + "Wand");
        List<String> lore = new ArrayList<>();
        lore.add(Broadcast.get("commands.wand-lore-pos1"));
        lore.add(Broadcast.get("commands.wand-lore-pos2"));
        im.setLore(lore);
        is.setItemMeta(im);

        player.getInventory().addItem(is);
        player.sendMessage(Broadcast.TAG + Broadcast.get("commands.wand"));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
