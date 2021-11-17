package tests;


import app.example.books.Book;
import app.example.books.BookStore;
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

    private List<Book> books = null;


    @BeforeSuite
    public void setup()throws Exception{
        books = new ArrayList<>();
        Book book1 = new Book();
        Book book2 = new Book();
        book1.setAuthor("a1");
        book1.setTitle("t1");
        book2.setAuthor("a2");
        book2.setTitle("t2");
        books.add(book1);
        books.add(book2);

    }

    @Test
    public void add_All_Books(){
        BookStore bookStore =  new Step.App_example_books_BookStore_addAllBooks(books).get_Result();
        Assert.assertTrue(bookStore.getBooks().size() == 2);
    }

    @Test
    public void listenToTitle(){
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

}
