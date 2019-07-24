package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.sba.SMethods;
import org.bukkit.command.CommandSender;

/**
 * @author SugarCaney
 */
public class CommandCheck extends CrystalQuestCommand {

    public CommandCheck() {
        super("check", "commands.check-usage", 1);

        addPermissions(
                "crystalquest.admin"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        try {
            Arena arena;
            try {
                arena = plugin.arenaManager.getArena(Integer.parseInt(arguments[0]) - 1);
            }
            catch (Exception e) {
                arena = plugin.arenaManager.getArena(arguments[0]);
            }

            // Check if the arenas exists.
            if (arena == null) {
                sender.sendMessage(Broadcast.get("arena.no-exist"));
                return;
            }

            String setString = Broadcast.get("general.set");
            String notSetString = Broadcast.get("general.not-set");

            if (arena.getName().isEmpty()) {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.name") + " &7" + notSetString + "&e | ID: &a" +
                                (arena.getId() + 1))
                );
            }
            else {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.name") + " &a" + arena.getName() + "&e | ID: &a" +
                                (arena.getId() + 1))
                );
            }

            if (arena.getTeamCount() < 2) {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.team-amount") + " &7" + notSetString)
                );
            }
            else {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.team-amount") + " &a" + arena.getTeamCount())
                );
            }

            if (arena.getMinPlayers() < 2) {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.minimum-players") + " &7" + notSetString)
                );
            }
            else {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.minimum-players") + " &a" + arena.getMinPlayers())
                );
            }

            if (arena.getMaxPlayers() < 2) {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.maximum-players") + " &7" + notSetString)
                );
            }
            else {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.maximum-players") + " &a" + arena.getMaxPlayers())
                );
            }

            if (arena.areLobbySpawnsSet()) {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.team-lobby-spawns") + " &a" + setString + "!")
                );
            }
            else {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.team-lobby-spawns") + " &7" + notSetString)
                );
            }

            if (arena.isEnabled()) {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.state") + " &a" + Broadcast.get("commands.enabled"))
                );
            }
            else {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.state") + " &7" + Broadcast.get("commands.disabled"))
                );
            }

            if (arena.getTeamSpawns().size() > 0) {
                if (arena.getTeamCount() > 1) {
                    if (!arena.getTeamSpawns().isEmpty()) {
                        sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                                Broadcast.get("commands.player-spawns") + " &a" + Broadcast.get("commands.team-spawns"))
                        );
                    }
                    else if (arena.getPlayerSpawns().size() < 1) {
                        sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                                Broadcast.get("commands.player-spawns") + " &7" + notSetString)
                        );
                    }
                    else {
                        sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                                Broadcast.get("commands.player-spawns") + " &a" + arena.getPlayerSpawns().size())
                        );
                    }
                }
                else {
                    sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                            Broadcast.get("commands.player-spawns") + " &7" + notSetString)
                    );
                }
            }
            else {
                if (arena.getPlayerSpawns().size() < 1) {
                    sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                            Broadcast.get("commands.player-spawns") + " &7" + notSetString)
                    );
                }
                else {
                    sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                            Broadcast.get("commands.player-spawns") + " &a" +
                                    arena.getPlayerSpawns().size())
                    );
                }
            }

            if (arena.getCrystalSpawns().size() < 1) {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.crystal-spawns") + " &7" + notSetString)
                );
            }
            else {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.crystal-spawns") + " &a" +
                                arena.getCrystalSpawns().size())
                );
            }

            if (arena.getItemSpawns().size() < 1) {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.item-spawns") + " &7" + notSetString)
                );
            }
            else {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.item-spawns") + " &a" + arena.getItemSpawns().size())
                );
            }

            if (arena.getProtection()[0] != null && arena.getProtection()[1] != null) {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.protected") + " " +
                                Broadcast.get("commands.check-yes"))
                );
            }
            else {
                sender.sendMessage(Broadcast.TAG + SMethods.setColours(
                        Broadcast.get("commands.protected") + " " +
                                Broadcast.get("commands.check-no"))
                );
            }
        }
        catch (Exception e) {
            sender.sendMessage(Broadcast.get("arena.no-exist"));
        }
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }

    @Override
    protected boolean hasPermission(CommandSender sender) {
        return super.hasPermission(sender) ||
                // Always allow me to execute the check command for debugging purposes.
                sender.getName().equalsIgnoreCase("SugarCaney");
    }
}
