package nl.sugcube.crystalquest;

import nl.sugcube.crystalquest.api.CrystalQuestAPI;
import nl.sugcube.crystalquest.command.CrystalQuestCommandManager;
import nl.sugcube.crystalquest.data.Database;
import nl.sugcube.crystalquest.data.LoadData;
import nl.sugcube.crystalquest.data.QueryEconomy;
import nl.sugcube.crystalquest.data.SaveData;
import nl.sugcube.crystalquest.economy.Economy;
import nl.sugcube.crystalquest.game.*;
import nl.sugcube.crystalquest.inventorymenu.PickTeam;
import nl.sugcube.crystalquest.inventorymenu.SelectClass;
import nl.sugcube.crystalquest.inventorymenu.SpectateArena;
import nl.sugcube.crystalquest.items.CurseListener;
import nl.sugcube.crystalquest.items.ItemHandler;
import nl.sugcube.crystalquest.items.ItemListener;
import nl.sugcube.crystalquest.items.Wand;
import nl.sugcube.crystalquest.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * @author SugarCaney
 */
public class CrystalQuest extends JavaPlugin {

    public Database database;
    public QueryEconomy queryEconomy;

    public ArenaManager arenaManager;
    public PickTeam menuPickTeam;
    public InventoryManager inventoryManager;
    public StringHandler stringHandler;
    public SelectClass menuSelectClass;
    public SpectateArena menuSpectate;
    public CrystalQuestCommandManager commandExecutor;
    public Broadcast broadcast;
    public SaveData saveData;
    public LoadData loadData;
    public SignHandler signHandler;
    public ItemHandler itemHandler;
    public ParticleHandler particleHandler;
    public CurseListener curseListener;
    public ClassUtils classes;
    public Economy economy;
    public Ability ability;

    public Teams teams;
    public PluginManager pluginManager;
    public Protection protection;
    public DeathMessages deathListener;
    public EntityListener entityListener;
    public InventoryListener inventoryListener;
    public PlayerListener playerListener;
    public SignListener signListener;
    public ItemListener itemListener;
    public ProjectileListener projectileListener;
    public ArenaListener arenaListener;
    public Wand wand;

    public void onEnable() {
        /*
         * Load config.yml and data.yml
		 */
        getLogger().info("Loading configuration...");
        File file = new File(getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            try {
                getConfig().options().copyDefaults(true);
                saveConfig();
                getLogger().info("Generated config.yml succesfully!");
            }
            catch (Exception e) {
                getLogger().info("Failed to generate config.yml!");
            }
        }

        getLogger().info("Loading data file...");
        File df = new File(getDataFolder() + File.separator + "data.yml");
        if (!df.exists()) {
            try {
                reloadData();
                saveData();
                getLogger().info("Generated data.yml succesfully!");
            }
            catch (Exception e) {
                getLogger().info("Failed to generate data.yml!");
            }
        }

        getLogger().info("Loading language file...");
        File lf = new File(getDataFolder() + File.separator + "lang.yml");
        if (!lf.exists()) {
            try {
                getLang().options().copyDefaults(true);
                saveLang();
                this.getLogger().info("Generated lang.yml succesfully!");
            }
            catch (Exception e) {
                this.getLogger().info("Failed to generate lang.yml!");
            }
        }

        /*
         * Establishing database connection
         */
        getLogger().info("Establishing database connection...");
        if (getConfig().getBoolean("mysql.enabled")) {
            database = new Database(
                    getConfig().getString("mysql.username"),
                    getConfig().getString("mysql.password"),
                    getConfig().getString("mysql.database"),
                    getConfig().getString("mysql.host"),
                    getConfig().getInt("mysql.port")
            );
            database.initialize();

            queryEconomy = new QueryEconomy(this);
            queryEconomy.createTables();
        }

		/*
         * SPECIAL FOR THE 1.7.5/1.8 UPDATE:
		 * CHANGING FROM PLAYER NAMES TO UUIDs
		 */
        getLogger().info("Configurating state...");
		Broadcast.plugin = this;
        Broadcast.setMessages();

        arenaManager = new ArenaManager(this);
        menuPickTeam = new PickTeam(this);
        inventoryManager = new InventoryManager(this);
        stringHandler = new StringHandler(this);
        menuSelectClass = new SelectClass(this);
        menuSpectate = new SpectateArena(this);
        commandExecutor = new CrystalQuestCommandManager(this);
        saveData = new SaveData(this);
        loadData = new LoadData(this);
        signHandler = new SignHandler(this, arenaManager);
        itemHandler = new ItemHandler(this);
        particleHandler = new ParticleHandler(this);
        classes = new ClassUtils(this);
        protection = new Protection(this);
        deathListener = new DeathMessages(this);
        entityListener = new EntityListener(this);
        inventoryListener = new InventoryListener(this);
        playerListener = new PlayerListener(this);
        signListener = new SignListener(this);
        itemListener = new ItemListener(this);
        projectileListener = new ProjectileListener(this);
        arenaListener = new ArenaListener(this);
        wand = new Wand(this);
        teams = new Teams(this);
        curseListener = new CurseListener(this);
        PluginDescriptionFile pdfFile = this.getDescription();
        this.pluginManager = getServer().getPluginManager();
        this.economy = new Economy(this);
        this.ability = new Ability(this);

		/*
         * Registering Events:
		 */
        pluginManager.registerEvents(entityListener, this);
        pluginManager.registerEvents(inventoryListener, this);
        pluginManager.registerEvents(playerListener, this);
        pluginManager.registerEvents(signListener, this);
        pluginManager.registerEvents(itemListener, this);
        pluginManager.registerEvents(projectileListener, this);
        pluginManager.registerEvents(deathListener, this);
        pluginManager.registerEvents(protection, this);
        pluginManager.registerEvents(ability, this);
        pluginManager.registerEvents(wand, this);
        pluginManager.registerEvents(arenaListener, this);
        this.economy.registerEvents(pluginManager);

		/*
         * Registering Commands
		 */
        this.getCommand("cq").setExecutor(commandExecutor);
        this.getCommand("cq").setTabCompleter(commandExecutor);

		/*
		 * Starting the game-loops
		 * Initialize all arenas
		 */
        getLogger().info("Setting up areans...");
        arenaManager.registerGameLoop();
        arenaManager.registerCrystalSpawningSequence();
        arenaManager.registerItemSpawningSequence();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ParticleHandler(this), 1L, 1L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, curseListener, 20L, 20L);
        signHandler.startSignUpdater();
		
