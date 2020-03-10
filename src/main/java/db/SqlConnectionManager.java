package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Wait;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlConnectionManager {
    private final static Logger log = LoggerFactory.getLogger(SqlConnectionManager.class);
    private static Map<String, Connection> connectionList = new HashMap<>();
    private static List<String> destroyKeys = new ArrayList<>();
    public static final String JDBC_DRIVER = "org.h2.Driver";  //ToDo Брать из пропертей

    public static Connection getConnection(final String url, final String user, final String password) {
        registerDriver(JDBC_DRIVER);
        String mapKey = buildDbConnectionKey(url, user, password);
        Connection con = null;
        if (!connectionList.containsKey(mapKey)) {
            con = createNewConnection(url, user, password);
        } else {
            con = connectionList.get(mapKey);
            if (isConnected(con)) {
                return con;
            } else {
                closeConnection(con);
                con = createNewConnection(url, user, password);
            }
        }
        connectionList.put(mapKey, con);
        return con;
    }

    public static void destroyAllConnections() {
        destroyKeys.clear();
        connectionList.values().forEach(SqlConnectionManager::closeConnection);
        connectionList.clear();
    }

    private static boolean isConnected(Connection conn) {
        if (conn == null) {
            return false;
        }
        try {
            if (conn.isClosed()) {
                return false;
            }
            if (conn.isValid(20)){  //ToDo Брать из пропертей
                return true;
            }
        } catch (SQLException e) {
            log.error("Error during DB connection check : " + e);
        }
        closeConnection(conn);
        return false;
    }

    private static Connection createNewConnection(final String url, final String user, final String password) {
        int attempt = 0;
        Connection con = null;
        while (attempt < 5) { // ToDo Брать из пропертей
            try {
                con = DriverManager.getConnection(url, user, password);
                log.info("Connection established : " + url);
                return con;

            } catch (SQLException e) {
                log.debug("Error in createNewConnection method : " + e);
                log.debug("Will sleep 1 sec and try again.");
                Wait.wait(1000);
                closeConnection(con);
                attempt+= 1;
            }
        }
        log.error("Can get connection : " + url + " for user/password " + user + "/" + password);
        return null;
    }

    private static boolean closeConnection(Connection con) {
        if (con == null) {
            return true;
        }
        int attempt = 0;
        while (attempt < 5) { //ToDo Брать из пропертей
            try {
                if (!con.isClosed()) {
                    con.close();
                }
                return true;
            } catch (SQLException e) {
                log.error("Can't close connection");
                log.debug("Error in closeConnection method : " + e);
            }
            attempt+= 1;
        }
        return false;
    }

    private static String buildDbConnectionKey(final String url, final String user, final String password) {
        return url + "_" + user + "_" + password;
    }
    private static void registerDriver(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("Class : " + className + " not registered");
            log.error("Error : " + e);
        }
    }
}
