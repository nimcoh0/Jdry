package tests;

import app.example.books.Book;
import app.example.books.BookStore;
import org.junit.Assert;
import org.softauto.serializer.CallFuture;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.infrastructure.AbstractTesterImpl;
import tests.infrastructure.Listener;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Listeners(org.softauto.testng.JdryTestListener.class)
public class ExamplesTests extends AbstractTesterImpl {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExamplesTests.class);
    private List<Book> books = null;

    @BeforeSuite
    public void setup()throws Exception{
        books = new ArrayList<>();
        Book book1 = new Book("blabla","Nim");
        Book book2 = new Book("blabla","Nim");
        book1.setAuthor("a1");
        book1.setTitle("t1");
        book2.setAuthor("a2");
        book2.setTitle("t2");
        books.add(book1);
        books.add(book2);

    }

    /**
     * the tests invoke a grpc call and get the result
     * @throws Exception
     */
    @Test
    public void call_method()throws Exception{
        BookStore bookStore = tests.app_example_books_BookStore_addAllBooks(books);
        Assert.assertTrue(bookStore.getBooks().size() >= 2);
    }

    /**
     * the tests first load a data to the SUT (addAllBooks)
     * then start a process on the SUT (loopOverBooks) by invoking an grpc call  and then wait to a result of a method in the
     * process flow (getTitle) .
     * @throws Exception
     */
    @Test
    public void listen_for_method_after()throws Exception{
        try {
            CallFuture<Void> future = new CallFuture<>();
            CountDownLatch lock = new CountDownLatch(1);
            AtomicReference<java.lang.String> ref = new AtomicReference();
            tests.app_example_books_BookStore_addAllBooks(books);
            asyncTests.app_example_books_BookStore_loopOverBooks(future);
            new Listener() {
                @Override
                public void app_example_books_Book_getTitle(java.lang.String result){
                    ref.set(result);
                    //org.softauto.listener.server.ListenerObserver.getInstance().unRegister("tests.infrastructure.Listener",this);
                    lock.countDown();
                    logger.info("got Title "+ result);
                }
            };
            lock.await(10, TimeUnit.MINUTES);
            Assert.assertTrue(ref.get() != null);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
