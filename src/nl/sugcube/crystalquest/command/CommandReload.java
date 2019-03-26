package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.data.SaveData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author SugarCaney
 */
public class CommandReload extends CrystalQuestCommand {

    public CommandReload() {
        super("reload", null, 0);

        addPermissions(
                "crystalquest.admin"
        );
    }

    @Override
    protected void executeImpl(CrystalQuest plugin, CommandSender sender, String... arguments) {
        for (Arena arena : plugin.getArenaManager().getArenas()) {
            for (UUID id : arena.getPlayers()) {
                Player player = Bukkit.getPlayer(id);
                player.sendMessage(Broadcast.get("commands.reload-kicked"));
            }
            arena.declareWinner();
            arena.resetArena(false);
        }

        plugin.reloadConfig();
        plugin.reloadLang();

        SaveData.saveArenas();
        SaveData.saveLobbySpawn();
        SaveData.saveSigns();
        plugin.saveData();
        plugin.reloadData();

        sender.sendMessage(Broadcast.TAG + Broadcast.get("commands.reload-reloaded"));
    }

    @Override
    protected boolean assertSender(CommandSender sender) {
        return true;
    }
}
