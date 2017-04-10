package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class CommandJoin extends CrystalQuestCommand {

    public CommandJoin() {
        super("join", "commands.join-usage", 1);

        addPermissions(
                "crystalquest.admin",
                "crystalquest.staff",
                "crystalquest.spectate"
        );

        addAutoCompleteMeta(0, AutoCompleteArgument.ARENA);
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        Player player = (Player)sender;
        Arena arena;
        try {
            arena = plugin.am.getArena(Integer.parseInt(arguments[0]) - 1);
        }
        catch (Exception e) {
            arena = plugin.am.getArena(arguments[0]);
        }

        // Check if the arena exists.
        if (arena == null) {
            player.sendMessage(Broadcast.get("arena.no-exist"));
            return;
        }

        // Check if the arena is full.
        if (arena.isFull()) {
            player.sendMessage(Broadcast.get("arena.full"));
            return;
        }

        // Check if the arena is restarting
        if (arena.isEndGame()) {
            player.sendMessage(Broadcast.get("arena.restarting"));
            return;
        }

        // Check if the arena is disabled.
        if (!arena.isEnabled()) {
            player.sendMessage(Broadcast.get("arena.disabled"));
            return;
        }

        // Join arena.
        plugin.menuPT.updateMenu(arena);
        plugin.menuPT.showMenu(player, arena);
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
