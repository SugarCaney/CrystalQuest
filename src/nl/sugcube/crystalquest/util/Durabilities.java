package nl.sugcube.crystalquest.util;

/**
 * @author SugarCaney
 */
public enum Durabilities {

    WOOD((short)60),
    STONE((short)132),
    IRON((short)251),
    GOLD((short)33),
    DIAMOND((short)1562),
    LEATHER_HELM((short)56),
    LEATHER_CHEST((short)81),
    LEATHER_PANTS((short)76),
    LEATHER_BOOTS((short)66),
    GOLD_HELM((short)78),
    GOLD_CHEST((short)113),
    GOLD_PANTS((short)106),
    GOLD_BOOTS((short)92),
    CHAIN_HELM((short)166),
    CHAIN_CHEST((short)241),
    CHAIN_PANTS((short)226),
    CHAIN_BOOTS((short)196),
    IRON_HELM((short)166),
    IRON_CHEST((short)241),
    IRON_PANTS((short)226),
    IRON_BOOTS((short)196),
    DIAMOND_HELM((short)364),
    DIAMOND_CHEST((short)529),
    DIAMOND_PANTS((short)496),
    DIAMOND_BOOTS((short)430),
    FISHING_ROD((short)65),
    BOW((short)385),
    FLINT_AND_STEEL((short)65),
    SHEARS((short)239),
    CARROT_ON_STICK((short)65),
    ELYTRA((short)432),
    SHIELD((short)337);

    private final short durability;

    Durabilities(short durability) {
        this.durability = durability;
    }

    public short getDurability() {
        return durability;
    }

}
