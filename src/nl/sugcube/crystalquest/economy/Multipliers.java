package nl.sugcube.crystalquest.economy;

import nl.sugcube.crystalquest.CrystalQuest;

/**
 * @author SugarCaney
 */
public class Multipliers {

    public static CrystalQuest plugin;

    public static final double MULTIPLIER_BUFF_LVL_0 = 1;
    public static final double MULTIPLIER_BUFF_LVL_1 = 1.1;
    public static final double MULTIPLIER_BUFF_LVL_2 = 1.2;
    public static final double MULTIPLIER_BUFF_LVL_3 = 1.3;
    public static final double MULTIPLIER_BUFF_LVL_4 = 1.4;
    public static final double MULTIPLIER_BUFF_LVL_5 = 1.5;

    public static final double MULTIPLIER_DEBUFF_LVL_0 = 1;
    public static final double MULTIPLIER_DEBUFF_LVL_1 = 1.1;
    public static final double MULTIPLIER_DEBUFF_LVL_2 = 1.2;
    public static final double MULTIPLIER_DEBUFF_LVL_3 = 1.3;
    public static final double MULTIPLIER_DEBUFF_LVL_4 = 1.4;
    public static final double MULTIPLIER_DEBUFF_LVL_5 = 1.5;

    public static final double MULTIPLIER_EXPLOSIVE_LVL_0 = 1;
    public static final double MULTIPLIER_EXPLOSIVE_LVL_1 = 1.1;
    public static final double MULTIPLIER_EXPLOSIVE_LVL_2 = 1.2;
    public static final double MULTIPLIER_EXPLOSIVE_LVL_3 = 1.3;
    public static final double MULTIPLIER_EXPLOSIVE_LVL_4 = 1.4;
    public static final double MULTIPLIER_EXPLOSIVE_LVL_5 = 1.5;

    public static final double MULTIPLIER_LIGHTNING_LVL_0 = 0;
    public static final double MULTIPLIER_LIGHTNING_LVL_1 = 0.5;
    public static final double MULTIPLIER_LIGHTNING_LVL_2 = 1;
    public static final double MULTIPLIER_LIGHTNING_LVL_3 = 1.5;
    public static final double MULTIPLIER_LIGHTNING_LVL_4 = 2;
    public static final double MULTIPLIER_LIGHTNING_LVL_5 = 2.5;

    public static final double MULTIPLIER_AMMO_LVL_0 = 0.0;
    public static final double MULTIPLIER_AMMO_LVL_1 = 0.1;
    public static final double MULTIPLIER_AMMO_LVL_2 = 0.2;
    public static final double MULTIPLIER_AMMO_LVL_3 = 0.3;
    public static final double MULTIPLIER_AMMO_LVL_4 = 0.4;
    public static final double MULTIPLIER_AMMO_LVL_5 = 0.5;

    public static final double MULTIPLIER_CREEPER_LVL_0 = 0.125;
    public static final double MULTIPLIER_CREEPER_LVL_1 = 0.250;
    public static final double MULTIPLIER_CREEPER_LVL_2 = 0.375;
    public static final double MULTIPLIER_CREEPER_LVL_3 = 0.500;
    public static final double MULTIPLIER_CREEPER_LVL_4 = 0.625;
    public static final double MULTIPLIER_CREEPER_LVL_5 = 0.750;

    public static final double MULTIPLIER_CREEPERGEM_LVL_0 = 1;
    public static final double MULTIPLIER_CREEPERGEM_LVL_1 = 2;
    public static final double MULTIPLIER_CREEPERGEM_LVL_2 = 3;
    public static final double MULTIPLIER_CREEPERGEM_LVL_3 = 4;
    public static final double MULTIPLIER_CREEPERGEM_LVL_4 = 5;
    public static final double MULTIPLIER_CREEPERGEM_LVL_5 = 6;

