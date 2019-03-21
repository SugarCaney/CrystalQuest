package nl.sugcube.crystalquest.game;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.sba.SItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SugarCaney
 */
@SuppressWarnings("deprecation")
public class InventoryManager {

    public static CrystalQuest plugin;

    public final Float EXP_DEFAULT = 0F;
    public final Integer LEVEL_DEFAULT = 0;
    public final Double HEALTH_DEFAULT = 20D;
    public final Integer FOOD_DEFAULT = 20;
    public final Float SATURATION_DEFAULT = 12.8F;
    public final GameMode GAMEMODE_DEFAULT = GameMode.SURVIVAL;

    public ConcurrentHashMap<UUID, ItemStack[]> invStorage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, ItemStack[]> armourStorage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, Integer> levelStorage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, Float> expStorage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, Double> healthStorage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, Integer> foodStorage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, Float> saturationStorage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, GameMode> gamemodeStorage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, Collection<PotionEffect>> potionStorage =
            new ConcurrentHashMap<>();

    public ConcurrentHashMap<UUID, String> playerClass = new ConcurrentHashMap<>();

    public InventoryManager(CrystalQuest instance) {
        plugin = instance;
    }

    /**
     * Removes a player from the playerClass HashMap.
     *
     * @param p
     *         (Player) The player to remove.
     */
    public void removePlayerClass(Player p) {
        this.playerClass.remove(p.getUniqueId());
    }

    /**
     * Adds a player to the playerClass HashMap.
     *
     * @param p
     *         (Player) The player to add.
     * @param playerClass
     *         (String) The technical name of the class (defined in config.yml)
     */
    public void setPlayerClass(Player p, String playerClass) {
        this.playerClass.put(p.getUniqueId(), playerClass);
    }

    /**
     * Saves the inventory contents.
     *
     * @param player
     *         (Player) The player from whom you want to save the inventory/
     */
    public void saveInventory(Player player) {
        invStorage.put(player.getUniqueId(), player.getInventory().getContents());
        armourStorage.put(player.getUniqueId(), player.getInventory().getArmorContents());
        levelStorage.put(player.getUniqueId(), player.getLevel());
        expStorage.put(player.getUniqueId(), player.getExp());
        healthStorage.put(player.getUniqueId(), player.getHealth());
        foodStorage.put(player.getUniqueId(), player.getFoodLevel());
        saturationStorage.put(player.getUniqueId(), player.getSaturation());
        gamemodeStorage.put(player.getUniqueId(), player.getGameMode());
        potionStorage.put(player.getUniqueId(), player.getActivePotionEffects());
    }

    /**
     * Gives the player the kit of his/her class
     *
     * @param player
     *         (Player) The player to give the items to.
     */
    public void setClassInventory(Player player) {
        player.getInventory().clear();
        player.updateInventory();

        int count = 0;
        if (!playerClass.containsKey(player.getUniqueId())) {
            boolean isNotOk = true;
            while (isNotOk && count < 10000) {
                Random ran = new Random();
                Set<String> set = plugin.getConfig().getConfigurationSection("kit").getKeys(false);
                List<String> list = new ArrayList<String>();
                list.addAll(set);

                int random = ran.nextInt(list.size());
                String ranClass = list.get(random);
                if (Classes.hasPermission(player, ranClass)) {
                    playerClass.put(player.getUniqueId(), ranClass);
                    isNotOk = false;
                }
                count++;
            }
        }

        for (String s : plugin.getConfig().getStringList("kit." + playerClass.get(player.getUniqueId()) + ".items")) {

            //Checking if there is a piece of team-armour
            if (s.contains("team_helmet")) {
                ItemStack helmet = plugin.sh.toItemStack(s.replace("team_helmet", "leatherhelmet"));
                getColourLeather(helmet, plugin.am.getTeam(player));
                player.getInventory().setHelmet(helmet);
            }
            else if (s.contains("team_chestplate")) {
                ItemStack chestplate = plugin.sh.toItemStack(s.replace("team_chestplate", "leatherchestplate"));
                getColourLeather(chestplate, plugin.am.getTeam(player));
                player.getInventory().setChestplate(chestplate);
            }
            else if (s.contains("team_leggings")) {
                ItemStack leggings = plugin.sh.toItemStack(s.replace("team_leggings", "leatherleggings"));
                getColourLeather(leggings, plugin.am.getTeam(player));
                player.getInventory().setLeggings(leggings);
            }
            else if (s.contains("team_boots")) {
                ItemStack boots = plugin.sh.toItemStack(s.replace("team_boots", "leatherboots"));
                getColourLeather(boots, plugin.am.getTeam(player));
                player.getInventory().setBoots(boots);
            }
            else {

                ItemStack is = plugin.sh.toItemStack(s);

                //Auto-equip Armour
                int itemId = SItem.toId(is.getType());
                if ((itemId >= 298 && itemId < 318) || is.getType() == Material.LEGACY_SKULL_ITEM) {
                    if ((itemId + 2) % 4 == 0) {
                        player.getInventory().setHelmet(is);
                    }
                    else if ((itemId + 1) % 4 == 0) {
                        player.getInventory().setChestplate(is);
                    }
                    else if (itemId % 4 == 0) {
                        player.getInventory().setLeggings(is);
                    }
                    else if (is.getType() != Material.LEGACY_SKULL_ITEM) {
                        player.getInventory().setBoots(is);
                    }
                    else {
                        player.getInventory().setHelmet(is);
                    }
                }
                else {
                    //Add other items.
                    player.getInventory().addItem(is);
                }

            }
        }

        if (player.hasPermission("crystalquest.admin") || player.hasPermission("crystalquest.staff") ||
                player.hasPermission("crystalquest.randomitem")) {
            boolean canCheck = true;
            ItemStack bonus = null;
            while (canCheck) {
                bonus = plugin.itemHandler.getRandomItem();
                if (bonus.getType() != Material.DIAMOND && bonus.getType() != Material.EMERALD &&
                        bonus.getType() != Material.CHAINMAIL_CHESTPLATE) {
                    canCheck = false;
                }
            }
            player.getInventory().addItem(bonus);
        }

        player.updateInventory();

        if (plugin.getConfig().getString("kit." + this.playerClass.get(player.getUniqueId()) + ".ability") != null) {
            List<String> abi = plugin.getConfig().getStringList("kit." + this.playerClass.get(player.getUniqueId()) +
                    ".ability");
            plugin.ab.getAbilities().put(player.getUniqueId(), abi);
        }

    }

