package nl.sugcube.crystalquest;

import nl.sugcube.crystalquest.sba.SMeth;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class Broadcast {

    public static CrystalQuest plugin;
    public static String TAG = SMeth.setColours("&7[&dCrystalQuest&7]: &e");
    public static String HELP = SMeth.setColours("&7[&dCQ-?&7]: ");
    public static String HOWDEY = "Howdey!";
    public static String NO_PERMISSION;
    public static String ONLY_IN_GAME;

    public Broadcast(CrystalQuest instance) {
        plugin = instance;
    }

    public static void showAbout(CommandSender sender) {
        Update uc = new Update(69421, plugin.getDescription().getVersion());
        String update = "";

        if (uc.query()) {
            update = "(New version avaiable!)";
        }
        else {
            update = "(Up-to-date)";
        }

        sender.sendMessage(TAG + "Plugin made by " + ChatColor.GREEN + plugin.getDescription().getAuthors().toString());
        sender.sendMessage(TAG + "Current version: " + plugin.getDescription().getVersion() + " " + update);
        sender.sendMessage(TAG + "Use " + ChatColor.LIGHT_PURPLE + "/cq help " + ChatColor.YELLOW + "to get a list of commands.");
    }

    public static void setMessages() {
        NO_PERMISSION = SMeth.setColours(plugin.getLang().getString("broadcast.no-permission"));
        ONLY_IN_GAME = SMeth.setColours(plugin.getLang().getString("broadcast.only-in-game"));
    }

    public static String get(String s) {
        return SMeth.setColours(plugin.getLang().getString(s));
    }

}