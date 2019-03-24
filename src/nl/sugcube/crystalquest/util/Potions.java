package nl.sugcube.crystalquest.util;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

/**
 * @author SugarCaney
 */
public class Potions {

    /**
     * Converts the in-game potion id to the {@link org.bukkit.potion.PotionEffectType} constant.
     * Returns null when it could not be found.
     */
    public static PotionEffectType parseType(String potionEffectType) {
        switch (potionEffectType.toLowerCase()) {
            case "speed":
                return PotionEffectType.SPEED;
            case "slowness":
                return PotionEffectType.SLOW;
            case "haste":
                return PotionEffectType.FAST_DIGGING;
            case "mining_fatigue":
                return PotionEffectType.SLOW_DIGGING;
            case "strength":
                return PotionEffectType.INCREASE_DAMAGE;
            case "instant_health":
                return PotionEffectType.HEAL;
            case "instant_damage":
                return PotionEffectType.HARM;
            case "jump_boost":
                return PotionEffectType.JUMP;
            case "nausea":
                return PotionEffectType.CONFUSION;
            case "regeneration":
                return PotionEffectType.REGENERATION;
            case "resistance":
                return PotionEffectType.DAMAGE_RESISTANCE;
            case "fire_resistance":
                return PotionEffectType.FIRE_RESISTANCE;
            case "water_breathing":
                return PotionEffectType.WATER_BREATHING;
            case "invisibility":
                return PotionEffectType.INVISIBILITY;
            case "blindness":
                return PotionEffectType.BLINDNESS;
            case "night_vision":
                return PotionEffectType.NIGHT_VISION;
            case "hunger":
                return PotionEffectType.HUNGER;
            case "weakness":
                return PotionEffectType.WEAKNESS;
            case "poison":
                return PotionEffectType.POISON;
            case "wither":
                return PotionEffectType.WITHER;
            case "health_boost":
                return PotionEffectType.HEALTH_BOOST;
            case "absorption":
                return PotionEffectType.ABSORPTION;
            case "saturation":
                return PotionEffectType.SATURATION;
            case "glowing":
                return PotionEffectType.GLOWING;
            case "levitation":
                return PotionEffectType.LEVITATION;
            case "luck":
                return PotionEffectType.LUCK;
            case "unluck":
                return PotionEffectType.UNLUCK;
            case "slow_falling":
                return PotionEffectType.SLOW_FALLING;
            case "conduit_power":
                return PotionEffectType.CONDUIT_POWER;
            case "dolphins_grace":
                return PotionEffectType.DOLPHINS_GRACE;
            default:
                return null;
        }
    }

    /**
     * Get the {@link PotionType} of the potion that has the given effect by default.
     */
    public static PotionType getPotionType(String potionEffectType) {
        return PotionType.valueOf(potionEffectType.toUpperCase());
    }

    private Potions() {
    }
}
