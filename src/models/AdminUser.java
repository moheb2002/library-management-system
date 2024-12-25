package models;

class AdminUser extends User {
    public AdminUser(String username, String password) {
        super(username, password);
    }

    public void addBook(String title, String author, String category) {
        BookManager.addBook(title, author, category);
    }
}