    public static final double MULTIPLIER_WOLFRESISTANCE_LVL_0 = 0;
    public static final double MULTIPLIER_WOLFRESISTANCE_LVL_1 = 1;
    public static final double MULTIPLIER_WOLFRESISTANCE_LVL_2 = 2;
    public static final double MULTIPLIER_WOLFRESISTANCE_LVL_3 = 2;
    public static final double MULTIPLIER_WOLFRESISTANCE_LVL_4 = 3;
    public static final double MULTIPLIER_WOLFRESISTANCE_LVL_5 = 3;

    public static final double MULTIPLIER_WOLFSTRENGTH_LVL_0 = 1;
    public static final double MULTIPLIER_WOLFSTRENGTH_LVL_1 = 1;
    public static final double MULTIPLIER_WOLFSTRENGTH_LVL_2 = 1;
    public static final double MULTIPLIER_WOLFSTRENGTH_LVL_3 = 2;
    public static final double MULTIPLIER_WOLFSTRENGTH_LVL_4 = 2;
    public static final double MULTIPLIER_WOLFSTRENGTH_LVL_5 = 3;

    public static final double MULTIPLIER_XP_LVL_0 = 1;
    public static final double MULTIPLIER_XP_LVL_1 = 2;
    public static final double MULTIPLIER_XP_LVL_2 = 3;
    public static final double MULTIPLIER_XP_LVL_3 = 4;
    public static final double MULTIPLIER_XP_LVL_4 = 5;
    public static final double MULTIPLIER_XP_LVL_5 = 6;

    public static final double MULTIPLIER_SMASH_LVL_0 = 0;
    public static final double MULTIPLIER_SMASH_LVL_1 = 0.1;
    public static final double MULTIPLIER_SMASH_LVL_2 = 0.2;
    public static final double MULTIPLIER_SMASH_LVL_3 = 0.3;
    public static final double MULTIPLIER_SMASH_LVL_4 = 0.4;
    public static final double MULTIPLIER_SMASH_LVL_5 = 0.5;

    public static final double MULTIPLIER_WIN_LVL_0 = 1;
    public static final double MULTIPLIER_WIN_LVL_1 = 1.1;
    public static final double MULTIPLIER_WIN_LVL_2 = 1.2;
    public static final double MULTIPLIER_WIN_LVL_3 = 1.3;
    public static final double MULTIPLIER_WIN_LVL_4 = 1.4;
    public static final double MULTIPLIER_WIN_LVL_5 = 1.5;

    public static final double MULTIPLIER_BLOOD_LVL_0 = 0;
    public static final double MULTIPLIER_BLOOD_LVL_1 = 0.2;
    public static final double MULTIPLIER_BLOOD_LVL_2 = 0.4;
    public static final double MULTIPLIER_BLOOD_LVL_3 = 0.6;
    public static final double MULTIPLIER_BLOOD_LVL_4 = 0.8;
    public static final double MULTIPLIER_BLOOD_LVL_5 = 1.0;

    public Multipliers(CrystalQuest instance) {
        plugin = instance;
    }

