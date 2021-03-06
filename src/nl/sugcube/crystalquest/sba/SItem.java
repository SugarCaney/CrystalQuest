package nl.sugcube.crystalquest.sba;

import org.bukkit.Material;

import java.util.Arrays;

/**
 * @author SugarCaney
 */
public final class SItem {

    private static final Material[] MATERIALS = Material.values();

    /**
     * Get the material that corresponds to the given block ID.
     *
     * @param blockId
     *         The material ID of the material to get.
     * @return The material that has the given {@code blockId}, or {@link Material#AIR} when the
     * blockID doesn't exist.
     */
    public static Material toMaterial(int blockId) {
        return Arrays.stream(MATERIALS)
                .filter(material -> material.getId() == blockId)
                .findFirst()
                .orElse(Material.AIR);
    }

    /**
     * Get the material that has the given name.
     * <p>
     * When the name could not be found, {@link SItem#legacyToMaterial(String)} will be used to
     * preserve backwards compatibility.
     *
     * @param trivialName
     *         The name of the material. Is the enum-name in all lower case with underscores. Will
     *         also work without underscores. You could also emit underscores (so stationarylava
     *         will work).
     * @return The material that corresponds to the given name.
     */
    public static Material toMaterial(String trivialName) {
        String name = trivialName.replace("_", "");
        return Arrays.stream(MATERIALS)
                .filter(material -> material.name().replace("_", "")
                        .equalsIgnoreCase(name))
                .findFirst()
                .orElse(legacyToMaterial(trivialName));
    }

