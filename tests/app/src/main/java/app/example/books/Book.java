package app.example.books;


import org.softauto.annotations.DefaultValue;
import org.softauto.annotations.ExposedForTesting;
import org.softauto.annotations.ListenerForTesting;
import org.softauto.annotations.RPC;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private String title;
    private String author;
    private int id;
    List<String> list = new ArrayList<>();

    public Book(){

    }


    @RPC
    @ExposedForTesting
    public Book(@DefaultValue("blabla") String title, @DefaultValue("Nim") String author) {
        this.title = title;
        this.author = author;
        id = 1;
        String b = "";
    }

    @RPC
    @ListenerForTesting
    public String getTitle() {
        return title;
    }

    @RPC
    @ListenerForTesting
    public void setTitle(String title) {
        this.title = title;
        list.add(title);
    }

    @RPC
    @ListenerForTesting
    public String getAuthor() {
        return author;
    }

    @RPC
    @ListenerForTesting
    public void setAuthor(String author) {
        this.author = author;
    }

    @RPC
    @ListenerForTesting
    public int getId() {
        return id;
    }

    @RPC
    @ExposedForTesting
    public String hello() {
         return "hello";
    }

    @RPC
    @ListenerForTesting
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book [title=" + title + ", author=" + author + "]";
    }
}
