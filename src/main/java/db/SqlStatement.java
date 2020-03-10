package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlStatement {
    private final static Logger log = LoggerFactory.getLogger(SqlStatement.class);

    public static Statement createStatement(Connection connection) {
        Statement statement = null;
        try {
            statement =  connection.createStatement();
            return statement;
        } catch (SQLException e) {
            log.debug("Error in createStatement method : " + e);
        }
        return statement;
    }

    public static boolean executeUpdateStatement(Connection connection, String query) {
        if (connection == null) {
            return false;
        }

        log.info("Executing query : " + query);
        Statement statement = createStatement(connection);
        try {
            statement.executeUpdate(query);
            statement.getConnection().commit();
            statement.close();
            return true;
        } catch (SQLException e) {
            log.error("Can't execute query : " + query);
            log.error("Error : " + e);
            return false;
        }
    }

    public static List<Map<String, Object>> executeSelectStatement(Connection connection, String query) {
        if (connection == null) {
            return null;
        }

        log.info("Executing query : " + query);
        Statement statement = createStatement(connection);

        try {
            ResultSet rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            List resultRows = new ArrayList();
            while(rs.next()){
                Map oneRow = new HashMap();
                for (int i = 1; i <= columnCount; i++){
                    oneRow.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultRows.add(oneRow);
            }
            if (resultRows.isEmpty()) {
                log.info("Empty result of query : " + query);
            }
                statement.close();
            return resultRows;
        } catch (SQLException e) {
            log.error("Can't execute query : " + query);
            log.error("Error : " + e);
            return null;
        }

    }

}
