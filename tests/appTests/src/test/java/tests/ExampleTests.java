package tests;


import app.example.books.Book;
import app.example.books.BookStore;
import org.softauto.espl.ExpressionBuilder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.infrastructure.AbstractTesterImpl;
import tests.infrastructure.Listener;
import tests.infrastructure.Step;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;

@Listeners(org.softauto.testng.JdryTestListener.class)
public class ExampleTests extends AbstractTesterImpl {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExampleTests.class);
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

    @AfterMethod
    public void finish(){
        logger.info("end test");
    }


    @Test
    public void call_method(){
        BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
        Assert.assertTrue(bookStore.getBooks().size() == 2);
    }

    @Test
    public void listen_for_method_after(){
        try {
            BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
            new Step.App_example_books_BookStore_loopOverBooks().then(
                    Listener.app_example_books_Book_getTitle.waitToResult().get_Result(),res ->{
                        Assert.assertTrue(res.result().equals("t1"));
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void listen_for_method_before(){
        try {
            BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
            new Step.App_example_books_BookStore_printBooks().then(
                    Listener.app_example_books_BookCatalog_printBook.waitTo().getBook().getTitle(),res ->{
                        if(res.result().toString().equals("t1")){
                            System.out.println("found t1");
                        }else {
                            System.out.println("not found t1, but found "+ res.result());
                        }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void listen_for_method_before_until(){
        try {
            Listener.setTimeOut(4);
            BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
            new Step.App_example_books_BookStore_printBooks().then(
                    Listener.app_example_books_BookCatalog_printBook.waitUntil(new ExpressionBuilder().newExpression("book.getAuthor().equals('a1')").build().build()).getBook().getTitle(), res ->{
                        Assert.assertTrue(res.result().equals("t1"));
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void listen_for_method_before_until1(){
        try {
            Listener.setTimeOut(4);
            BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
            new Step.App_example_books_BookStore_printBooks().then(
                    Listener.app_example_books_BookCatalog_printBook.waitUntil(new ExpressionBuilder().newExpression().setContext("book").setStatement("getId()").build()
                                                                                                .setOperator("==")
                                                                                                .newExpression().setContext(bookStore).setStatement("bookByTitle('t1').get().getId()").build()
                                                                                                .build()

                    ).getBook(),res ->{
                        Assert.assertTrue(res.result().equals("t1"));
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

   @Test
   public void auto_initialize_class(){
       Listener.setTimeOut(4);
       String msg = new Step.App_example_books_Book_hello().get_Result();
       Assert.assertTrue(msg.equals("hello"));
   }

    @Test
    public void manually_initialize_class(){
        Listener.setTimeOut(4);
        new Step.App_example_books_Book_Book("blabla","nim").then(
                new Step.App_example_books_Book_hello().get_Result(),res ->{
                    Assert.assertTrue(res.result().equals("hello"));
                }
        );
    }

    @Test
    public void check_session(){
        Listener.setTimeOut(4);
        new Step.App_example_books_BookStore_addAllBooks(books);
        Book book = new Step.App_example_books_BookStore_bookByTitle("t1").get_Result().get();
        Assert.assertTrue(book.getTitle().equals("t1"));
    }

}
