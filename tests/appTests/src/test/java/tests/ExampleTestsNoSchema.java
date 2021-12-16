package tests;

import app.example.books.Book;
import app.example.books.BookStore;
import org.junit.Assert;
import org.softauto.core.CallFuture;
import org.softauto.espl.ExpressionBuilder;
import org.softauto.tester.AbstractTesterImpl;
import org.softauto.tester.Listener;
import org.softauto.tester.Step;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Listeners(org.softauto.testng.JdryTestListener.class)
public class ExampleTestsNoSchema extends AbstractTesterImpl {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExampleTestsNoSchema.class);
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

    public Function ff()throws Exception{
        return new Function() {
            @Override
            public Object apply(Object o) {
               return o;
            }
        };
    }


    @Test
    public void add_listener()throws Exception{
        Listener listener =  Listener.addListener("app_example_books_BookCatalog_printBook",new Class[]{Book.class});
        new Step("app_example_books_BookStore_printBooks",new Object[]{books},new Class[]{List.class},"RPC").then(
                listener.waitTo("app_example_books_BookCatalog_printBook",ff()));
        Assert.assertTrue(listener.getResult() != null);
    }

    @Test
    public void add_listener2()throws Exception{
        Listener listener =  Listener.addListener("app_example_books_BookCatalog_printBook",new Class[]{Book.class});
        new Step("app_example_books_BookStore_printBooks",new Object[]{books},new Class[]{List.class},"RPC").then(
                listener.waitTo("app_example_books_BookCatalog_printBook"));
        Assert.assertTrue(listener.getResult() != null);
    }

    @Test
    public void add_listener1()throws Exception{
        CallFuture<Book> future = new CallFuture<>();
        Listener listener =  Listener.addListener("app_example_books_BookCatalog_printBook",new Class[]{Book.class});
        new Step("app_example_books_BookStore_printBooks",new Object[]{books},new Class[]{List.class},"RPC").then(
                listener.waitTo("app_example_books_BookCatalog_printBook",ff(),future), res ->{
                    res.result();
                });
        Assert.assertTrue(future.getResult() != null);
    }

    @Test
    public void add_listener3()throws Exception{
        CallFuture<Book> future = new CallFuture<>();
        Listener listener =  Listener.addListener("app_example_books_BookCatalog_printBook",new Class[]{Book.class});
        new Step("app_example_books_BookStore_printBooks",new Object[]{books},new Class[]{List.class},"RPC").then(
                listener.waitTo("app_example_books_BookCatalog_printBook",future), res ->{
                    res.result();
                });
        Assert.assertTrue(future.getResult() != null);
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
            CallFuture<Object> future = new CallFuture<>();
            Listener listener =  Listener.addListener("app_example_books_Book_getTitle",new Class[]{});
            new Step("app_example_books_BookStore_addAllBooks",new Object[]{books},new Class[]{Collection.class},"RPC").get_Result();
            new Step("app_example_books_BookStore_loopOverBooks",new Object[]{},new Class[]{},"RPC").then(
                  listener.waitToResult("app_example_books_Book_getTitle",ff(),future));
            Assert.assertTrue(future.getResult() != null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void listen_for_method_before_until()throws Exception{
        try {
            CallFuture<Object> future = new CallFuture<>();
            Listener listener =  Listener.addListener("app_example_books_BookCatalog_printBook",new Class[]{Book.class});
            new Step("app_example_books_BookStore_printBooks",new Object[]{books},new Class[]{List.class},"RPC").then(
                    listener.waitUntil("app_example_books_BookCatalog_printBook",ff(),new ExpressionBuilder().newExpression("book.getAuthor().equals('a1')").build(),future));
            Assert.assertTrue(((Book)((Object[])future.getResult())[0]).getTitle().equals("t1"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void listen_for_method_before_until2()throws Exception{
        try {
            CallFuture<Object> future = new CallFuture<>();
            Listener listener =  Listener.addListener("app_example_books_BookCatalog_printBook",new Class[]{Book.class});
            new Step("app_example_books_BookStore_printBooks",new Object[]{books},new Class[]{List.class},"RPC").then(
                    listener.waitUntil("app_example_books_BookCatalog_printBook",new ExpressionBuilder().newExpression("book.getAuthor().equals('a1')").build(),future));
            Assert.assertTrue(((Book)((Object[])future.getResult())[0]).getTitle().equals("t1"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void listen_for_method_before_until1()throws Exception{
        try {
            CallFuture<Object> future = new CallFuture<>();
            Listener listener =  Listener.addListener("app_example_books_BookCatalog_printBook",new Class[]{Book.class});
            BookStore bookStore =  (BookStore)new Step("app_example_books_BookStore_addAllBooks",new Object[]{books},new Class[]{Collection.class},"RPC").get_Result();
            new Step("app_example_books_BookStore_printBooks",new Object[]{},new Class[]{},"RPC").then(
                    listener.waitUntil("app_example_books_BookCatalog_printBook",ff(),new ExpressionBuilder().newExpression().setContext("book").setStatement("getTitle()").build()
                            .newExpression().setContext(bookStore).setStatement("getBookById(0).getTitle()").build()
                            .build(),future));
            Assert.assertTrue(((Book)((Object[])future.getResult())[0]).getTitle() != null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