    /**
     * @deprecated Use {@link SItem#toMaterial(String)} instead.
     */
    @Deprecated
    public static Material legacyToMaterial(String trivialName) {
        switch (trivialName.toLowerCase()) {
            case "stone":
                return Material.STONE;
            case "grass":
                return Material.GRASS;
            case "dirt":
                return Material.DIRT;
            case "cobblestone":
                return Material.COBBLESTONE;
            case "woodenplanks":
                return Material.OAK_PLANKS;
            case "sapling":
                return Material.OAK_SAPLING;
            case "bedrock":
                return Material.BEDROCK;
            case "water":
            case "stationarywater":
                return Material.WATER;
            case "lava":
            case "stationarylava":
                return Material.LAVA;
            case "sand":
                return Material.SAND;
            case "gravel":
                return Material.GRAVEL;
            case "goldore":
                return Material.GOLD_ORE;
            case "ironore":
                return Material.IRON_ORE;
            case "coalore":
                return Material.COAL_ORE;
            case "log":
                return Material.OAK_LOG;
            case "leaves":
                return Material.OAK_LEAVES;
            case "sponge":
                return Material.SPONGE;
            case "glass":
                return Material.GLASS;
            case "lapisore":
                return Material.LAPIS_ORE;
            case "lapisblock":
                return Material.LAPIS_BLOCK;
            case "dispenser":
                return Material.DISPENSER;
            case "sandstone":
                return Material.SANDSTONE;
            case "noteblock":
                return Material.NOTE_BLOCK;
            case "bedblock":
            case "bed":
                return Material.RED_BED;
            case "poweredrail":
                return Material.POWERED_RAIL;
            case "detectorrail":
                return Material.DETECTOR_RAIL;
            case "stickypiston":
                return Material.STICKY_PISTON;
            case "cobweb":
                return Material.COBWEB;
            case "tallgrass":
                return Material.TALL_GRASS;
            case "deadbush":
                return Material.DEAD_BUSH;
            case "piston":
                return Material.PISTON;
            case "pistonextension":
            case "pistonmovingpiece":
                return Material.PISTON_HEAD;
            case "wool":
                return Material.WHITE_WOOL;
            case "dandelion":
                return Material.DANDELION;
            case "rose":
                return Material.POPPY;
            case "browmushroom":
                return Material.BROWN_MUSHROOM;
            case "redmushroom":
                return Material.RED_MUSHROOM;
            case "goldblock":
                return Material.GOLD_BLOCK;
            case "ironblock":
                return Material.IRON_BLOCK;
            case "doubleslab":
            case "slab":
            case "woodenslab":
            case "woodendoubleslab":
                return Material.OAK_SLAB;
            case "brickblock":
            case "brick":
                return Material.BRICK;
            case "tnt":
                return Material.TNT;
            case "bookshelf":
                return Material.BOOKSHELF;
            case "mossycobblestone":
                return Material.MOSSY_COBBLESTONE;
            case "obsidian":
                return Material.OBSIDIAN;
            case "torch":
                return Material.TORCH;
            case "fire":
                return Material.FIRE;
            case "mobspawner":
                return Material.SPAWNER;
            case "woodenstairs":
                return Material.OAK_STAIRS;
            case "chest":
                return Material.CHEST;
            case "redstonewire":
                return Material.REDSTONE_WIRE;
            case "diamondore":
                return Material.DIAMOND_ORE;
            case "diamondblock":
                return Material.DIAMOND_BLOCK;
            case "craftingtable":
                return Material.CRAFTING_TABLE;
            case "crops":
            case "wheat":
                return Material.WHEAT;
            case "farmland":
                return Material.FARMLAND;
            case "furnace":
            case "burningfurnace":
                return Material.FURNACE;
            case "signpost":
            case "sign":
                return Material.SIGN;
            case "woodendoorblock":
            case "woodendoor":
                return Material.OAK_DOOR;
            case "ladder":
                return Material.LADDER;
            case "rails":
                return Material.RAIL;
            case "cobblestonestairs":
                return Material.COBBLESTONE_STAIRS;
            case "wallsign":
                return Material.WALL_SIGN;
            case "lever":
                return Material.LEVER;
            case "stonepressureplate":
                return Material.STONE_PRESSURE_PLATE;
            case "irondoorblock":
            case "irondoor":
                return Material.IRON_DOOR;
            case "woodenpressureplate":
                return Material.OAK_PRESSURE_PLATE;
            case "redstoneore":
            case "glowingredstoneore":
                return Material.REDSTONE_ORE;
            case "redstonetorchoff":
            case "redstonetorch":
                return Material.REDSTONE_TORCH;
            case "stonebutton":
                return Material.STONE_BUTTON;
            case "snow":
                return Material.SNOW;
            case "ice":
                return Material.ICE;
            case "snowblock":
                return Material.SNOW_BLOCK;
            case "cactus":
                return Material.CACTUS;
            case "clayblock":
                return Material.CLAY;
            case "sugarcaneblock":
            case "sugarcane":
                return Material.SUGAR_CANE;
            case "jukebox":
                return Material.JUKEBOX;
            case "fence":
                return Material.OAK_FENCE;
            case "pumpkin":
                return Material.PUMPKIN;
            case "netherrack":
                return Material.NETHERRACK;
            case "soulsand":
                return Material.SOUL_SAND;
            case "glowstone":
                return Material.GLOWSTONE;
            case "netherportal":
                return Material.NETHER_PORTAL;
            case "jackolantern":
                return Material.JACK_O_LANTERN;
            case "cakeblock":
            case "cake":
                return Material.CAKE;
            case "repeateroff":
            case "repeateron":
            case "repeater":
                return Material.REPEATER;
            case "trapdoor":
                return Material.OAK_TRAPDOOR;
            case "monsteregg":
                return Material.INFESTED_STONE;
            case "smoothstonebrick":
                return Material.STONE_BRICKS;
            case "mushroom":
                return Material.RED_MUSHROOM_BLOCK;
            case "mushroom2":
                return Material.BROWN_MUSHROOM_BLOCK;
            case "ironbars":
                return Material.IRON_BARS;
            case "thinglass":
                return Material.GLASS_PANE;
            case "melonblock":
            case "melon":
                return Material.MELON;
            case "melonstem":
                return Material.MELON_STEM;
            case "pumpkinstem":
                return Material.PUMPKIN_STEM;
            case "vine":
                return Material.VINE;
            case "gate":
                return Material.OAK_FENCE_GATE;
            case "brickstairs":
                return Material.BRICK_STAIRS;
            case "smoothbrickstairs":
                return Material.STONE_BRICK_STAIRS;
            case "mycelium":
                return Material.MYCELIUM;
            case "lilypad":
                return Material.LILY_PAD;
            case "netherbrickblock":
                return Material.NETHER_BRICK;
            case "netherfence":
                return Material.NETHER_BRICK_FENCE;
            case "netherbrickstairs":
                return Material.NETHER_BRICK_STAIRS;
            case "netherwartblock":
                return Material.NETHER_WART_BLOCK;
            case "enchantmenttable":
                return Material.ENCHANTING_TABLE;
            case "brewingstandblock":
            case "brewingstand":
                return Material.BREWING_STAND;
            case "cauldronblock":
            case "cauldron":
                return Material.CAULDRON;
            case "endportal":
                return Material.END_PORTAL;
            case "endportalframe":
                return Material.END_PORTAL_FRAME;
            case "endstone":
                return Material.END_STONE;
            case "dragonegg":
                return Material.DRAGON_EGG;
            case "redstonelamp":
            case "redstonelampon":
                return Material.REDSTONE_LAMP;
            case "cocoablock":
                return Material.COCOA;
            case "sandstonestairs":
                return Material.SANDSTONE_STAIRS;
            case "emeraldore":
                return Material.EMERALD_ORE;
            case "enderchest":
                return Material.ENDER_CHEST;
            case "tripwirehook":
                return Material.TRIPWIRE_HOOK;
            case "tripwire":
                return Material.TRIPWIRE;
            case "emeraldblock":
                return Material.EMERALD_BLOCK;
            case "sprucewoodstairs":
                return Material.SPRUCE_STAIRS;
            case "birchwoodstairs":
                return Material.BIRCH_STAIRS;
            case "junglewoodstairs":
                return Material.JUNGLE_STAIRS;
            case "commandblock":
                return Material.COMMAND_BLOCK;
            case "beacon":
                return Material.BEACON;
            case "cobblestonewall":
                return Material.COBBLESTONE_WALL;
            case "flowerpotblock":
            case "flowerpot":
                return Material.FLOWER_POT;
            case "carrotcrops":
                return Material.CARROT;
            case "potatocrops":
                return Material.POTATO;
            case "woodenbutton":
                return Material.OAK_BUTTON;
            case "headblock":
            case "head":
                return Material.PLAYER_HEAD;
            case "anvil":
                return Material.ANVIL;
            case "trappedchest":
                return Material.TRAPPED_CHEST;
            case "goldenpressureplate":
                return Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
            case "ironpressureplate":
                return Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
            case "comparatoroff":
            case "comparatoron":
            case "comparator":
                return Material.COMPARATOR;
            case "daylightdetector":
                return Material.DAYLIGHT_DETECTOR;
            case "redstoneblock":
                return Material.REDSTONE_BLOCK;
            case "quartzore":
                return Material.NETHER_QUARTZ_ORE;
            case "hopper":
                return Material.HOPPER;
            case "quartzblock":
                return Material.QUARTZ_BLOCK;
            case "quartzstairs":
                return Material.QUARTZ_STAIRS;
            case "activatorrail":
                return Material.ACTIVATOR_RAIL;
            case "dropper":
                return Material.DROPPER;
            case "stainedclay":
                return Material.WHITE_TERRACOTTA;
            case "hay":
                return Material.HAY_BLOCK;
            case "carpet":
                return Material.WHITE_CARPET;
            case "hardenedclay":
                return Material.TERRACOTTA;
            case "coalblock":
                return Material.COAL_BLOCK;
            case "ironshovel":
                return Material.IRON_SHOVEL;
            case "ironpickaxe":
                return Material.IRON_PICKAXE;
            case "ironaxe":
                return Material.IRON_AXE;
            case "flintandsteel":
                return Material.FLINT_AND_STEEL;
            case "apple":
                return Material.APPLE;
            case "bow":
                return Material.BOW;
            case "arrow":
                return Material.ARROW;
            case "coal":
                return Material.COAL;
            case "diamond":
                return Material.DIAMOND;
            case "ironingot":
                return Material.IRON_INGOT;
            case "goldingot":
                return Material.GOLD_INGOT;
            case "ironsword":
                return Material.IRON_SWORD;
            case "woodsword":
                return Material.WOODEN_SWORD;
            case "woodspade":
                return Material.WOODEN_SHOVEL;
            case "woodpickaxe":
                return Material.WOODEN_PICKAXE;
            case "woodaxe":
                return Material.WOODEN_AXE;
            case "stonesword":
                return Material.STONE_SWORD;
            case "stoneshovel":
                return Material.STONE_SHOVEL;
            case "stonepickaxe":
                return Material.STONE_PICKAXE;
            case "stoneaxe":
                return Material.STONE_AXE;
            case "diamondsword":
                return Material.DIAMOND_SWORD;
            case "diamondshovel":
                return Material.DIAMOND_SHOVEL;
            case "diamondpickaxe":
                return Material.DIAMOND_PICKAXE;
            case "diamondaxe":
                return Material.DIAMOND_AXE;
            case "stick":
                return Material.STICK;
            case "bowl":
                return Material.BOWL;
            case "mushroomsoup":
                return Material.MUSHROOM_STEW;
            case "goldensword":
                return Material.GOLDEN_SWORD;
            case "goldenshovel":
                return Material.GOLDEN_SHOVEL;
            case "goldenpickaxe":
                return Material.GOLDEN_PICKAXE;
            case "goldaxe":
                return Material.GOLDEN_AXE;
            case "string":
                return Material.STRING;
            case "feather":
                return Material.FEATHER;
            case "gunpowder":
                return Material.GUNPOWDER;
            case "woodenhoe":
                return Material.WOODEN_HOE;
            case "stonehoe":
                return Material.STONE_HOE;
            case "ironhoe":
                return Material.IRON_HOE;
            case "diamondhoe":
                return Material.DIAMOND_HOE;
            case "goldenhoe":
                return Material.GOLDEN_HOE;
            case "seeds":
                return Material.WHEAT_SEEDS;
            case "bread":
                return Material.BREAD;
            case "leatherhelmet":
                return Material.LEATHER_HELMET;
            case "leatherchestplate":
                return Material.LEATHER_CHESTPLATE;
            case "leatherleggings":
                return Material.LEATHER_LEGGINGS;
            case "leatherboots":
                return Material.LEATHER_BOOTS;
            case "chainmailhelmet":
                return Material.CHAINMAIL_HELMET;
            case "chainmailchestplate":
                return Material.CHAINMAIL_CHESTPLATE;
            case "chainmailleggings":
                return Material.CHAINMAIL_LEGGINGS;
            case "chainmailboots":
                return Material.CHAINMAIL_BOOTS;
            case "ironhelmet":
                return Material.IRON_HELMET;
            case "ironchestplate":
                return Material.IRON_CHESTPLATE;
            case "ironleggings":
                return Material.IRON_LEGGINGS;
            case "ironboots":
                return Material.IRON_BOOTS;
            case "diamondhelmet":
                return Material.DIAMOND_HELMET;
            case "diamondchestplate":
                return Material.DIAMOND_CHESTPLATE;
            case "diamondleggings":
                return Material.DIAMOND_LEGGINGS;
            case "diamondboots":
                return Material.DIAMOND_BOOTS;
            case "goldenhelmet":
            case "goldenleggings":
                return Material.GOLDEN_HELMET;
            case "goldenchestplate":
                return Material.GOLDEN_CHESTPLATE;
            case "goldenboots":
                return Material.GOLDEN_BOOTS;
            case "flint":
                return Material.FLINT;
            case "rawpork":
                return Material.PORKCHOP;
            case "cookedpork":
                return Material.COOKED_PORKCHOP;
            case "painting":
                return Material.PAINTING;
            case "goldenapple":
                return Material.GOLDEN_APPLE;
            case "bucket":
                return Material.BUCKET;
            case "waterbucket":
                return Material.WATER_BUCKET;
            case "lavabucket":
                return Material.LAVA_BUCKET;
            case "minecart":
                return Material.MINECART;
            case "saddle":
                return Material.SADDLE;
            case "redstone":
                return Material.REDSTONE;
            case "snowball":
                return Material.SNOWBALL;
            case "boat":
                return Material.OAK_BOAT;
            case "leather":
                return Material.LEATHER;
            case "milkbucket":
                return Material.MILK_BUCKET;
            case "clay":
                return Material.CLAY_BALL;
            case "paper":
                return Material.PAPER;
            case "book":
                return Material.BOOK;
            case "slimeball":
                return Material.SLIME_BALL;
            case "storageminecart":
                return Material.CHEST_MINECART;
            case "poweredminecart":
                return Material.FURNACE_MINECART;
            case "egg":
                return Material.EGG;
            case "compass":
                return Material.COMPASS;
            case "fishingrod":
                return Material.FISHING_ROD;
            case "watch":
                return Material.CLOCK;
            case "glowstonedust":
                return Material.GLOWSTONE_DUST;
            case "rawfish":
                return Material.COD;
            case "cookedfish":
                return Material.COOKED_COD;
            case "dye":
                return Material.INK_SAC;
            case "bone":
                return Material.BONE;
            case "sugar":
                return Material.SUGAR;
            case "cookie":
                return Material.COOKIE;
            case "map":
            case "emptymap":
                return Material.MAP;
            case "shears":
                return Material.SHEARS;
            case "pumpkinseeds":
                return Material.PUMPKIN_SEEDS;
            case "melonseeds":
                return Material.MELON_SEEDS;
            case "rawbeef":
                return Material.BEEF;
            case "cookedbeef":
                return Material.COOKED_BEEF;
            case "rawchicken":
                return Material.CHICKEN;
            case "cookedchicken":
                return Material.COOKED_CHICKEN;
            case "rottenflesh":
                return Material.ROTTEN_FLESH;
            case "enderpearl":
                return Material.ENDER_PEARL;
            case "blazerod":
                return Material.BLAZE_ROD;
            case "ghasttear":
                return Material.GHAST_TEAR;
            case "goldennugget":
                return Material.GOLD_NUGGET;
            case "netherwart":
                return Material.NETHER_WART;
            case "potion":
                return Material.POTION;
            case "glassbottle":
                return Material.GLASS_BOTTLE;
            case "spidereye":
                return Material.SPIDER_EYE;
            case "fermentedspidereye":
                return Material.FERMENTED_SPIDER_EYE;
            case "blazepowder":
                return Material.BLAZE_POWDER;
            case "magmacream":
                return Material.MAGMA_CREAM;
            case "eyeofender":
                return Material.ENDER_EYE;
            case "speckledmelon":
                return Material.GLISTERING_MELON_SLICE;
            case "spawnegg":
                return Material.CREEPER_SPAWN_EGG;
            case "bottleoenchanting":
                return Material.EXPERIENCE_BOTTLE;
            case "fireball":
                return Material.FIRE_CHARGE;
            case "bookandquill":
                return Material.WRITABLE_BOOK;
            case "writtenbook":
                return Material.WRITTEN_BOOK;
            case "emerald":
                return Material.EMERALD;
            case "itemframe":
                return Material.ITEM_FRAME;
            case "carrot":
                return Material.CARROTS;
            case "potato":
                return Material.POTATOES;
            case "bakedpotato":
                return Material.BAKED_POTATO;
            case "poisonouspotato":
                return Material.POISONOUS_POTATO;
            case "goldencarrot":
                return Material.GOLDEN_CARROT;
            case "carrotstick":
                return Material.CARROT_ON_A_STICK;
            case "netherstar":
                return Material.NETHER_STAR;
            case "pumpkinpie":
                return Material.PUMPKIN_PIE;
            case "firework":
                return Material.FIREWORK_ROCKET;
            case "fireworkstar":
                return Material.FIREWORK_STAR;
            case "enchantedbook":
                return Material.ENCHANTED_BOOK;
            case "netherbrick":
                return Material.NETHER_BRICKS;
            case "netherquartz":
                return Material.QUARTZ;
            case "tntcart":
                return Material.TNT_MINECART;
            case "hopperminecart":
            case "ironhorsearmour":
                return Material.HOPPER_MINECART;
            case "goldenhorsearmour":
                return Material.GOLDEN_HORSE_ARMOR;
            case "diamondhorsearmour":
                return Material.DIAMOND_HORSE_ARMOR;
            case "lead":
                return Material.LEAD;
            case "nametag":
                return Material.NAME_TAG;
            case "record13":
                return Material.MUSIC_DISC_13;
            case "recordcat":
                return Material.MUSIC_DISC_CAT;
            case "recordblocks":
                return Material.MUSIC_DISC_BLOCKS;
            case "recordchirp":
                return Material.MUSIC_DISC_CHIRP;
            case "recordfar":
                return Material.MUSIC_DISC_FAR;
            case "recordmall":
                return Material.MUSIC_DISC_MALL;
            case "recordmellohi":
                return Material.MUSIC_DISC_MELLOHI;
            case "recordstal":
                return Material.MUSIC_DISC_STAL;
            case "recordstrad":
                return Material.MUSIC_DISC_STRAD;
            case "recordward":
                return Material.MUSIC_DISC_WARD;
            case "record11":
                return Material.MUSIC_DISC_11;
            case "recordwait":
                return Material.MUSIC_DISC_WAIT;
            case "stainedglass":
                return Material.WHITE_STAINED_GLASS;
            case "stainedglasspane":
                return Material.WHITE_STAINED_GLASS_PANE;
            case "leaves2":
                return Material.BIRCH_LEAVES;
            case "log2":
                return Material.BIRCH_LOG;
            case "packedice":
                return Material.PACKED_ICE;
            case "doubleplant":
                return Material.LEGACY_DOUBLE_PLANT;
            case "commandminecart":
                return Material.COMMAND_BLOCK_MINECART;
            default:
                return Material.AIR;
        }
    }

    /**
     * @deprecated Use {@link Material#getId()} instead. Even though it's deprecated as well...
     */
    @Deprecated
    public static int toId(Material material) {
        return material.getId();
    }

    private SItem() {
        throw new AssertionError("Noop");
    }
}