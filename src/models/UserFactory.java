package models;

import database.Database;
import database.DatabaseProxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserFactory {

    private static final Database database = new DatabaseProxy();

    public static User createUser(String type, String username, String password) {
        if (type.equalsIgnoreCase("admin")) {
            return new AdminUser(username, password);
        } else if (type.equalsIgnoreCase("regular")) {
            return new RegularUser(username, password);
        }
        return null;
    }

    public static boolean doesUserExist(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }

    public static boolean authenticateUser(String username, String password, String type) {
        String query = "SELECT * FROM users WHERE username = ? AND type = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, type);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String encryptedPassword = rs.getString("password");
                String decryptedPassword = EncryptionUtils.decrypt(encryptedPassword);

                if (decryptedPassword == null) {
                    System.err.println("Decryption failed for user: " + username);
                    return false;
                }
                return decryptedPassword.equals(password);
            }
        } catch (SQLException e) {
            System.err.println("SQL error during authentication: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid password format: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during authentication: " + e.getMessage());
        }
        return false;
    }

    public static boolean registerUser(String username, String password, String type) {
        if (doesUserExist(username)) {
            System.err.println("User already exists with username: " + username);
            return false;
        }

        String encryptedPassword = EncryptionUtils.encrypt(password);

        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            System.err.println("Encryption failed for password.");
            return false;
        }

        String query = "INSERT INTO users (username, password, type) VALUES (?, ?, ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, encryptedPassword);
            stmt.setString(3, type);
            stmt.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Username already exists: " + username);
        } catch (SQLException e) {
            System.err.println("SQL error during user registration: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during user registration: " + e.getMessage());
        }
        return false;
    }
}
