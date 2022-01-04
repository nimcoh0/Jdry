package app.example.books;


import org.softauto.annotations.ExposedForTesting;
import org.softauto.annotations.RPC;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class BookStore {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getLogger(BookStore.class);
    private List<Book> books = new ArrayList<>();
    int counter = 0;


    @RPC
    @ExposedForTesting
    BookCatalog bookCatalog = new BookCatalog();

    @RPC
    @ExposedForTesting
    public BookStore addBook(Book book) {
        books.add(book);
        return this;
    }

    @RPC
    @ExposedForTesting
    public Book getBookById(int id){
        AtomicReference<Book> ref = new AtomicReference();
        books.forEach(b ->{
            if(b.getId()== id){
                ref.set(b);
            }
        });
        return ref.get();
    }


    @RPC
    @ExposedForTesting
    public BookStore updateBook(Book book,String newTitle) {
       book.setTitle(newTitle);
        return this;
    }

    @RPC
    @ExposedForTesting
    public BookStore addAllBooks(Collection<Book> books) {
        for(Book book : books){
            book.setId(counter);
            ++counter;
            this.books.add(book);
        }
        logger.debug("execute addAllBooks");
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

    @RPC
    @ExposedForTesting
    public void printBooks(){
        for(Book book : books){
            new BookCatalog().printBook(book);
        }
    }

    @RPC
    @ExposedForTesting
    public void printBooks(List<Book> books){
        for(Book book : books){
            new BookCatalog().printBook(book);
        }
    }


}
