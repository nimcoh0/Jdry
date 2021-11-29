package tests;


import app.example.books.Book;
import app.example.books.BookStore;
import org.softauto.espl.ExpressionBuilder;
import org.softauto.serializer.CallFuture;
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

    /**
     * the tests invoke a grpc call and get the result
     * @throws Exception
     */
    @Test
    public void call_method()throws Exception{
        BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
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
            CallFuture<String> future = new CallFuture<>();
            BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
            new Step.App_example_books_BookStore_loopOverBooks().then(
                    Listener.app_example_books_Book_getTitle.waitToResult(future));
            Assert.assertTrue(future.getResult() != null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  the tests first load a data to the SUT (addAllBooks)
     *  then invoke another grpc call (booksByAuthor) and verify the result
     * @throws Exception
     */
    @Test
    public void get_step_result()throws Exception{
        try {
            BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
            new Step.App_example_books_BookStore_booksByAuthor("a1").then(res ->{
                ((Book)res.result().get(0)).getTitle().equals("t1");
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     *  the tests call a grpc method (printBooks) then wait to a method (printBook)
     *  to be invoke as part of the printBooks process . then print the book parameter before the
     *  printBook is execute
     *
     * @throws Exception
     */
    @Test
    public void listen_for_method_before_first()throws Exception{
        try {
            //BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
            //CallFuture<Object[]> future = new CallFuture<>();
            new Step.App_example_books_BookStore_printBooks(books).then(
                Listener.app_example_books_BookCatalog_printBook.waitTo().getBook(res->{
                        System.out.println(res.result().getId());
                    }));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * the test call a grpc method on the SUT (printBooks) then wait for printBook to be invoke as part of the
     * printBooks process and if the expression evaluate to true verify the book Title
     * @throws Exception
     */
    @Test
    public void listen_for_method_before_until()throws Exception{
        try {
            CallFuture<Object[]> future = new CallFuture<>();
            //BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
            new Step.App_example_books_BookStore_printBooks(books).then(
                  Listener.app_example_books_BookCatalog_printBook.waitUntil(new ExpressionBuilder().newExpression("book.getAuthor().equals('a1')").build(),future));
            Assert.assertTrue(((Book)future.getResult()[0]).getTitle().equals("t1"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * the tests first load a data to the SUT (addAllBooks)
     * then call a grpc method on the SUT (printBooks) and wait for printBook to be invoke as part of the
     * printBooks process and if the expression evaluate to true verify the book Title
     * @throws Exception
     */
    @Test
    public void listen_for_method_before_until1()throws Exception{
        try {
            CallFuture<Object[]> future = new CallFuture<>();
            BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
            new Step.App_example_books_BookStore_printBooks(books).then(
                    Listener.app_example_books_BookCatalog_printBook.waitUntil(new ExpressionBuilder().newExpression().setContext("book").setStatement("getId()").build()
                                                                                                .setOperator("==")
                                                                                                .newExpression().setContext(bookStore).setStatement("bookByTitle('t1').get().getId()").build()
                                                                                                .build(),future));
            Assert.assertTrue(((Book)future.getResult()[0]).getTitle().equals("t1"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * the tests first load a data to the SUT (addAllBooks)
     * then call a grpc method on the SUT (printBooks) and wait for printBook to be invoke as part of the
     * printBooks process and if the expression evaluate to true verify the book Title
     * @throws Exception
     */
    @Test
    public void listen_for_method_before_until2()throws Exception{
        try {
            CallFuture<Object[]> future = new CallFuture<>();
            BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
            new Step.App_example_books_BookStore_printBooks(books).then(
                    Listener.app_example_books_BookCatalog_printBook.waitUntil(new ExpressionBuilder().newExpression("book.getAuthor().equals('a1')").build()
                            .setOperator(" and ")
                            .newExpression().setContext(bookStore).setStatement("bookByTitle('t1').get().getAuthor().equals('a1')").build()
                            .build(),future));
            Assert.assertTrue(((Book)future.getResult()[0]).getTitle().equals("t1"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * this tests will use the default values in the schema (set by @defaultValue in the Book constructor)
     * to automatically initialize the Book class if it's not found in the JVM . if found it will use the JVM Book first instance
      * @throws Exception
     */
   @Test
   public void auto_initialize_class()throws Exception{
       String msg = new Step.App_example_books_Book_hello().get_Result();
       logger.debug("msg "+ msg);
       Assert.assertTrue(msg.equals("hello"));
   }

    /**
     * this test will initialize the Book class using the tests constructor parameters
     * @throws Exception
     */
    @Test
    public void manually_initialize_class()throws Exception{
       Book book = new Step.App_example_books_Book_Book("blabla","nim").get_Result();
                new Step.App_example_books_Book_hello(res ->{
                    Assert.assertTrue(res.result().equals("hello"));
                }
        );
    }

    /**
     * this tests will create Book instance in the JVM at the first step and use it the second step
     * @throws Exception
     */
    @Test
    public void check_session()throws Exception{
        new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
        Book book = new Step.App_example_books_BookStore_bookByTitle("t1").get_Result().get();
        Assert.assertTrue(book.getTitle().equals("t1"));
    }

}
