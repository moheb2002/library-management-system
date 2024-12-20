package models;

import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookManager {

    public static List<String> getAllBooks() {
        String query = "SELECT title FROM books";
        return fetchBooks(query, null);
    }

    public static List<String> getBorrowedBooks(String username) {
        String query = "SELECT book_title FROM borrowed_books WHERE user_username = ?";
        return fetchBooks(query, username);
    }

    public static boolean borrowBook(String username, String bookTitle) {
        if (!isBookAvailable(bookTitle)) {
            System.err.println("Book does not exist in the database.");
            return false;
        }

        String query = "INSERT INTO borrowed_books (user_username, book_title, borrow_date) VALUES (?, ?, CURRENT_DATE)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, bookTitle);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error borrowing book: " + e.getMessage());
            return false;
        }
    }

    
public static boolean addBook(String title, String author, String category) {
    String query = "INSERT INTO books (title, author, category) VALUES (?, ?, ?)";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setString(1, title);
        stmt.setString(2, author);
        stmt.setString(3, category);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error adding book: " + e.getMessage());
        return false;
    }
}

    private static List<String> fetchBooks(String query, String username) {
        List<String> books = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (username != null) stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching books: " + e.getMessage());
        }
        return books;
    }

    private static boolean isBookAvailable(String bookTitle) {
        String query = "SELECT COUNT(*) FROM books WHERE title = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, bookTitle);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking book availability: " + e.getMessage());
            return false;
        }
    }
}
