package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * A crystal quest command is a subcommand of the /cq command. Confusing? Don't think so.
 * <p>
 * For example "spectate [arena]" is a CrystalQuest command.
 *
 * @author SugarCaney
 */
public abstract class CrystalQuestCommand {

    /**
     * The name of the command as it appears after the `/cq` command.
     */
    protected final String name;

    /**
     * The node in the language file that displays the usage message.
     */
    protected final String usageNode;

    /**
     * The minimum amount of arguments that are expected for the command.
     */
    protected final int argumentCount;

    /**
     * Permissions required to issue the command.
     * <p>
     * Only one of the permissions in this list is required in order to be allowed to execute the
     * command. When the set is empty, it means that everyone is allowed to execute the command.
     */
    protected final Set<String> permissions;

    /**
     * Contains what type of content must be auto completed at what index of the command.
     * <p>
     * Maps the index of the argument (NOT including the command name itself) to the type of
     * stuff that needs to be autocompleted. This map only contains arguments that must be auto
     * completed.
     */
    protected final Map<Integer, AutoCompleteArgument> autoCompleteMeta;

    /**
     * @param name
     *         The name of the command after `/cq`
     * @param usageNode
     *         The node of the usage-message in the language file.
     * @param argumentCount
     *         The amount of arguments are expected for the command.
     */
    public CrystalQuestCommand(String name, String usageNode, int argumentCount) {
        this.name = name;
        this.usageNode = usageNode;
        this.argumentCount = argumentCount;
        this.permissions = new HashSet<>();
        this.autoCompleteMeta = new HashMap<>();
    }

    /**
     * Executes the given command.
     *
     * @param plugin
     *         The main CrystalQuest plugin instance.
     * @param sender
     *         The one who executed the command.
     * @param arguments
     *         The arguments that appear after `/cq {@code name}`.
     */
    public final void execute(CrystalQuest plugin, CommandSender sender, String... arguments) {
        // Check permissions.
        if (!hasPermission(sender)) {
            sender.sendMessage(Broadcast.NO_PERMISSION);
            return;
        }

        // Check sender.
        if (!assertSender(sender)) {
            sender.sendMessage(Broadcast.ONLY_IN_GAME);
            return;
        }

        // Check argument count.
        if (arguments.length < argumentCount) {
            sender.sendMessage(Broadcast.get(usageNode));
            return;
        }

        // Execute command
        executeImpl(plugin, sender, arguments);
    }

    /**
     * Get what type must be autocompleted at the given index.
     *
     * @param index
     *         The argument index.
     */
    public Optional<AutoCompleteArgument> getAutoComplete(int index) {
        return Optional.ofNullable(autoCompleteMeta.get(index));
    }

    /**
     * Get the name of the command i.e. what is typed after `\cq`.
     */
    public String getName() {
        return name;
    }

    /**
     * Implementation of the command execution.
     *
     * @param plugin
     *         The main CrystalQuest plugin instance.
     * @param sender
     *         The one who executed the command.
     * @param arguments
     *         The arguments that appear after `/cq {@code name}`.
     */
    protected abstract void executeImpl(CrystalQuest plugin, CommandSender sender,
                                        String... arguments);

    /**
     * Checks whether the given CommandSender may execute the command or not.
     *
     * @return Whether the command sender may execute the command or not.
     */
    abstract protected boolean assertSender(CommandSender sender);

    /**
     * Adds the given permissions to the command.
     *
     * @see CrystalQuestCommand#permissions
     */
    protected void addPermissions(String... permissions) {
        Collections.addAll(this.permissions, permissions);
    }

    /**
     * Checks whether the given sender has enough permissions to execute the command.
     *
     * @return Whether the sender has enough permissions to execute the command.
     */
    protected boolean hasPermission(CommandSender sender) {
        if (permissions.isEmpty()) {
            return true;
        }

        for (String permission : permissions) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @see CrystalQuestCommand#autoCompleteMeta
     */
    protected void addAutoCompleteMeta(int index, AutoCompleteArgument argumentType) {
        autoCompleteMeta.put(index, argumentType);
    }
}
