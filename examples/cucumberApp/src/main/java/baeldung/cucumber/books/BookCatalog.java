package baeldung.cucumber.books;

import org.softauto.annotations.ExposedForTesting;
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
    @ExposedForTesting(description = "^I have the following books in the store$")
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "BookCatalog [books=" + books + "]";
    }
}