    public static double getMultiplier(String type, int level, boolean asPercentage) {

        if (type.equalsIgnoreCase("buff")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_BUFF_LVL_0 * 100 : MULTIPLIER_BUFF_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_BUFF_LVL_1 * 100 : MULTIPLIER_BUFF_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_BUFF_LVL_2 * 100 : MULTIPLIER_BUFF_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_BUFF_LVL_3 * 100 : MULTIPLIER_BUFF_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_BUFF_LVL_4 * 100 : MULTIPLIER_BUFF_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_BUFF_LVL_5 * 100 : MULTIPLIER_BUFF_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_BUFF_LVL_0 * 100 : MULTIPLIER_BUFF_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("debuff")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_DEBUFF_LVL_0 * 100 : MULTIPLIER_DEBUFF_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_DEBUFF_LVL_1 * 100 : MULTIPLIER_DEBUFF_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_DEBUFF_LVL_2 * 100 : MULTIPLIER_DEBUFF_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_DEBUFF_LVL_3 * 100 : MULTIPLIER_DEBUFF_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_DEBUFF_LVL_4 * 100 : MULTIPLIER_DEBUFF_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_DEBUFF_LVL_5 * 100 : MULTIPLIER_DEBUFF_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_DEBUFF_LVL_0 * 100 : MULTIPLIER_DEBUFF_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("explosive")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_EXPLOSIVE_LVL_0 * 100 : MULTIPLIER_EXPLOSIVE_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_EXPLOSIVE_LVL_1 * 100 : MULTIPLIER_EXPLOSIVE_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_EXPLOSIVE_LVL_2 * 100 : MULTIPLIER_EXPLOSIVE_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_EXPLOSIVE_LVL_3 * 100 : MULTIPLIER_EXPLOSIVE_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_EXPLOSIVE_LVL_4 * 100 : MULTIPLIER_EXPLOSIVE_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_EXPLOSIVE_LVL_5 * 100 : MULTIPLIER_EXPLOSIVE_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_EXPLOSIVE_LVL_0 * 100 : MULTIPLIER_EXPLOSIVE_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("lightning")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_LIGHTNING_LVL_0 * 100 : MULTIPLIER_LIGHTNING_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_LIGHTNING_LVL_1 * 100 : MULTIPLIER_LIGHTNING_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_LIGHTNING_LVL_2 * 100 : MULTIPLIER_LIGHTNING_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_LIGHTNING_LVL_3 * 100 : MULTIPLIER_LIGHTNING_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_LIGHTNING_LVL_4 * 100 : MULTIPLIER_LIGHTNING_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_LIGHTNING_LVL_5 * 100 : MULTIPLIER_LIGHTNING_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_LIGHTNING_LVL_0 * 100 : MULTIPLIER_LIGHTNING_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("ammo")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_AMMO_LVL_0 * 100 : MULTIPLIER_AMMO_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_AMMO_LVL_1 * 100 : MULTIPLIER_AMMO_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_AMMO_LVL_2 * 100 : MULTIPLIER_AMMO_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_AMMO_LVL_3 * 100 : MULTIPLIER_AMMO_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_AMMO_LVL_4 * 100 : MULTIPLIER_AMMO_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_AMMO_LVL_5 * 100 : MULTIPLIER_AMMO_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_AMMO_LVL_0 * 100 : MULTIPLIER_AMMO_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("creeper")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_CREEPER_LVL_0 * 100 : MULTIPLIER_CREEPER_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_CREEPER_LVL_1 * 100 : MULTIPLIER_CREEPER_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_CREEPER_LVL_2 * 100 : MULTIPLIER_CREEPER_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_CREEPER_LVL_3 * 100 : MULTIPLIER_CREEPER_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_CREEPER_LVL_4 * 100 : MULTIPLIER_CREEPER_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_CREEPER_LVL_5 * 100 : MULTIPLIER_CREEPER_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_CREEPER_LVL_0 * 100 : MULTIPLIER_CREEPER_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("creepergem")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_CREEPERGEM_LVL_0 * 100 : MULTIPLIER_CREEPERGEM_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_CREEPERGEM_LVL_1 * 100 : MULTIPLIER_CREEPERGEM_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_CREEPERGEM_LVL_2 * 100 : MULTIPLIER_CREEPERGEM_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_CREEPERGEM_LVL_3 * 100 : MULTIPLIER_CREEPERGEM_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_CREEPERGEM_LVL_4 * 100 : MULTIPLIER_CREEPERGEM_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_CREEPERGEM_LVL_5 * 100 : MULTIPLIER_CREEPERGEM_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_CREEPERGEM_LVL_0 * 100 : MULTIPLIER_CREEPERGEM_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("wolfstrength")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_WOLFSTRENGTH_LVL_0 * 100 : MULTIPLIER_WOLFSTRENGTH_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_WOLFSTRENGTH_LVL_1 * 100 : MULTIPLIER_WOLFSTRENGTH_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_WOLFSTRENGTH_LVL_2 * 100 : MULTIPLIER_WOLFSTRENGTH_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_WOLFSTRENGTH_LVL_3 * 100 : MULTIPLIER_WOLFSTRENGTH_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_WOLFSTRENGTH_LVL_4 * 100 : MULTIPLIER_WOLFSTRENGTH_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_WOLFSTRENGTH_LVL_5 * 100 : MULTIPLIER_WOLFSTRENGTH_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_WOLFSTRENGTH_LVL_0 * 100 : MULTIPLIER_WOLFSTRENGTH_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("wolfresistance")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_WOLFRESISTANCE_LVL_0 * 100 : MULTIPLIER_WOLFRESISTANCE_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_WOLFRESISTANCE_LVL_1 * 100 : MULTIPLIER_WOLFRESISTANCE_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_WOLFRESISTANCE_LVL_2 * 100 : MULTIPLIER_WOLFRESISTANCE_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_WOLFRESISTANCE_LVL_3 * 100 : MULTIPLIER_WOLFRESISTANCE_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_WOLFRESISTANCE_LVL_4 * 100 : MULTIPLIER_WOLFRESISTANCE_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_WOLFRESISTANCE_LVL_5 * 100 : MULTIPLIER_WOLFRESISTANCE_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_WOLFRESISTANCE_LVL_0 * 100 : MULTIPLIER_WOLFRESISTANCE_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("xp")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_XP_LVL_0 * 100 : MULTIPLIER_XP_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_XP_LVL_1 * 100 : MULTIPLIER_XP_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_XP_LVL_2 * 100 : MULTIPLIER_XP_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_XP_LVL_3 * 100 : MULTIPLIER_XP_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_XP_LVL_4 * 100 : MULTIPLIER_XP_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_XP_LVL_5 * 100 : MULTIPLIER_XP_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_XP_LVL_0 * 100 : MULTIPLIER_XP_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("smash")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_SMASH_LVL_0 * 100 : MULTIPLIER_SMASH_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_SMASH_LVL_1 * 100 : MULTIPLIER_SMASH_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_SMASH_LVL_2 * 100 : MULTIPLIER_SMASH_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_SMASH_LVL_3 * 100 : MULTIPLIER_SMASH_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_SMASH_LVL_4 * 100 : MULTIPLIER_SMASH_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_SMASH_LVL_5 * 100 : MULTIPLIER_SMASH_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_SMASH_LVL_0 * 100 : MULTIPLIER_SMASH_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("win")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_WIN_LVL_0 * 100 : MULTIPLIER_WIN_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_WIN_LVL_1 * 100 : MULTIPLIER_WIN_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_WIN_LVL_2 * 100 : MULTIPLIER_WIN_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_WIN_LVL_3 * 100 : MULTIPLIER_WIN_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_WIN_LVL_4 * 100 : MULTIPLIER_WIN_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_WIN_LVL_5 * 100 : MULTIPLIER_WIN_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_WIN_LVL_0 * 100 : MULTIPLIER_WIN_LVL_0);
            }
        }
        else if (type.equalsIgnoreCase("blood")) {
            switch (level) {
                case 0:
                    return (asPercentage ? MULTIPLIER_BLOOD_LVL_0 * 100 : MULTIPLIER_BLOOD_LVL_0);
                case 1:
                    return (asPercentage ? MULTIPLIER_BLOOD_LVL_1 * 100 : MULTIPLIER_BLOOD_LVL_1);
                case 2:
                    return (asPercentage ? MULTIPLIER_BLOOD_LVL_2 * 100 : MULTIPLIER_BLOOD_LVL_2);
                case 3:
                    return (asPercentage ? MULTIPLIER_BLOOD_LVL_3 * 100 : MULTIPLIER_BLOOD_LVL_3);
                case 4:
                    return (asPercentage ? MULTIPLIER_BLOOD_LVL_4 * 100 : MULTIPLIER_BLOOD_LVL_4);
                case 5:
                    return (asPercentage ? MULTIPLIER_BLOOD_LVL_5 * 100 : MULTIPLIER_BLOOD_LVL_5);
                default:
                    return (asPercentage ? MULTIPLIER_BLOOD_LVL_0 * 100 : MULTIPLIER_BLOOD_LVL_0);
            }
        }

        return -1;
    }

}
