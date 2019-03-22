package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.CrystalQuest;

/**
 * @author SugarCaney
 */
public class Multipliers {

    public static CrystalQuest plugin;

    public static final double[] MULTIPLIERS_BUFF = { 1, 1.1, 1.2, 1.3, 1.4, 1.5 };
    public static final double[] MULTIPLIERS_DEBUFF = { 1, 1.1, 1.2, 1.3, 1.4, 1.5 };
    public static final double[] MULTIPLIERS_EXPLOSIVE = { 1, 1.1, 1.2, 1.3, 1.4, 1.5 };
    public static final double[] MULTIPLIERS_LIGHTNING = { 0, 0.5, 1, 1.5, 2, 2.5 };
    public static final double[] MULTIPLIERS_AMMO = { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5 };
    public static final double[] MULTIPLIERS_CREEPER = { 0.125, 0.250, 0.375, 0.500, 0.625, 0.750 };
    public static final double[] MULTIPLIERS_CREEPERGEM = { 1, 2, 3, 4, 5, 6 };
    public static final double[] MULTIPLIERS_WOLFRESISTANCE = { 0, 1, 2, 2, 3, 3 };
    public static final double[] MULTIPLIERS_WOLFSTRENGTH = { 1, 1, 1, 2, 2, 3 };
    public static final double[] MULTIPLIERS_XP = { 1, 2, 3, 4, 5, 6 };
    public static final double[] MULTIPLIERS_SMASH = { 0, 0.1, 0.2, 0.3, 0.4, 0.5 };
    public static final double[] MULTIPLIERS_WIN = { 1, 1.1, 1.2, 1.3, 1.4, 1.5 };
    public static final double[] MULTIPLIERS_BLOOD = { 0, 0.2, 0.4, 0.6, 0.8, 1.0 };

    public Multipliers(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * @param type
     *         buff|debuff|explosive|lightning|ammo|creeper|creepergem|wolfstrength|
     *         wolfresistance|xp|smash|win|blood
     * @param level
     *         The level of the upgrade (0-5).
     * @param asPercentage
     *         {@code true} means in range [0,100], {@code false} means in range [0,1].
     * @return The applicable multiplier, or -1 when the type does not exist.
     */
    public static double getMultiplier(String type, int level, boolean asPercentage) {
        double percentageApplier = asPercentage ? 100 : 1;
        double multiplier;
        if (type.equalsIgnoreCase("buff")) {
            multiplier = MULTIPLIERS_BUFF[level];
        }
        else if (type.equalsIgnoreCase("debuff")) {
            multiplier = MULTIPLIERS_DEBUFF[level];
        }
        else if (type.equalsIgnoreCase("explosive")) {
            multiplier = MULTIPLIERS_EXPLOSIVE[level];
        }
        else if (type.equalsIgnoreCase("lightning")) {
            multiplier = MULTIPLIERS_LIGHTNING[level];
        }
        else if (type.equalsIgnoreCase("ammo")) {
            multiplier = MULTIPLIERS_AMMO[level];
        }
        else if (type.equalsIgnoreCase("creeper")) {
            multiplier = MULTIPLIERS_CREEPER[level];
        }
        else if (type.equalsIgnoreCase("creepergem")) {
            multiplier = MULTIPLIERS_CREEPERGEM[level];
        }
        else if (type.equalsIgnoreCase("wolfstrength")) {
            multiplier = MULTIPLIERS_WOLFSTRENGTH[level];
        }
        else if (type.equalsIgnoreCase("wolfresistance")) {
            multiplier = MULTIPLIERS_WOLFRESISTANCE[level];
        }
        else if (type.equalsIgnoreCase("xp")) {
            multiplier = MULTIPLIERS_XP[level];
        }
        else if (type.equalsIgnoreCase("smash")) {
            multiplier = MULTIPLIERS_SMASH[level];
        }
        else if (type.equalsIgnoreCase("win")) {
            multiplier = MULTIPLIERS_WIN[level];
        }
        else if (type.equalsIgnoreCase("blood")) {
            multiplier = MULTIPLIERS_BLOOD[level];
        }
        else {
            return -1;
        }
        return multiplier * percentageApplier;
    }
}