package nl.sugcube.crystalquest.economy;

/**
 * @author SugarCaney
 */
public enum ShopUpgrade {

    POWERUP_BUFF("buff"),
    POWERUP_DEBUFF("debuff"),
    POWERUP_EXPLOSIVE("explosive"),
    POWERUP_WEAPONRY("weaponry"),
    POWERUP_CREEPERS("creepers"),
    POWERUP_WOLF("wolf"),
    CRYSTALS_XP("xp"),
    CRYSTALS_SMASH("smash"),
    CRYSTALS_WIN("win"),
    CRYSTALS_BLOOD("blood");

    private String id;

    ShopUpgrade(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * @return null when there is no upgrade with the given id.
     */
    public ShopUpgrade getById(String id) {
        for (ShopUpgrade upgrade : values()) {
            if (upgrade.id == id) {
                return upgrade;
            }
        }
        return null;
    }
}