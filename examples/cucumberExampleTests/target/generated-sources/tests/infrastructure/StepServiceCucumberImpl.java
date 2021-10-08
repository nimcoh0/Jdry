/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package tests.infrastructure;
import java.util.function.Function;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.cucumber.java.en.Given;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.ParameterType;
import org.softauto.serializer.CallFuture;
import org.softauto.core.Utils;
import java.lang.reflect.Field;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import io.cucumber.datatable.DataTable;
import java.util.Map;
import java.util.*;

@org.apache.avro.specific.AvroGenerated
public class StepServiceCucumberImpl extends CucumberAbstractTesterImpl {
    private static Logger logger = LogManager.getLogger(StepServiceCucumberImpl.class);

    public StepServiceCucumberImpl(){
        registerGlueClass(this);
    }





@ParameterType(".*")
public String exp (String exp) throws Exception{
    return exp;
}

/*
@ParameterType(".*")
public Object future (String future) throws Exception{
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(future);
      if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
      }
      Expression exp2 = parser.parseExpression(getFuture());
      return exp2.getValue(itemContext);
}
*/
@ParameterType(".*")
public String operator (String operator)throws Exception{
       return operator;
}
@ParameterType(".*")
public Object expected (String expected)throws Exception{
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(expected);
       if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
       }
      Expression exp2 = parser.parseExpression(expected);
      Object result = exp2.getValue(itemContext);

return result;
}

@Then("validate step result of {exp} {operator} {expected}")
public void validate(String exp ,String operator,Object expected)throws Exception{
    //Object o =  evulExp(expected);
    boolean result = getResult(exp + " "+ operator + " "+ expected);
    Assert.assertTrue(result);
}



  /**
   */

public java.util.List<app.books.Book> Book_;
@Given("^I have the following books in the store$")
public java.util.List<app.books.Book> app_books_BookCatalog_getBooks(){
    Book_ = tests.app_books_BookCatalog_getBooks();
    return Book_;
}





@ParameterType(".*")
public CallFuture future0 (String future0)throws Exception{
   return new CallFuture<java.util.List<app.books.Book>>();
}
java.util.List<app.books.Book> Book_0;


CallFuture<java.util.List<app.books.Book>> future0;
java.util.List<app.books.Book> async_getBooks_result;


public java.util.List<app.books.Book> getFuture0() throws ExecutionException, InterruptedException{
        async_getBooks_result =  future0.get();
        return async_getBooks_result;
}

@Given("a ^I have the following books in the store$ #async")
public void async_app_books_BookCatalog_getBooks()throws Exception {
future0 = new CallFuture<>();
asyncTests.app_books_BookCatalog_getBooks( future0);
}
  /**
   */


@ParameterType(".*")
public app.books.Book book (String book)throws Exception{
if(Utils.isJson(book)){
      return new ObjectMapper().readValue(book, app.books.Book.class);
}
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(book);
      if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
       }
      Expression exp2 = parser.parseExpression(book);
      app.books.Book result = (app.books.Book) exp2.getValue(itemContext,app.books.Book.class);

return result;
}
//public app.books.Book book;

@Given("addBook  with book {book}")
public  void app_books_BookStore_addBook(app.books.Book book){
tests.app_books_BookStore_addBook(book);
}




public List<app.books.Book> bookList;
     @Given("a list of app.books.Book")
     public List<app.books.Book> create_app_books_Book (DataTable table){
     try{
        List<Map<String, String>> map = table.asMaps(String.class, String.class);
        String json = new ObjectMapper().writeValueAsString(map);
        app.books.Book[] res = new ObjectMapper().readValue(json, app.books.Book[].class);
        bookList = Arrays.asList(res);
     }catch (Exception e){
        e.printStackTrace();
     }
     return   bookList;
     }



@ParameterType(".*")
public CallFuture future1 (String future1)throws Exception{
   return new CallFuture<Void>();
}



@ParameterType(".*")
public app.books.Book book0 (String book0)throws Exception{
if(Utils.isJson(book0)){
      return new ObjectMapper().readValue(book0, app.books.Book.class);
}
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(book0);
      if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
      }
      Expression exp2 = parser.parseExpression(book0);
      app.books.Book result = (app.books.Book) exp2.getValue(itemContext,app.books.Book.class);

return result;
}
//public app.books.Book book0;

CallFuture<Void> future1;
Void async_addBook0_result;


public Void getFuture1() throws ExecutionException, InterruptedException{
        async_addBook0_result =  future1.get();
        return async_addBook0_result;
}

@Given("a addBook1 with book0 {book0} #async")
public  void async_app_books_BookStore_addBook(app.books.Book book0)throws Exception {
future1 = new CallFuture<>();
asyncTests.app_books_BookStore_addBook(book0, future1);
}
  /**
   */


@ParameterType(".*")
public java.util.Collection<app.books.Book> books (String books)throws Exception{
if(Utils.isJson(books)){
      return new ObjectMapper().readValue(books, java.util.Collection.class);
}
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(books);
      if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
       }
      Expression exp2 = parser.parseExpression(books);
      java.util.Collection<app.books.Book> result = (java.util.Collection<app.books.Book>) exp2.getValue(itemContext,java.util.Collection.class);

return result;
}
//public java.util.Collection<app.books.Book> books;

