package models;


import models.BookManager;

class AdminUser extends User {
    public AdminUser(String username, String password) {
        super(username, password);
    }

    public void addBook(String title, String author, String category) {
        if (!isAdmin()) {
            throw new SecurityException("Only admins can add books.");
        }

        BookManager manager = new BookManager();
        manager.addBook(title, author, category);
    }

    private boolean isAdmin() {
        return true; 
    }
}
