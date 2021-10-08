package baeldung.server;

import baeldung.cucumber.books.BookStore;
import io.vertx.core.AbstractVerticle;

public class Main extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        BookStore bookStore = new BookStore();
    }

}
