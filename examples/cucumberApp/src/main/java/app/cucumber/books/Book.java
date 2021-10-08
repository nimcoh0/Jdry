package app.cucumber.books;

public class Book {

    private String title;
    private String author;
    private int id;
    
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }
    
    public Book() {}

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book [title=" + title + ", author=" + author + "]";
    }
}
