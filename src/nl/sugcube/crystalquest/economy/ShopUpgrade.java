package nl.sugcube.crystalquest.economy;

/**
 * @author SugarCaney
 */
public enum ShopUpgrade {

    POWERUP_BUFF("buff", "upgrade"),
    POWERUP_DEBUFF("debuff", "upgrade"),
    POWERUP_EXPLOSIVE("explosive", "upgrade"),
    POWERUP_WEAPONRY("weaponry", "upgrade"),
    POWERUP_CREEPERS("creepers", "upgrade"),
    POWERUP_WOLF("wolf", "upgrade"),
    CRYSTALS_XP("xp", "crystals"),
    CRYSTALS_SMASH("smash", "crystals"),
    CRYSTALS_WIN("win", "crystals"),
    CRYSTALS_BLOOD("blood", "crystals");

    private String id;
    private String type;

    ShopUpgrade(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    /**
     * @return null when there is no upgrade with the given id.
     */
    public static ShopUpgrade getById(String id) {
        for (ShopUpgrade upgrade : values()) {
            if (upgrade.id == id) {
                return upgrade;
            }
        }
        return null;
    }
}