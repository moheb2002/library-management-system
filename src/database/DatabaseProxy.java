package database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseProxy implements Database {
    private RealDatabase realDatabase;

    @Override
    public Connection getConnection() {
        if (realDatabase == null) {
            realDatabase = RealDatabase.getInstance();
        } else {
            try {
                Connection connection = realDatabase.getConnection();
                if (connection == null || connection.isClosed()) {
                    System.out.println("Reconnecting to database...");
                    realDatabase = RealDatabase.getInstance(); 
                }
            } catch (SQLException e) {
                System.err.println("Failed to reconnect to database: " + e.getMessage());
            }
        }
        return realDatabase.getConnection();
    }
}
