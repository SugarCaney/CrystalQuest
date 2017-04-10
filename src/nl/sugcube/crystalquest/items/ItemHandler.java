package nl.sugcube.crystalquest.items;

import nl.sugcube.crystalquest.Broadcast;
import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.Update;
import nl.sugcube.crystalquest.sba.SItem;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SugarCaney
 */
public class ItemHandler {

    public static CrystalQuest plugin;
    public static List<String> dogNames;
    public static ItemStack carrot;
    public static int dogCount = 0;
    public ConcurrentHashMap<Entity, Integer> cursed;
    private List<ItemStack> items;
    private List<ItemExecutor> executors;

    public ItemHandler(CrystalQuest instance) {
        plugin = instance;
        items = new ArrayList<>();
        cursed = new ConcurrentHashMap<>();
        dogNames = new ArrayList<>();
        executors = new ArrayList<>();
        initializeDogNames();
    }

    public static void initializeDogNames() {
        try {
            InputStream is = plugin.getResource("woof");
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = in.readLine()) != null) {
                dogNames.add(line.trim());
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            dogNames.add("Wolfie");
        }
        finally {
            dogCount = dogNames.size();
        }
    }

    /**
     * Adds all items to the item-list
     */
    public void addAllItems() {
        addCrystalShard();
        addSmallCrystal();
        addShinyCrystal();
        addGrenade();
        addHealthPotion();
        addRailgun();
        addSpeedPotion();
        addBlooper();
        addGoldenApple();
        addFireFlower();
        addStrengthPotion();
        addShield();
        addWither();
        addCreeperEgg();
        addHammer();
        addLandmine();
        addWolf();
        addGlue();
        addLightning();
        addAnvil();
        addTnt();
        addTelepearl();
        addBloodBunny();
        addBomb();
        addCurse();
        addPoisonDart();
        addForceField();

        if (plugin.getConfig().isSet("arena.banned-items")) {
            List<ItemStack> toRemove = new ArrayList<>();
            for (ItemStack item : this.getAllItems()) {
                Material mat = item.getType();
                List<Material> matlist = new ArrayList<>();
                for (String s : plugin.getConfig().getStringList("arena.banned-items")) {
                    matlist.add(SItem.toMaterial(s));
                }
                if (matlist.contains(mat)) {
                    toRemove.add(item);
                }
            }
            for (ItemStack item : toRemove) {
                this.getAllItems().remove(item);
            }
        }

    }

    /**
     * Returns a list containing all items in the game.
     *
     * @return (ItemStackList)
     */
    public List<ItemStack> getAllItems() {
        return this.items;
    }

    /**
     * Gets a random item from the item-list
     *
     * @return (ItemStack) A random item.
     */
    public ItemStack getRandomItem() {
        Random ran = new Random();
        ItemStack is = this.items.get(ran.nextInt(this.items.size()));
        if (is.getType() == Material.EGG) {
            is.setAmount(ran.nextInt(3) + 1);
        }
        else if (is.getType() == Material.IRON_HOE) {
            is.setAmount(ran.nextInt(2) + 1);
        }
        else if (is.getType() == Material.RED_ROSE) {
            is.setAmount(ran.nextInt(3) + 1);
        }
        return is;
    }

    /**
     * Adds the Force Field-item to the items-list
     */
    public void addForceField() {
        ItemStack is = new ItemStack(Material.GLASS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.force-field"));
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new ForceField());
    }

    /**
     * Adds the Poison Dart-item to the items-list
     */
    public void addPoisonDart() {
        ItemStack is = new ItemStack(Material.SUGAR_CANE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.poison-dart"));
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new PoisonDart());
    }

    /**
     * Adds the Curse-item to the items-list
     */
    public void addCurse() {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short)0);
        SkullMeta im = (SkullMeta)is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.curse"));
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new Curse());
    }

    /**
     * Add the Bomb-item
     */
    public void addBomb() {
        ItemStack is = new ItemStack(Material.SPLASH_POTION, 1);
        PotionMeta im = (PotionMeta)is.getItemMeta();
        im.setColor(Color.BLACK);
        im.setDisplayName(Broadcast.get("items.bomb"));
        im.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 300, 10), true);
        is.setItemMeta(im);
        this.items.add(is);
    }

    /**
     * Add the Blood Bunny-item
     */
    public void addBloodBunny() {
        ItemStack is = new ItemStack(Material.CARROT_ITEM, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.bunny"));
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new BloodBunny());
        carrot = is;
    }

    /**
     * Add the TNT-item
     */
    public void addTnt() {
        ItemStack is = new ItemStack(Material.TNT, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.tnt"));
        is.setItemMeta(im);
        this.items.add(is);
    }

    /**
     * Add the Telepearl-item
     */
    public void addTelepearl() {
        ItemStack is = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.telepearl"));
        is.setItemMeta(im);
        is.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
        this.items.add(is);
    }

    /**
     * Add the Anvil-item
     */
    public void addAnvil() {
        ItemStack is = new ItemStack(Material.ANVIL, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.anvil"));
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new Anvil());
    }

    /**
     * Add the Lightning-item
     */
    public void addLightning() {
        ItemStack is = new ItemStack(Material.FEATHER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.lightning-bolt"));
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new LightningBolt());
    }

    /**
     * Add the Glue-item
     */
    public void addGlue() {
        ItemStack is = new ItemStack(Material.SLIME_BALL, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.glue"));
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new Glue());
    }

    /**
     * Add the Landmine-item
     */
    public void addLandmine() {
        ItemStack is = new ItemStack(Material.STONE_PLATE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.landmine"));
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new Landmine());
    }

    /**
     * Add the Wolf-item
     */
    public void addWolf() {
        ItemStack is = new ItemStack(Material.BONE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.wolf") + ChatColor.RED + " <3");
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new WolfHeart());
    }

    /**
     * Add the CreeperEgg-item to the items-list
     */
    public void addCreeperEgg() {
        ItemStack is = new ItemStack(Material.MONSTER_EGG, 1, (short)50);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.DARK_GREEN + "Creeper Egg");
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new CreeperEgg());
    }

    /**
     * Adds the Hammer-item to the items-list
     */
    public void addHammer() {
        ItemStack is = new ItemStack(Material.DIAMOND_AXE, 1, (short)1561);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.hammer"));
        is.setItemMeta(im);
        is.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
        is.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
        this.items.add(is);
    }

    /**
     * Adds the Wither-item to the items-list
     */
    public void addWither() {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short)1);
        SkullMeta im = (SkullMeta)is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.wither"));
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new Wither());
    }

    /**
     * Adds the Shield-item to the items-list
     */
    public void addShield() {
        ItemStack is = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.shield"));
        is.setItemMeta(im);
        this.items.add(is);
    }

    /**
     * Adds the StrengthPotion-item to the items-list
     */
    public void addStrengthPotion() {
        PotionEffect effect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 240, 0);
        ItemStack is = new ItemStack(Material.SPLASH_POTION, 1);
        PotionMeta im = (PotionMeta)is.getItemMeta();
        im.addCustomEffect(effect, true);
        im.setDisplayName(Broadcast.get("items.strength"));
        is.setItemMeta(im);
        this.items.add(is);
    }

    /**
     * Adds the FireFlower-item to the items-list
     */
    public void addFireFlower() {
        ItemStack is = new ItemStack(Material.RED_ROSE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.flower"));
        is.setItemMeta(im);
        this.items.add(is);
        executors.add(new FireFlower());
    }

    /**
     * Adds the GoldenApple-item to the items-list
     */
    public void addGoldenApple() {
        ItemStack is = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.apple"));
        is.setItemMeta(im);
        this.items.add(is);
    }

    /**
     * Adds the Railgun-item to the items-list
     */
    public void addRailgun() {
        ItemStack is = new ItemStack(Material.IRON_HOE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.railgun"));
        is.setItemMeta(im);
        items.add(is);
        executors.add(new Railgun());
    }

    /**
     * Adds the Blooper-item to the items-list
     */
    public void addBlooper() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (short)0);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.blooper"));
        is.setItemMeta(im);
        items.add(is);
        executors.add(new Blooper());
    }

    /**
     * Adds the SpeedPotion-item to the items-list
     */
    public void addSpeedPotion() {
        PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 360, 1);
        ItemStack is = new ItemStack(Material.SPLASH_POTION, 1);
        PotionMeta im = (PotionMeta)is.getItemMeta();
        im.setColor(Color.fromRGB(79, 215, 242));
        im.addCustomEffect(effect, true);
        im.setDisplayName(Broadcast.get("items.speed"));
        is.setItemMeta(im);
        this.items.add(is);
    }

    /**
     * Adds the HealthPotion-item to the items-list
     */
    public void addHealthPotion() {
        PotionEffect effect = new PotionEffect(PotionEffectType.HEAL, 4, 4);
        ItemStack is = new ItemStack(Material.SPLASH_POTION, 1);
        PotionMeta im = (PotionMeta)is.getItemMeta();
        im.addCustomEffect(effect, true);
        im.setDisplayName(Broadcast.get("items.health"));
        is.setItemMeta(im);
        this.items.add(is);
    }

    /**
     * Adds the Grenade-item to the items-list
     */
    public void addGrenade() {
        ItemStack is = new ItemStack(Material.EGG, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.grenade"));
        is.setItemMeta(im);
        items.add(is);
    }

    /**
     * Adds the ShinyCrystal-item to the items-list
     */
    public void addShinyCrystal() {
        ItemStack is = new ItemStack(Material.DIAMOND, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.shiny-crystal"));
        is.setItemMeta(im);
        is.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        items.add(is);
    }

    /**
     * Adds the SmallCrystal-item to the items-list
     */
    public void addSmallCrystal() {
        ItemStack is = new ItemStack(Material.DIAMOND, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.small-crystal"));
        is.setItemMeta(im);
        items.add(is);
    }

    /**
     * Adds the CrystalShard-item to the items-list
     */
    public void addCrystalShard() {
        ItemStack is = new ItemStack(Material.EMERALD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Broadcast.get("items.crystal-shard"));
        is.setItemMeta(im);
        items.add(is);
    }

    /**
     * Gets a specific item using the name
     *
     * @param name
     *         (String) The name of the item
     * @return (ItemStack) The itemstack the item represents
     */
    public ItemStack getItemByName(String name) {
        for (ItemStack item : this.items) {
            if (item.getItemMeta().getDisplayName().contains(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gets the book containing all instructions. Specially
     * made for plugin developers.
     *
     * @param specialMessage2lines
     *         (String) A special message with a length of 2 lines at the end of the book.
     * @return (ItemStack) The instruction manual.
     */
    public ItemStack getInstructionManual(String specialMessage2lines) {
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta meta = (BookMeta)is.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Instruction Manual");
        meta.setAuthor(ChatColor.GREEN + "MrSugarCaney");

        String[] pages = new String[17];

        //Page 1:
        pages[0] = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "CrystalQuest\n" +
                ChatColor.RESET + ChatColor.BLACK + ChatColor.ITALIC + "It's all about bling!\n" +
                "\n" + ChatColor.RESET +
                "CrystalQuest is a minigame where the goal is to collect as many crystals" +
                " as you possibly can. You can do this by smashing Ender Crystals or by " +
                "killing other people and stealing their crystals.";
        pages[1] = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Classes\n" +
                ChatColor.RESET + ChatColor.BLACK + "\n" +
                "The first thing you should do when you are in the lobby, waiting to start" +
                " is to choose a class. Once you have chosen a class, you will automaticly " +
                "get the class's items everytime you spawn.";
        pages[2] = ChatColor.BLUE + "" + ChatColor.BOLD + "The Game\n" +
                ChatColor.RESET + ChatColor.BLACK + "\n" +
                "You have " + (plugin.getConfig().getInt("arena.game-length") / 60) + " minutes to" +
                " collect as many crystals as possible. Ender Crystals will give you 3 points and" +
                " killing people will let them drop 1-6 of their points (crystals). The team with" +
                " the most amount of crystals in the end, wins!";
        pages[3] = ChatColor.GOLD + "" + ChatColor.BOLD + "Shop\n" +
                ChatColor.RESET + ChatColor.BLACK + "\n" +
                "When you kill someone and by winning you get crystals (now as a currency) to buy new" +
                " upgrades in the shop. You can upgrade your power-ups, buy new classes or increase" +
                " the amount of crystals you'll get!\nCrystalQuestCommand: /cq shop";
        pages[4] = ChatColor.GREEN + "" + ChatColor.BOLD + "Power-Ups\n" +
                ChatColor.RESET + ChatColor.BLACK + "\n" +
                "To make the game more crazy, there are different kinds of items that will help you" +
                " on your Quest. All items are listed down here:\n" +
                "\n" + ChatColor.AQUA + "Crystals" + ChatColor.RESET + "\n" +
                ChatColor.BLACK + "These will give you 1, 2 or 3 points.";
        pages[5] = ChatColor.GOLD + "Grenades\n" + ChatColor.BLACK + "Explode your target!\n\n" +
                ChatColor.RED + "Health Potion\n" + ChatColor.BLACK + "Like the vanilla one.\n\n" +
                ChatColor.GRAY + "Railgun\n" + ChatColor.BLACK + "A heavy gun which instant-kills" +
                " anyone hit by it's projectiles.\n\n" + ChatColor.AQUA + "Speed Potion\n" +
                ChatColor.BLACK + "Speed II - 18s";
        pages[6] = ChatColor.BLACK + "Blooper\n" + ChatColor.BLACK + "Will blind a random team. - 6s\n\n" +
                ChatColor.GOLD + "Golden Apple\n" + ChatColor.BLACK + "Like the vanilla one.\n\n" +
                ChatColor.DARK_RED + "FireFlower\n" + ChatColor.BLACK + "Shoots an exploding fireball.\n\n" +
                ChatColor.AQUA + "Sterngth Potion\n" + ChatColor.BLACK + "Strength II - 10s";
        pages[7] = ChatColor.GRAY + "Shield\n" + ChatColor.BLACK + "Resistance I - 30s\n\n" +
                ChatColor.DARK_GRAY + "Wither\n" + ChatColor.BLACK + "Withers (II) a random team for 6s.\n\n" +
                ChatColor.DARK_GREEN + "Creeper Egg\n" + ChatColor.BLACK + "Spawns a creeper at the clicked" +
                " location with a chance of being supercharged.";
        pages[8] = ChatColor.DARK_PURPLE + "Hammer\n" + ChatColor.BLACK + "Sharpness III, Knockback X axe.\n\n" +
                ChatColor.GRAY + "Landmine\n" + ChatColor.BLACK + "Puts down an instant-death plate.\n\n" +
                ChatColor.RED + "Wolfie <3\n" + ChatColor.BLACK + "Spawns you a wolf with Strength I.";
        pages[9] = ChatColor.GREEN + "Glue\n" + ChatColor.BLACK + "Slowness 15 for 6s on a random team.\n\n" +
                ChatColor.GOLD + "Lightning Bolt\n" + ChatColor.BLACK + "Random team gets lightning, nausea and fire.\n\n" +
                ChatColor.DARK_GRAY + "Anvil\n" + ChatColor.BLACK + "Let the iron rain!\n\n" +
                ChatColor.DARK_RED + "TNT\n" + ChatColor.BLACK + "Auto-2s-fuse.";
        pages[10] = ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Abilities\n" +
                ChatColor.RESET + ChatColor.BLACK + "\n" +
                "Some classes have special abilities/weaknesses. Deatails about these are listed down" +
                "here:\n" +
                "\n" + ChatColor.UNDERLINE + "Doublejump Boost" + ChatColor.RESET + "\n" +
                ChatColor.BLACK + "Will increase the power of your doublejumps.";
        pages[11] = ChatColor.UNDERLINE + "Agility\n" + ChatColor.BLACK + "Permanent Speed I\n\n" +
                ChatColor.UNDERLINE + "Super Speed\n" + ChatColor.BLACK + "Permanent Speed II\n\n" +
                ChatColor.UNDERLINE + "Strength\n" + ChatColor.BLACK + "Permanent Strength I\n\n" +
                ChatColor.UNDERLINE + "Poison\n" + ChatColor.BLACK + "Poison your target when you hit him/her.";
        pages[12] = ChatColor.UNDERLINE + "Explosive Arrows\n" + ChatColor.BLACK + "Fired arrows will give a small explosion when they hit.\n\n" +
                ChatColor.UNDERLINE + "Resistance\n" + ChatColor.BLACK + "Permanent Resist. I\n\n" +
                ChatColor.UNDERLINE + "Jump Boost\n" + ChatColor.BLACK + "Permanent J.Boost II\n\n" +
                ChatColor.UNDERLINE + "Health Boost\n" + ChatColor.BLACK + "2 more hearts!";
        pages[13] = ChatColor.UNDERLINE + "Last Revenge\n" + ChatColor.BLACK + "Strikes lightning at a random team when you die.\n\n" +
                ChatColor.UNDERLINE + "Less Death Crystals\n" + ChatColor.BLACK + "You will drop less crystals when you die.\n\n" +
                ChatColor.UNDERLINE + "Return Arrow\n" + ChatColor.BLACK + "If you hit your target, you will get your arrow back.\n\n";
        pages[14] = ChatColor.UNDERLINE + "Drain\n" + ChatColor.BLACK + "You regain 0.5 heart when you hit someone.\n\n" +
                ChatColor.UNDERLINE + "Slowness\n" + ChatColor.BLACK + "Permanent Slowness I\n\n" +
                ChatColor.UNDERLINE + "Weakness\n" + ChatColor.BLACK + "Permanent Weakness I\n\n" +
                ChatColor.UNDERLINE + "Water Healing\n" + ChatColor.BLACK + "You get regeneration whilst in the water.";
        pages[15] = ChatColor.UNDERLINE + "Corroding\n" + ChatColor.BLACK + "Being in the water corrodes you (Wither-effect).\n\n" +
                ChatColor.UNDERLINE + "Magical Aura\n" + ChatColor.BLACK + "Wands regenerate twice as fast.\n\n" +
                ChatColor.UNDERLINE + "Power Loss\n" + ChatColor.BLACK + "Wands regenerate twice as slow.\n\n";

        String version = plugin.getDescription().getVersion();
        String uptodate = "";
        if (plugin.getConfig().getBoolean("updates.check-for-updates")) {
            Update uc = new Update(69421, version);
            if (uc.query()) {
                uptodate = "New version available!";
            }
            else {
                uptodate = "Up-to-date";
            }
        }
        else {
            uptodate = "Checking disabled!";
        }
        pages[16] = "Good luck and thanks for playing!\n\n" +
                "Plugin Author:\n-" + ChatColor.GOLD + ChatColor.BOLD + "MrSugarCaney\n\n" + ChatColor.RESET + ChatColor.BLACK +
                "Current Version:\n" + version + "\n" + uptodate + "\n\n" + specialMessage2lines;

        meta.addPage(pages);

        is.setItemMeta(meta);
        return is;
    }

    /**
     * Get an unmodifiable list of all ItemExecutors.
     */
    public List<ItemExecutor> getExecutors() {
        return Collections.unmodifiableList(executors);
    }
}