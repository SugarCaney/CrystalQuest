package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.CrystalQuest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author SugarCaney
 */
public abstract class ItemExecutor {

    /**
     * The material that when clicked executes this.
     */
    protected Material triggerMaterial;

    /**
     * The durability the item must have in order to be triggeted.
     * <p>
     * Defaults to zero.
     */
    protected short triggerDurability;

    /**
     * {@code triggerDurability = 0}.
     *
     * @param triggerMaterial
     *         The material that triggers the item's execution.
     */
    public ItemExecutor(Material triggerMaterial) {
        this(triggerMaterial, (short)0);
    }

    /**
     * @param triggerMaterial
     *         The material that triggers the item's execution.
     * @param triggerDurability
     *         The durability that the item must have in order for it to trigger.
     */
    public ItemExecutor(Material triggerMaterial, short triggerDurability) {
        this.triggerMaterial = triggerMaterial;
        this.triggerDurability = triggerDurability;
    }

    /**
     * Executes the ItemAction.
     *
     * @param plugin
     *         The CrystalQuest plugin.
     * @param player
     *         The player that executes the item.
     * @param itemStack
     *         The actual item that was clicked in the inventory.
     * @return Whether the action has been executed correctly or not.
     */
    abstract boolean execute(CrystalQuest plugin, Player player, ItemStack itemStack);

    /**
     * Removes an item from the given players inventory.
     *
     * @param plugin
     *         The CrystalQuest plugin.
     * @param player
     *         The player whose item should be removed.
     * @param itemStack
     *         The itemstack of the item to be removed.
     */
    public void removeItem(CrystalQuest plugin, Player player, ItemStack itemStack) {
        if (itemStack.getAmount() == 1) {
            player.getInventory().removeItem(itemStack);
        }
        else {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
    }

    /**
     * Get the material that triggers the item execution.
     */
    public Material getTriggerMaterial() {
        return triggerMaterial;
    }

    /**
     * Get the durability the item must have in order to be triggeted.
     */
    public short getTriggerDurability() {
        return triggerDurability;
    }
}