    /**
     * Gives you the in-game inventory
     *
     * @param player
     *         (Player) The target player.
     */
    public void setInGameInventory(Player player) {
        saveInventory(player);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setLevel(this.LEVEL_DEFAULT);
        player.setExp(this.EXP_DEFAULT);
        player.setHealth(this.HEALTH_DEFAULT);
        player.setFoodLevel(this.FOOD_DEFAULT);
        player.setSaturation(this.SATURATION_DEFAULT);
        player.setGameMode(this.GAMEMODE_DEFAULT);
        for (PotionEffect pe : player.getActivePotionEffects()) {
            player.removePotionEffect(pe.getType());
        }
    }

    /**
     * Restores a player's inventory.
     *
     * @param player
     *         (Player) The target player.
     * @return (boolean) True if all went ok, False if there was an error.
     */
    public boolean restoreInventory(Player player) {
        try {
            player.getInventory().clear();
            player.getInventory().setContents(invStorage.get(player.getUniqueId()));
            player.getInventory().setArmorContents(armourStorage.get(player.getUniqueId()));
            player.setLevel(levelStorage.get(player.getUniqueId()));
            player.setExp(expStorage.get(player.getUniqueId()));
            player.setHealth(healthStorage.get(player.getUniqueId()));
            player.setFoodLevel(foodStorage.get(player.getUniqueId()));
            player.setSaturation(saturationStorage.get(player.getUniqueId()));
            player.setGameMode(gamemodeStorage.get(player.getUniqueId()));
            player.addPotionEffects(potionStorage.get(player.getUniqueId()));
            player.updateInventory();

            invStorage.remove(player.getUniqueId());
            armourStorage.remove(player.getUniqueId());
            levelStorage.remove(player.getUniqueId());
            expStorage.remove(player.getUniqueId());
            healthStorage.remove(player.getUniqueId());
            foodStorage.remove(player.getUniqueId());
            saturationStorage.remove(player.getUniqueId());
            gamemodeStorage.remove(player.getUniqueId());
            potionStorage.remove(player.getUniqueId());
            playerClass.remove(player.getUniqueId());

            if (player.getGameMode() != GameMode.CREATIVE) {
                player.setAllowFlight(false);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 127));
            }

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns coloured leather armour (ItemStack)
     *
     * @param leatherArmour
     *         The armour to dye.
     * @param team
     *         The player's team.
     * @return The coloured leather armour.
     */
    public ItemStack getColourLeather(ItemStack leatherArmour, CrystalQuestTeam team) {
        LeatherArmorMeta im = (LeatherArmorMeta)leatherArmour.getItemMeta();
        im.setColor(team.getColour());
        leatherArmour.setItemMeta(im);
        return leatherArmour;
    }
}