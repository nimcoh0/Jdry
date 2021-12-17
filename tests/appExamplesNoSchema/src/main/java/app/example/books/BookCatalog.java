package app.example.books;


import java.util.ArrayList;
import java.util.List;

public class BookCatalog {

    private List<Book> books = new ArrayList<>();


    public void addBook(Book book) {
        books.add(book);
    }


    public List<Book> getBooks() {
        return books;
    }


    public void printBook(Book book){
        System.out.print(book.getTitle());
    }


    public void printBook1(Book book){
        System.out.print(book.getTitle());
    }

    @Override
    public String toString() {
        return "BookCatalog [books=" + books + "]";
    }
}
