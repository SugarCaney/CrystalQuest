package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SugarCaney
 */
public class CrystalQuestCommandManager implements CommandExecutor, TabCompleter {

    // Initialise all commands.
    private static final List<CrystalQuestCommand> COMMANDS;
    static {
        COMMANDS = new ArrayList<>();
        Collections.addAll(COMMANDS,
                new CommandSpectate(),
                new CommandJoin(),
                new CommandClass(),
                new CommandLeave(),
                new CommandQuit(),
                new CommandMoney(),
                new CommandBalance(),
                new CommandShop(),
                new CommandCreateArena(),
                new CommandEnable(),
                new CommandDisable(),
                new CommandTeamLobby(),
                new CommandForceStart(),
                new CommandSetName(),
                new CommandSpawn(),
                new CommandCrystalSpawn(),
                new CommandItemSpawn(),
                new CommandTeamSpawn(),
                new CommandKick(),
                new CommandMinPlayers(),
                new CommandMaxPlayers(),
                new CommandSetTeams(),
                new CommandList(),
                new CommandHowdey(),
                new CommandCheck(),
                new CommandLobby(),
                new CommandReload(),
                new CommandReset(),
                new CommandHelp(),
                new CommandWand(),
                new CommandProtect(),
                new CommandPos(),
                new CommandDoubleJump(),
                new CommandTp(),
                new CommandSetLobby()
        );
    }

    public static CrystalQuest plugin;
    public boolean askedHardReset = false;

    public CrystalQuestCommandManager(CrystalQuest instance) {
        plugin = instance;
    }

    /*
     * Fired if someone uses the /cq command
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            // Execute command if present.
            final String[] commandArguments = Arrays.copyOfRange(args, 1, args.length);
            Optional<CrystalQuestCommand> cqCommand = COMMANDS.stream()
                    .filter(cmd -> cmd.getName().equalsIgnoreCase(args[0]))
                    .findFirst();
            if (cqCommand.isPresent()) {
                cqCommand.get().execute(plugin, sender, commandArguments);
                return true;
            }

            /*
             * HARDRESET
			 */
            if (args[0].equalsIgnoreCase("hardreset")) {
                if (sender instanceof ConsoleCommandSender) {
                    plugin.getLogger().info("Would you really like to delete ALL data (with exception of the data.yml)? " +
                            "Please mind that there is no way back! If you really would like to remove all data," +
                            " please use the command 'cq yes' to confirm.");
                    this.askedHardReset = true;
                }
                else {
                    sender.sendMessage(Broadcast.NO_PERMISSION);
                }
            }
            /*
             * YES
			 */
            else if (args[0].equalsIgnoreCase("yes")) {
                if (sender instanceof ConsoleCommandSender) {
                    if (this.askedHardReset) {
                        File file = new File(plugin.getDataFolder() + File.separator + "data.yml");
                        try {
                            PrintWriter pw = new PrintWriter(file);
                            pw.print("");
                            pw.close();
                            plugin.reloadData();
                            plugin.getLogger().info("All data has been destructed!");
                            plugin.am.arenas.clear();
                        }
                        catch (FileNotFoundException e) {
                            plugin.getLogger().info("Could not destroy data!");
                        }
                        this.askedHardReset = false;
                    }
                    else {
                        plugin.getLogger().info("No effect!");
                    }
                }
                else {
                    sender.sendMessage(Broadcast.NO_PERMISSION);
                }
            }
            // Otherwise, send error message.
            else if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + Broadcast.get("commands.invalid"));
            }
            else {
                plugin.getLogger().info(Broadcast.get("commands.invalid"));
            }
        }
        else {
            Broadcast.showAbout(sender);
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias,
                                      String[] args) {
        // Main command suggestions
        if (args.length <= 1) {
            return COMMANDS.stream()
                    .filter(cmd -> cmd.hasPermission(sender))
                    .map(CrystalQuestCommand::getName)
                    .filter(name -> args.length <= 0 ||
                            name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Find the corresponding command.
        final String[] commandArguments = Arrays.copyOfRange(args, 1, args.length);
        Optional<CrystalQuestCommand> cqCommand = COMMANDS.stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(args[0]))
                .findFirst();
        if (!cqCommand.isPresent()) {
            return null;
        }

        // Get auto complete.
        return cqCommand.get().getAutoComplete(args.length - 2)
                .map(type -> type.options(args[args.length - 1], plugin))
                .orElse(null);
    }
}