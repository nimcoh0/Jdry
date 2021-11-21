package app.example.books;

import org.softauto.annotations.ListenerForTesting;
import org.softauto.annotations.RPC;

import java.util.ArrayList;
import java.util.List;

public class BookCatalog {

    private List<Book> books = new ArrayList<>();

    @RPC
    @ListenerForTesting
    public void addBook(Book book) {
        books.add(book);
    }

    @RPC
    @ListenerForTesting
    public List<Book> getBooks() {
        return books;
    }

    @RPC
    @ListenerForTesting
    public void printBook(Book book){
        System.out.print(book.getTitle());
    }

    @Override
    public String toString() {
        return "BookCatalog [books=" + books + "]";
    }
}
