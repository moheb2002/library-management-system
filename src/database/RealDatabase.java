package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RealDatabase implements Database {
    private static final RealDatabase INSTANCE = new RealDatabase(); 
    private Connection connection;
    private final String URL = "jdbc:mysql://localhost:3306/library_data";
    private final String USER = "root";
    private final String PASSWORD = "moheb2002";

    private RealDatabase() {
        try {
            connect();
        } catch (SQLException e) {
            System.err.println("Initial database connection error: " + e.getMessage());
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public static RealDatabase getInstance() {
        return INSTANCE;
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Reconnecting to the database...");
                connect();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection: " + e.getMessage());
            throw new RuntimeException("Failed to reconnect to database", e);
        }
        return connection;
    }

    private void connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            throw new SQLException("Driver not found", e);
        }
    }
}
