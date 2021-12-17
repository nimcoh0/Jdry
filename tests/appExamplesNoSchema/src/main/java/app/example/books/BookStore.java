package app.example.books;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class BookStore {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(BookStore.class);
    private List<Book> books = new ArrayList<>();
    int counter = 0;


    public BookStore addBook(Book book) {
        books.add(book);
        return this;
    }


    public Book getBookById(int id){
        AtomicReference<Book> ref = new AtomicReference();
        books.forEach(b ->{
            if(b.getId()== id){
                ref.set(b);
            }
        });
        return ref.get();
    }



    public BookStore updateBook(Book book,String newTitle) {
       book.setTitle(newTitle);
        return this;
    }


    public BookStore addAllBooks(Collection<Book> books) {
        for(Book book : books){
            book.setId(counter);
            ++counter;
            this.books.add(book);
        }
        logger.debug("execute addAllBooks");
       return this;
    }


    public List<Book> booksByAuthor(String author) {
        return books.stream()
          .filter(book -> Objects.equals(author, book.getAuthor()))
          .collect(Collectors.toList());
    }


    public Optional<Book> bookByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst();
    }

    public List<Book> getBooks(){
        return books;
    }


    public void loopOverBooks(){
        for(Book book : books){
            book.getTitle();
        }
    }



    public void printBooks(){
        for(Book book : books){
            new BookCatalog().printBook(book);
        }
    }


    public void printBooks(List<Book> books){
        for(Book book : books){
            new BookCatalog().printBook(book);
        }
    }


    public void printBooks1(List<Book> books){
        for(Book book : books){
            new BookCatalog().printBook1(book);
        }
    }
}
