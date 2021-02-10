package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database_Connector {

    private static Connection conn = null;
    private static String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=playlist;user=javaTeste;password=123";

    public Database_Connector() {}

    //----------------------------
    public static Connection getConnection() throws SQLException {

        if (conn == null || conn.isClosed())
            try {conn = DriverManager.getConnection(connectionUrl);
            }
            catch (SQLException e) {
                e.printStackTrace(); }
        return conn;
    }

}
