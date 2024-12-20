package models;


class RegularUser extends User {
    public RegularUser(String username, String password) {
        super(username, password);
    }

    public void borrowBook(String title) {
        System.out.println("Book borrowed: " + title);
    }
}
