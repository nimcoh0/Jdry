package app.server;

import app.example.books.BookStore;
import io.vertx.core.AbstractVerticle;

public class Main extends AbstractVerticle {

    BookStore bookStore = null;

    @Override
    public void start() throws Exception {
        bookStore =  new BookStore();
    }

}
