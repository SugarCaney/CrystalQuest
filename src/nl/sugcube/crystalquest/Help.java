package nl.sugcube.crystalquest;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class Help {

    public static void showSetup(CommandSender s) {

        if (s.hasPermission("crystalquest.admin")) {
            s.sendMessage(Broadcast.HELP + ChatColor.YELLOW + "<>--------------" + ChatColor.LIGHT_PURPLE +
                    "SETUP-HELP" + ChatColor.YELLOW + "--------------<>");
            s.sendMessage(Broadcast.HELP + "/cq setlobby" + ChatColor.YELLOW + " " + Broadcast.get("help.setlobby"));
            s.sendMessage(Broadcast.HELP + "/cq createarena [name]" + ChatColor.YELLOW + " " + Broadcast.get("help.createarena"));
            s.sendMessage(Broadcast.HELP + "/cq setname <arena> <name>" + ChatColor.YELLOW + " " + Broadcast.get("help.setname"));
            s.sendMessage(Broadcast.HELP + "/cq teamlobby <arena> <teamID>" + ChatColor.YELLOW + " " + Broadcast.get("help.teamlobby"));
            s.sendMessage(Broadcast.HELP + "/cq setteams <arena> <amount>" + ChatColor.YELLOW +
                    " " + Broadcast.get("help.setteams"));
            s.sendMessage(Broadcast.HELP + "/cq minplayers <arena> <amount>" + ChatColor.YELLOW +
                    " " + Broadcast.get("help.minplayers"));
            s.sendMessage(Broadcast.HELP + "/cq maxplayers <arena> <amount>" + ChatColor.YELLOW +
                    " " + Broadcast.get("help.maxplayers"));
            s.sendMessage(Broadcast.HELP + "/cq spawn <arena> [clear]" + ChatColor.YELLOW +
                    " " + Broadcast.get("help.spawn"));
            s.sendMessage(Broadcast.HELP + "/cq teamspawn <arena> <team> [clear]" +
                    ChatColor.YELLOW + " " + Broadcast.get("help.teamspawn"));
            s.sendMessage(Broadcast.HELP + "/cq crystalspawn <arena> [clear]" + ChatColor.YELLOW +
                    " " + Broadcast.get("help.crystalspawn"));
            s.sendMessage(Broadcast.HELP + "/cq itemspawn <arena> [clear]" + ChatColor.YELLOW +
                    " " + Broadcast.get("help.itemspawn"));
            s.sendMessage(Broadcast.HELP + "/cq doublejump <arena>" + ChatColor.YELLOW + " " + Broadcast.get("help.doublejump"));
            s.sendMessage(Broadcast.HELP + "/cq check <arena>" + ChatColor.YELLOW + " " + Broadcast.get("help.check"));
            s.sendMessage(Broadcast.HELP + "/cq reset <arena>" + ChatColor.YELLOW + " " + Broadcast.get("help.reset"));
            s.sendMessage(Broadcast.HELP + "/cq wand" + ChatColor.YELLOW + " " + Broadcast.get("help.wand"));
            s.sendMessage(Broadcast.HELP + "/cq protect <arena> [remove]" + ChatColor.YELLOW + " " + Broadcast.get("help.protect"));
            s.sendMessage(Broadcast.HELP + "/cq pos <1|2>" + ChatColor.YELLOW + " " + Broadcast.get("help.pos"));
        }
        if (!(s instanceof Player)) {
            s.sendMessage(Broadcast.HELP + "/cq hardreset" + ChatColor.YELLOW + " Resets ALL data");
        }

    }

    public static void showDefault(CommandSender s) {

        s.sendMessage(Broadcast.HELP + ChatColor.YELLOW + "<>--------------" + ChatColor.LIGHT_PURPLE +
                "CQ-HELP" + ChatColor.YELLOW + "--------------<>");
        s.sendMessage(Broadcast.HELP + "/cq lobby" + ChatColor.YELLOW + " " + Broadcast.get("help.lobby"));
        s.sendMessage(Broadcast.HELP + "/cq quit" + ChatColor.YELLOW + " " + Broadcast.get("help.quit"));
        s.sendMessage(Broadcast.HELP + "/cq balance <player>" + ChatColor.YELLOW + " " + Broadcast.get("help.balance"));
        s.sendMessage(Broadcast.HELP + "/cq shop" + ChatColor.YELLOW + " " + Broadcast.get("help.shop"));

        if (s.hasPermission("crystalquest.join")) {
            s.sendMessage(Broadcast.HELP + "/cq join <arena>" + ChatColor.YELLOW + " " + Broadcast.get("help.join"));
        }
        if (s.hasPermission("crystalquest.changeclass") || s.hasPermission("crystalquest.staff") ||
                s.hasPermission("crystalquest.admin")) {
            s.sendMessage(Broadcast.HELP + "/cq class" + ChatColor.YELLOW + " " + Broadcast.get("help.class"));
        }
        if (s.hasPermission("crystalquest.tp")) {
            s.sendMessage(Broadcast.HELP + "/cq tp <arena>" + ChatColor.YELLOW + " " + Broadcast.get("help.tp"));
        }
        if (s.hasPermission("crystalquest.enable") || s.hasPermission("crystalquest.staff") ||
                s.hasPermission("crystalquest.admin")) {
            s.sendMessage(Broadcast.HELP + "/cq enable <arena>" + ChatColor.YELLOW + " " + Broadcast.get("help.enable"));
        }
        if (s.hasPermission("crystalquest.disable") || s.hasPermission("crystalquest.staff") ||
                s.hasPermission("crystalquest.admin")) {
            s.sendMessage(Broadcast.HELP + "/cq disable <arena>" + ChatColor.YELLOW + " " + Broadcast.get("help.disable"));
        }
        if (s.hasPermission("crystalquest.forcestart") || s.hasPermission("crystalquest.staff") ||
                s.hasPermission("crystalquest.admin")) {
            s.sendMessage(Broadcast.HELP + "/cq forcestart <arena>" + ChatColor.YELLOW + " " + Broadcast.get("help.forcestart"));
        }
        if (s.hasPermission("crystalquest.kick") || s.hasPermission("crystalquest.staff") ||
                s.hasPermission("crystalquest.admin")) {
            s.sendMessage(Broadcast.HELP + "/cq kick <player>" + ChatColor.YELLOW + " " + Broadcast.get("help.kick"));
        }
        if (s.hasPermission("crystalquest.admin")) {
            s.sendMessage(Broadcast.HELP + "/cq money <player> <amount>" + ChatColor.YELLOW + " " + Broadcast.get("help.money"));
        }
        if (s.hasPermission("crystalquest.admin")) {
        	s.sendMessage(Broadcast.HELP + "/cq item <item-key>" + ChatColor.YELLOW + " " + Broadcast.get("help.item"));
        }
        if (s.hasPermission("crystalquest.admin")) {
            s.sendMessage(Broadcast.HELP + "/cq reload" + ChatColor.YELLOW + " " + Broadcast.get("help.reload"));
            s.sendMessage(Broadcast.HELP + ChatColor.YELLOW + Broadcast.get("help.other"));
        }

    }

}