package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Teams;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandTeamLobby extends CrystalQuestCommand {

    public CommandTeamLobby() {
        super("teamlobby", "commands.teamlobby-usage", 2);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
        addAutoCompleteMeta(1, AutoCompleteArgument.TEAMS);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        try {
            Arena arena;
            try {
                arena = plugin.am.getArena(Integer.parseInt(arguments[0]) - 1);
            }
            catch (Exception e) {
                arena = plugin.am.getArena(arguments[0]);
            }

            Location[] spawns = arena.getLobbySpawns();
            int teamId = Teams.getTeamId(arguments[1]);
            if (teamId < arena.getTeamCount()) {
                spawns[teamId] = ((Player)sender).getLocation();
                arena.setLobbySpawns(spawns);
                sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.teamlobby-set")
                        .replace("%team%", arguments[1])
                        .replace("%arena%", arguments[0]));
            }
            else {
                sender.sendMessage(Broadcast.get("commands.team-not-exist"));
            }
        }
        catch (Exception e) {
            sender.sendMessage(Broadcast.get("commands.teamlobby-error"));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}