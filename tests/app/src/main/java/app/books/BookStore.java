package app.books;

import org.softauto.annotations.ExposedForTesting;
import org.softauto.annotations.RPC;

import java.util.*;
import java.util.stream.Collectors;

public class BookStore {
    private List<Book> books = new ArrayList<>();

    @RPC
    @ExposedForTesting
    public BookStore addBook(Book book) {
        books.add(book);
        return this;
    }

    @RPC
    @ExposedForTesting
    public BookStore addAllBooks(Collection<Book> books) {
        this.books.addAll(books);
        return this;
    }

    @RPC
    @ExposedForTesting
    public List<Book> booksByAuthor(String author) {
        return books.stream()
          .filter(book -> Objects.equals(author, book.getAuthor()))
          .collect(Collectors.toList());
    }

    @RPC
    @ExposedForTesting
    public Optional<Book> bookByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst();
    }

    public List<Book> getBooks(){
        return books;
    }

    @RPC
    @ExposedForTesting
    public void loopOverBooks(){
        for(Book book : books){
            book.getTitle();
        }
    }
}
