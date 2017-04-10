package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.game.Arena;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * @author SugarCaney
 */
public class PlaceLandmine implements Runnable {

    public Block block;
    public Arena arena;
    public Player player;

    public PlaceLandmine(Block b, Arena a, Player p) {
        this.block = b;
        this.arena = a;
        this.player = p;
    }

    public void run() {
        block.setType(Material.STONE_PLATE);
        arena.getGameBlocks().add(block);
    }

}