package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandTeamSpawn extends CrystalQuestCommand {

    public CommandTeamSpawn() {
        super("teamspawn", "commands.teamspawn-usage", 2);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
        addAutoCompleteMeta(1, AutoCompleteArgument.TEAMS);
        addAutoCompleteMeta(2, AutoCompleteArgument.CLEAR);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Arena arena;
        try {
            arena = plugin.am.getArena(Integer.parseInt(arguments[0]) - 1);
        }
        catch (Exception e) {
            arena = plugin.am.getArena(arguments[0]);
        }

        // Check if the arenas exists.
        if (arena == null) {
            sender.sendMessage(Broadcast.get("arena.no-exist"));
            return;
        }

        boolean canContinue = true;
        if (arguments.length >= 3) {
            try {
                // Clear
                if (arguments[2].equalsIgnoreCase("clear")) {
                    CrystalQuestTeam team = CrystalQuestTeam.valueOfName(arguments[1]);
                    if (arena.hasTeam(team)) {
                        arena.getTeamSpawns().get(team).clear();
                        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.teamspawn-clear")
                                .replace("%arena%", arguments[0])
                                .replace("%team%", arguments[1]));
                    }
                    else {
                        sender.sendMessage(Broadcast.get("commands.teamspawn-team-doesnt-exist"));
                    }
                }
            }
            catch (Exception e) {
                sender.sendMessage(Broadcast.get("commands.teamspawn-usage"));
            }
            finally {
                canContinue = false;
            }
        }

        // Add
        if (canContinue) {
            CrystalQuestTeam team = CrystalQuestTeam.valueOfName(arguments[1]);
            if (arena.hasTeam(team)) {
                arena.getTeamSpawns().get(team).add(((Player)sender).getLocation());
                sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.teamspawn-added")
                        .replace("%no%", arena.getTeamSpawns().get(team).size() + "")
                        .replace("%arena%", arguments[0])
                        .replace("%team%", arguments[1]));
            }
            else {
                sender.sendMessage(Broadcast.get("commands.teamspawn-team-doesnt-exist"));
            }
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
