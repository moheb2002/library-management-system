package models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

class RegularDashboardView {
    private final String regularUsername;
    private DefaultListModel<String> bookListModel;

    public RegularDashboardView(String regularUsername) {
        this.regularUsername = regularUsername;
    }

    public void showRegularDashboard() {
        JFrame frame = new JFrame("Regular User Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JLabel headerLabel = new JLabel("Available Books", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        bookListModel = new DefaultListModel<>();
        JList<String> bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton borrowButton = new JButton("Borrow Book");
        JButton viewBorrowedBooksButton = new JButton("View Borrowed Books");
        buttonPanel.add(borrowButton);
        buttonPanel.add(viewBorrowedBooksButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        loadAvailableBooks();

        borrowButton.addActionListener((ActionEvent e) -> borrowBookAction(bookList));
        viewBorrowedBooksButton.addActionListener((ActionEvent e) -> viewBorrowedBooksAction());

        frame.setVisible(true);
    }

    private void loadAvailableBooks() {
        bookListModel.clear();
        List<String> books = BookManager.getAllBooks();
        if (books.isEmpty()) {
            bookListModel.addElement("No books available.");
        } else {
            books.forEach(bookListModel::addElement);
        }
    }

    private void borrowBookAction(JList<String> bookList) {
        String selectedBook = bookList.getSelectedValue();
        if (selectedBook == null || selectedBook.equals("No books available.")) {
            JOptionPane.showMessageDialog(null, "Please select a valid book to borrow.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (BookManager.borrowBook(regularUsername, selectedBook)) {
            JOptionPane.showMessageDialog(null, "Book borrowed successfully: " + selectedBook, "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAvailableBooks();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to borrow the book. It may already be borrowed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewBorrowedBooksAction() {
        List<String> borrowedBooks = BookManager.getBorrowedBooks(regularUsername);
        if (borrowedBooks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You haven't borrowed any books yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder("Your Borrowed Books:\n");
            borrowedBooks.forEach(book -> message.append("- ").append(book).append("\n"));
            JOptionPane.showMessageDialog(null, message.toString(), "Borrowed Books", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
