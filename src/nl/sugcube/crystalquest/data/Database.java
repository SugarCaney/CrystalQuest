package nl.sugcube.crystalquest.data;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import nl.sugcube.crystalquest.util.CrystalQuestException;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author SugarCaney
 */
public class Database implements AutoCloseable {

    static {
        Properties properties = new Properties(System.getProperties());
        properties.put("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        properties.put("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "SEVERE");
        System.setProperties(properties);
    }

    private ComboPooledDataSource dataSource;
    private String username;
    private String password;
    private String database;
    private String host;
    private int port;

    public Database(String username, String password, String database, String host, int port) {
        this.username = username;
        this.password = password;
        this.database = database;
        this.host = host;
        this.port = port;
    }

    /**
     * Initialises the database, allocating resources.
     */
    public void initialize() {
        dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        }
        catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        dataSource.setJdbcUrl(makeUrl());
        dataSource.setUser(username);
        dataSource.setPassword(password);
    }

    private String makeUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false";
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        }
        catch (SQLException e) {
            throw new CrystalQuestException("Problem with getting database connection.", e);
        }
    }

    /**
     * Disposes all used resources.
     */
    public void dispose() {
        if (dataSource == null) {
            throw new IllegalStateException("No data source has been initialized.");
        }
        dataSource.close();
    }

    @Override
    public void close() throws Exception {
        dispose();
    }
}
