package baeldung.cucumber.books;

import org.softauto.annotations.ExposedForTesting;
import org.softauto.annotations.ListenerForTesting;
import org.softauto.annotations.RPC;

import java.util.*;
import java.util.stream.Collectors;

public class BookStore {
    private List<Book> books = new ArrayList<>();

    @RPC
    @ExposedForTesting
    public void addBook(Book book) {
        books.add(book);
    }

    @RPC
    @ExposedForTesting
    public void addAllBooks(Collection<Book> books) {
        this.books.addAll(books);
    }

    @RPC
    @ExposedForTesting(description = "^I search for books by author (.+)$")
    public List<Book> booksByAuthor(String author) {
        return books.stream()
          .filter(book -> Objects.equals(author, book.getAuthor()))
          .collect(Collectors.toList());
    }

    @RPC
    @ExposedForTesting(description = "^I search for books by title (.+)$")
    public Optional<Book> bookByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst();
    }
}