@Given("addAllBooks  with books {books}")
public  void app_books_BookStore_addAllBooks(java.util.Collection<app.books.Book> books){
tests.app_books_BookStore_addAllBooks(books);
}





@ParameterType(".*")
public CallFuture future2 (String future2)throws Exception{
   return new CallFuture<Void>();
}



@ParameterType(".*")
public java.util.Collection<app.books.Book> books0 (String books0)throws Exception{
if(Utils.isJson(books0)){
      return new ObjectMapper().readValue(books0, java.util.Collection.class);
}
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(books0);
      if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
      }
      Expression exp2 = parser.parseExpression(books0);
      java.util.Collection<app.books.Book> result = (java.util.Collection<app.books.Book>) exp2.getValue(itemContext,app.books.Book.class);

return result;
}
//public java.util.Collection<app.books.Book> books0;

CallFuture<Void> future2;
Void async_addAllBooks0_result;


public Void getFuture2() throws ExecutionException, InterruptedException{
        async_addAllBooks0_result =  future2.get();
        return async_addAllBooks0_result;
}

@Given("a addAllBooks1 with books0 {books0} #async")
public  void async_app_books_BookStore_addAllBooks(java.util.Collection<app.books.Book> books0)throws Exception {
future2 = new CallFuture<>();
asyncTests.app_books_BookStore_addAllBooks(books0, future2);
}
  /**
   */


@ParameterType(".*")
public java.lang.String author (String author)throws Exception{
if(Utils.isJson(author)){
      return new ObjectMapper().readValue(author, java.lang.String.class);
}
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(author);
      if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
       }
      Expression exp2 = parser.parseExpression(author);
      java.lang.String result = (java.lang.String) exp2.getValue(itemContext,java.lang.String.class);

return result;
}
//public java.lang.String author;

public java.util.List<app.books.Book> Book_1;
@Given("^I search for books by author (.+)$")
public  java.util.List<app.books.Book> app_books_BookStore_booksByAuthor(java.lang.String author){
    Book_1 = tests.app_books_BookStore_booksByAuthor(author);
    return Book_1;
}





@ParameterType(".*")
public CallFuture future3 (String future3)throws Exception{
   return new CallFuture<java.util.List<app.books.Book>>();
}
java.util.List<app.books.Book> Book_2;



@ParameterType(".*")
public java.lang.String author0 (String author0)throws Exception{
if(Utils.isJson(author0)){
      return new ObjectMapper().readValue(author0, java.lang.String.class);
}
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(author0);
      if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
      }
      Expression exp2 = parser.parseExpression(author0);
      java.lang.String result = (java.lang.String) exp2.getValue(itemContext,java.lang.String.class);

return result;
}
//public java.lang.String author0;

CallFuture<java.util.List<app.books.Book>> future3;
java.util.List<app.books.Book> async_booksByAuthor_result;


public java.util.List<app.books.Book> getFuture3() throws ExecutionException, InterruptedException{
        async_booksByAuthor_result =  future3.get();
        return async_booksByAuthor_result;
}

@Given("a ^I search for books by author (.+)$ #async")
public  void async_app_books_BookStore_booksByAuthor(java.lang.String author0)throws Exception {
future3 = new CallFuture<>();
asyncTests.app_books_BookStore_booksByAuthor(author0, future3);
}
  /**
   */


@ParameterType(".*")
public java.lang.String title (String title)throws Exception{
if(Utils.isJson(title)){
      return new ObjectMapper().readValue(title, java.lang.String.class);
}
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(title);
      if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
       }
      Expression exp2 = parser.parseExpression(title);
      java.lang.String result = (java.lang.String) exp2.getValue(itemContext,java.lang.String.class);

return result;
}
//public java.lang.String title;

public java.util.Optional<app.books.Book> Book_3;
@Given("^I search for books by title (.+)$")
public  java.util.Optional<app.books.Book> app_books_BookStore_bookByTitle(java.lang.String title){
    Book_3 = tests.app_books_BookStore_bookByTitle(title);
    return Book_3;
}





@ParameterType(".*")
public CallFuture future4 (String future4)throws Exception{
   return new CallFuture<java.util.Optional<app.books.Book>>();
}
java.util.Optional<app.books.Book> Book_4;



@ParameterType(".*")
public java.lang.String title0 (String title0)throws Exception{
if(Utils.isJson(title0)){
      return new ObjectMapper().readValue(title0, java.lang.String.class);
}
      ExpressionParser parser = new SpelExpressionParser();
      StandardEvaluationContext itemContext = getContext(title0);
      if(itemContext == null){
          itemContext = new StandardEvaluationContext(this);
      }
      Expression exp2 = parser.parseExpression(title0);
      java.lang.String result = (java.lang.String) exp2.getValue(itemContext,java.lang.String.class);

return result;
}
//public java.lang.String title0;

CallFuture<java.util.Optional<app.books.Book>> future4;
java.util.Optional<app.books.Book> async_bookByTitle_result;


public java.util.Optional<app.books.Book> getFuture4() throws ExecutionException, InterruptedException{
        async_bookByTitle_result =  future4.get();
        return async_bookByTitle_result;
}

@Given("a ^I search for books by title (.+)$ #async")
public  void async_app_books_BookStore_bookByTitle(java.lang.String title0)throws Exception {
future4 = new CallFuture<>();
asyncTests.app_books_BookStore_bookByTitle(title0, future4);
}
}