		/*
		 * Loading Data
		 */
        LoadData.loadArenas();        //ARENAS
        LoadData.loadLobbySpawn();    //LOBBYSPAWN
		
		/*
		 * Pass plugin instance to the API
		 */
        CrystalQuestAPI.setPlugin(this);
		
		/*
		 * Check for updates
		 */
        if (getConfig().getBoolean("updates.check-for-updates")) {
            getLogger().info("Checking for updates...");
            Update uc = new Update(69421, this.getDescription().getVersion());
            if (uc.query()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CrystalQuest] <> A new version of CrystalQuest is " +
                        "avaiable! Get it at the BukkitDev page! <>");
            }
            else {
                Bukkit.getConsoleSender().sendMessage("[CrystalQuest] CrystalQuest is up-to-date!");
            }
        }
		
		/*
		 * Resets all arenas and makes sure everything is ready to go.
		 */
        getLogger().info("Resetting areans...");
        for (Arena a : arenaManager.getArenas()) {
            a.resetArena(true);
        }

        LoadData.loadSigns();        //Load the lobby-signs

        itemHandler.addAllItems();

        // Finally done enabling :)
        getLogger().info("CrystalQuest v" + pdfFile.getVersion() + " has been enabled!");
    }

    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
		
		/*
		 * Kicks players from game on reload.
		 */
        for (Arena a : arenaManager.arenas) {
            a.removePlayers();
        }
		
		/*
		 * Reset all arenas
		 */
        for (Arena a : arenaManager.getArenas()) {
            a.resetArena(false);
        }
		
		/*
		 * Saves data
		 */
        SaveData.saveArenas();
        SaveData.saveSigns();
        SaveData.saveLobbySpawn();

        /*
         * Release database resources.
         */
        if (database != null) {
            database.dispose();
        }

        this.getLogger().info("[CrystalQuest] CrystalQuest v" + pdfFile.getVersion() + " has been disabled!");

    }

    /*
     * Use a file (data.yml) to store data
     */
    private FileConfiguration data = null;
    private File dataFile = null;

    /**
     * Reloads the data-file
     */
    public void reloadData() {
        if (dataFile == null) {
            dataFile = new File(getDataFolder(), "data.yml");
        }
        data = YamlConfiguration.loadConfiguration(dataFile);

        InputStream dataStream = this.getResource("data.yml");
        if (dataStream != null) {
            InputStreamReader reader = new InputStreamReader(dataStream);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
            data.setDefaults(defConfig);
        }
    }

    /**
     * Gets the data.yml
     *
     * @return (FileConfiguration) Data.yml
     */
    public FileConfiguration getData() {
        if (data == null) {
            this.reloadData();
        }
        return data;
    }

    /**
     * Saves the data.yml
     */
    public void saveData() {
        if (data == null || dataFile == null) {
            return;
        }
        try {
            getData().save(dataFile);
        }
        catch (Exception ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + dataFile, ex);
        }
    }

    /*
     * Use a file (language.yml) to store localized messages
     */
    private FileConfiguration lang = null;
    private File langFile = null;

    /**
     * Reloads the lang-file
     */
    public void reloadLang() {
        if (langFile == null) {
            langFile = new File(getDataFolder(), "lang.yml");
        }
        lang = YamlConfiguration.loadConfiguration(langFile);

        InputStream langStream = this.getResource("lang.yml");
        if (langStream != null) {
            InputStreamReader reader = new InputStreamReader(langStream);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
            lang.setDefaults(defConfig);
        }
    }

    /**
     * Gets the lang.yml
     *
     * @return (FileConfiguration) lang.yml
     */
    public FileConfiguration getLang() {
        if (lang == null) {
            this.reloadLang();
        }
        return lang;
    }

    /**
     * Saves the lang.yml
     */
    public void saveLang() {
        if (lang == null || langFile == null) {
            return;
        }
        try {
            getLang().save(langFile);
        }
        catch (Exception ex) {
            this.getLogger().log(Level.SEVERE, "Could not save " + langFile, ex);
        }
    }

    /**
     * Gets the InventoryManager of the plugin
     *
     * @return (InventoryManager) The plugin's inventory manager.
     */
    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    /**
     * Gets the ArenaManager of the plugin
     *
     * @return (ArenaManager) The plugin's arenas manager.
     */
    public ArenaManager getArenaManager() {
        return this.arenaManager;
    }

}