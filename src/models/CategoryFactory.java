package models;



public class CategoryFactory {
    public static BookCategory createCategory(String name) {
        return new BookCategory(name);
    }
}
