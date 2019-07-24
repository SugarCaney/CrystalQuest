package nl.sugcube.crystalquest;

import nl.sugcube.crystalquest.sba.SMethods;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class Broadcast {

    public static CrystalQuest plugin;
    public static String TAG = SMethods.setColours("&7[&dCrystalQuest&7]: &e");
    public static String HELP = SMethods.setColours("&7[&dCQ-?&7]: ");
    public static String HOWDEY = "Howdey!";
    public static String NO_PERMISSION;
    public static String ONLY_IN_GAME;
    public static String PRICE;
    public static String UPGRADE_TO;

    public Broadcast(CrystalQuest instance) {
        plugin = instance;

        TAG = get("general.tag");
        HELP = get("general.tag-help");
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

        sender.sendMessage(TAG + Broadcast.get("general.plugin-made") + " " + ChatColor.GREEN + plugin.getDescription().getAuthors().toString());
        sender.sendMessage(TAG + Broadcast.get("general.plugin-version") + " " + plugin.getDescription().getVersion() + " " + update);
        sender.sendMessage(TAG + Broadcast.get("general.plugin-help"));
    }

    public static void setMessages() {
        NO_PERMISSION = get("broadcast.no-permission");
        ONLY_IN_GAME = get("broadcast.only-in-game");
        PRICE = get("shop.price");
        UPGRADE_TO = get("shop.upgrade-to");
    }

    public static String get(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Cannot get the message for null key.");
        }
        return SMethods.setColours(plugin.getLang().getString(s));
    }

}