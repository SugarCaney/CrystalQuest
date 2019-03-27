package nl.sugcube.crystalquest.data;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.economy.ShopUpgrade;
import nl.sugcube.crystalquest.util.CrystalQuestException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author SugarCaney
 */
public class QueryEconomy {

    private CrystalQuest plugin;
    private String upgradeTableName;
    private String classTableName;

    public QueryEconomy(CrystalQuest plugin) {
        this.plugin = plugin;

        String prefix = plugin.getConfig().getString("mysql.table-prefix");
        upgradeTableName = prefix + "economy";
        classTableName = prefix + "classes";
    }

    /**
     * Creates all tables necessary for the economy queries to work, if they do not exist yet.
     */
    public void createTables() {
        createUpgradeTable();
        createClassTable();
    }

    private void createUpgradeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `" + upgradeTableName + "`" +
                "(" +
                "  `uuid`      VARCHAR(36) NOT NULL," +
                "  `balance`   INT         NOT NULL DEFAULT '0'," +
                "  `buff`      INT         NOT NULL DEFAULT '0'," +
                "  `debuff`    INT         NOT NULL DEFAULT '0'," +
                "  `explosive` INT         NOT NULL DEFAULT '0'," +
                "  `weaponry`  INT         NOT NULL DEFAULT '0'," +
                "  `creepers`  INT         NOT NULL DEFAULT '0'," +
                "  `wolf`      INT         NOT NULL DEFAULT '0'," +
                "  `xp`        INT         NOT NULL DEFAULT '0'," +
                "  `smash`     INT         NOT NULL DEFAULT '0'," +
                "  `win`       INT         NOT NULL DEFAULT '0'," +
                "  `blood`     INT         NOT NULL DEFAULT '0'," +
                "  PRIMARY KEY (`uuid`)" +
                ");";
        try (Connection connection = plugin.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Could not create economy table!", e);
        }
    }

    private void createClassTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `" + classTableName + "`" +
                "(" +
                "  `uuid`    VARCHAR(36)  NOT NULL," +
                "  `classid` VARCHAR(128) NOT NULL," +
                "  PRIMARY KEY (`uuid`, `classid`)" +
                ");";
        try (Connection connection = plugin.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Could not create class table!", e);
        }
    }

    public int getBalance(UUID uuid) {
        return getBalance(uuid.toString());
    }

    public int getBalance(String uuid) {
        String sql = "SELECT `balance` FROM `" + upgradeTableName + "` WHERE uuid=?;";
        try (Connection connection = plugin.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("balance");
            }
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Could not query the balance of user " + uuid, e);
        }
        return 0;
    }

    public void setBalance(UUID uuid, int newBalance) {
        setBalance(uuid.toString(), newBalance);
    }

    public void setBalance(String uuid, int newBalance) {
        addNewPlayerEntry(uuid);
        String sql = "UPDATE `" + upgradeTableName + "` SET `balance`=? WHERE `uuid`=?;";
        try (Connection connection = plugin.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, newBalance);
            statement.setString(2, uuid);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Could not set balance of " + uuid, e);
        }
    }

    public int getUpgradeLevel(UUID uuid, ShopUpgrade upgrade) {
        return getUpgradeLevel(uuid.toString(), upgrade);
    }

    public int getUpgradeLevel(String uuid, ShopUpgrade upgrade) {
        String sql = "SELECT `" + upgrade.getId() + "` FROM `" + upgradeTableName + "` WHERE uuid=?;";
        try (Connection connection = plugin.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(upgrade.getId());
            }
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Could not query the '" + upgrade.getId() + "' upgrade level of user " + uuid, e);
        }
        return 0;
    }

    public void setUpgradeLevel(UUID uuid, ShopUpgrade upgrade, int newLevel) {
        setUpgradeLevel(uuid.toString(), upgrade, newLevel);
    }

    public void setUpgradeLevel(String uuid, ShopUpgrade upgrade, int newLevel) {
        addNewPlayerEntry(uuid);
        String sql = "UPDATE `" + upgradeTableName + "` SET `" + upgrade.getId() + "`=? WHERE `uuid`=?;";
        try (Connection connection = plugin.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, newLevel);
            statement.setString(2, uuid);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Could not set level of upgrade " + upgrade.getId() + " for " + uuid, e);
        }
    }

    /**
     * Does not insert a new entry when a player with the given uuid already exists.
     */
    public void addNewPlayerEntry(UUID uuid) {
        addNewPlayerEntry(uuid.toString());
    }

    /**
     * Does not insert a new entry when a player with the given uuid already exists.
     */
    public void addNewPlayerEntry(String uuid) {
        String sql = "INSERT IGNORE INTO `" + upgradeTableName + "`(uuid) VALUES (?);";
        try (Connection connection = plugin.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Could not initialize table entry for " + uuid, e);
        }
    }

    public boolean hasClass(UUID uuid, String classId) {
        return hasClass(uuid.toString(), classId);
    }

    public boolean hasClass(String uuid, String classId) {
        String sql = "SELECT DISTINCT `uuid` FROM `" + classTableName + "` WHERE `uuid`=? AND `classid`=?;";
        try (Connection connection = plugin.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            statement.setString(2, classId);

            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Could not query if " + uuid + " has class " + classId, e);
        }
    }

    public void registerClass(UUID uuid, String classId) {
        registerClass(uuid.toString(), classId);
    }

    public void registerClass(String uuid, String classId) {
        String sql = "INSERT IGNORE INTO `" + classTableName + "`(uuid,classid) VALUES (?,?);";
        try (Connection connection = plugin.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            statement.setString(2, classId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Could not register class " + classId + " for " + uuid, e);
        }
    }

    public Set<String> getAllClasses(UUID uuid) {
        return getAllClasses(uuid.toString());
    }

    public Set<String> getAllClasses(String uuid) {
        String sql = "SELECT `classid` FROM `" + classTableName + "` WHERE `uuid`=?;";
        try (Connection connection = plugin.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);

            Set<String> allClasses = new HashSet<>();
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                allClasses.add(result.getString("classid"));
            }
            return allClasses;
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Could not query classes of " + uuid, e);
        }
    }
}