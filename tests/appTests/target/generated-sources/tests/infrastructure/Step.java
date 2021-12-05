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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.softauto.core.AsyncResult;
import org.softauto.core.Handler;
import org.softauto.core.Future;
import org.softauto.serializer.CallFuture;
import org.softauto.tester.InvocationHandler;
import org.softauto.core.IListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@org.apache.avro.specific.AvroGenerated
public class Step {

    private static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Step.class);




public static class app_example_books_BookStore_addBook {

    CallFuture< app.example.books.BookStore> future = new CallFuture<>();
    public app.example.books.BookStore get_Result() throws Exception{
            try {

               if(!future.isDone()) {
                  logger.debug("waiting to future to be done");
                  future.await();
               }
                logger.debug("successfully get_Result() ");
                return future.get();
             }catch (Exception e){
                 logger.error("fail get_Result() "+ e);
                 throw new Exception("fail get_Result() "+ e);
             }
        }

    public  app_example_books_BookStore_addBook then(Handler<AsyncResult<app.example.books.BookStore>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }


    public  app_example_books_BookStore_addBook then(IListener o)throws Exception{
        future.handleResult(future.get());

        return this;
    }

    public  App_example_books_BookStore_addBook then(IListener o,Handler<AsyncResult<app.example.books.BookStore>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }

    public  App_example_books_BookStore_addBook then(IListener o ,CallFuture<app.example.books.BookStore> future)throws Exception{
        future.handleResult(future.get());
        return this;
    }





    public App_example_books_BookStore_addBook(app.example.books.Book book    ){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_addBook");
            new InvocationHandler().invoke("app_example_books_BookStore_addBook",new Object[]{book},new Class[]{app.example.books.Book.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_addBook"+ e);
        }
    }



    public App_example_books_BookStore_addBook(app.example.books.Book book,CallFuture<app.example.books.BookStore> future){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_addBook");
            new InvocationHandler().invoke("app_example_books_BookStore_addBook",new Object[]{book},new Class[]{app.example.books.Book.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_addBook"+ e);
        }
 }


    public  App_example_books_BookStore_addBook(app.example.books.Book book,Handler<AsyncResult<app.example.books.BookStore>> resultHandler  )throws Exception{
        try {
            CallFuture<app.example.books.BookStore> future = new CallFuture<>();
            logger.debug("invoking proxy for app_example_books_BookStore_addBook");
            new InvocationHandler().invoke("app_example_books_BookStore_addBook",new Object[]{book},new Class[]{app.example.books.Book.class},future,"RPC");
            resultHandler.handle(Future.handleResult(future.get()));
       }catch (Exception e){
          logger.error("fail invoke for app_example_books_BookStore_addBook"+ e);
          resultHandler.handle(Future.handleError(e));
       }
    }




 }

public static class App_example_books_BookStore_updateBook {

    CallFuture< app.example.books.BookStore> future = new CallFuture<>();
    public app.example.books.BookStore get_Result() throws Exception{
            try {

               if(!future.isDone()) {
                  logger.debug("waiting to future to be done");
                  future.await();
               }
                logger.debug("successfully get_Result() ");
                return future.get();
             }catch (Exception e){
                 logger.error("fail get_Result() "+ e);
                 throw new Exception("fail get_Result() "+ e);
             }
        }

    public  App_example_books_BookStore_updateBook then(Handler<AsyncResult<app.example.books.BookStore>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }


    public  App_example_books_BookStore_updateBook then(IListener o)throws Exception{
        future.handleResult(future.get());

        return this;
    }

    public  App_example_books_BookStore_updateBook then(IListener o,Handler<AsyncResult<app.example.books.BookStore>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }

    public  App_example_books_BookStore_updateBook then(IListener o ,CallFuture<app.example.books.BookStore> future)throws Exception{
        future.handleResult(future.get());
        return this;
    }





    public App_example_books_BookStore_updateBook(app.example.books.Book book, java.lang.String newTitle    ){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_updateBook");
            new InvocationHandler().invoke("app_example_books_BookStore_updateBook",new Object[]{book,newTitle},new Class[]{app.example.books.Book.class,java.lang.String.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_updateBook"+ e);
        }
    }



    public App_example_books_BookStore_updateBook(app.example.books.Book book, java.lang.String newTitle,CallFuture<app.example.books.BookStore> future){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_updateBook");
            new InvocationHandler().invoke("app_example_books_BookStore_updateBook",new Object[]{book,newTitle},new Class[]{app.example.books.Book.class,java.lang.String.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_updateBook"+ e);
        }
 }


    public  App_example_books_BookStore_updateBook(app.example.books.Book book, java.lang.String newTitle,Handler<AsyncResult<app.example.books.BookStore>> resultHandler  )throws Exception{
        try {
            CallFuture<app.example.books.BookStore> future = new CallFuture<>();
            logger.debug("invoking proxy for app_example_books_BookStore_updateBook");
            new InvocationHandler().invoke("app_example_books_BookStore_updateBook",new Object[]{book,newTitle},new Class[]{app.example.books.Book.class,java.lang.String.class},future,"RPC");
            resultHandler.handle(Future.handleResult(future.get()));
       }catch (Exception e){
          logger.error("fail invoke for app_example_books_BookStore_updateBook"+ e);
          resultHandler.handle(Future.handleError(e));
       }
    }




 }

public static class App_example_books_BookStore_addAllBooks {

    CallFuture< app.example.books.BookStore> future = new CallFuture<>();
    public app.example.books.BookStore get_Result() throws Exception{
            try {

               if(!future.isDone()) {
                  logger.debug("waiting to future to be done");
                  future.await();
               }
                logger.debug("successfully get_Result() ");
                return future.get();
             }catch (Exception e){
                 logger.error("fail get_Result() "+ e);
                 throw new Exception("fail get_Result() "+ e);
             }
        }

    public  App_example_books_BookStore_addAllBooks then(Handler<AsyncResult<app.example.books.BookStore>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }


    public  App_example_books_BookStore_addAllBooks then(IListener o)throws Exception{
        future.handleResult(future.get());

        return this;
    }

    public  App_example_books_BookStore_addAllBooks then(IListener o,Handler<AsyncResult<app.example.books.BookStore>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }

    public  App_example_books_BookStore_addAllBooks then(IListener o ,CallFuture<app.example.books.BookStore> future)throws Exception{
        future.handleResult(future.get());
        return this;
    }





    public App_example_books_BookStore_addAllBooks(java.util.Collection<app.example.books.Book> books    ){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_addAllBooks");
            new InvocationHandler().invoke("app_example_books_BookStore_addAllBooks",new Object[]{books},new Class[]{java.util.Collection.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_addAllBooks"+ e);
        }
    }



    public App_example_books_BookStore_addAllBooks(java.util.Collection<app.example.books.Book> books,CallFuture<app.example.books.BookStore> future){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_addAllBooks");
            new InvocationHandler().invoke("app_example_books_BookStore_addAllBooks",new Object[]{books},new Class[]{java.util.Collection.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_addAllBooks"+ e);
        }
 }


    public  App_example_books_BookStore_addAllBooks(java.util.Collection<app.example.books.Book> books,Handler<AsyncResult<app.example.books.BookStore>> resultHandler  )throws Exception{
        try {
            CallFuture<app.example.books.BookStore> future = new CallFuture<>();
            logger.debug("invoking proxy for app_example_books_BookStore_addAllBooks");
            new InvocationHandler().invoke("app_example_books_BookStore_addAllBooks",new Object[]{books},new Class[]{java.util.Collection.class},future,"RPC");
            resultHandler.handle(Future.handleResult(future.get()));
       }catch (Exception e){
          logger.error("fail invoke for app_example_books_BookStore_addAllBooks"+ e);
          resultHandler.handle(Future.handleError(e));
       }
    }




 }

public static class App_example_books_BookStore_booksByAuthor {

    CallFuture< java.util.List<app.example.books.Book>> future = new CallFuture<>();
    public java.util.List<app.example.books.Book> get_Result() throws Exception{
            try {

               if(!future.isDone()) {
                  logger.debug("waiting to future to be done");
                  future.await();
               }
                logger.debug("successfully get_Result() ");
                return future.get();
             }catch (Exception e){
                 logger.error("fail get_Result() "+ e);
                 throw new Exception("fail get_Result() "+ e);
             }
        }

    public  App_example_books_BookStore_booksByAuthor then(Handler<AsyncResult<java.util.List<app.example.books.Book>>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }


    public  App_example_books_BookStore_booksByAuthor then(IListener o)throws Exception{
        future.handleResult(future.get());

        return this;
    }

    public  App_example_books_BookStore_booksByAuthor then(IListener o,Handler<AsyncResult<java.util.List<app.example.books.Book>>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }

    public  App_example_books_BookStore_booksByAuthor then(IListener o ,CallFuture<java.util.List<app.example.books.Book>> future)throws Exception{
        future.handleResult(future.get());
        return this;
    }





    public App_example_books_BookStore_booksByAuthor(java.lang.String author    ){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_booksByAuthor");
            new InvocationHandler().invoke("app_example_books_BookStore_booksByAuthor",new Object[]{author},new Class[]{java.lang.String.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_booksByAuthor"+ e);
        }
    }



    public App_example_books_BookStore_booksByAuthor(java.lang.String author,CallFuture<java.util.List<app.example.books.Book>> future){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_booksByAuthor");
            new InvocationHandler().invoke("app_example_books_BookStore_booksByAuthor",new Object[]{author},new Class[]{java.lang.String.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_booksByAuthor"+ e);
        }
 }


    public  App_example_books_BookStore_booksByAuthor(java.lang.String author,Handler<AsyncResult<java.util.List<app.example.books.Book>>> resultHandler  )throws Exception{
        try {
            CallFuture<java.util.List<app.example.books.Book>> future = new CallFuture<>();
            logger.debug("invoking proxy for app_example_books_BookStore_booksByAuthor");
            new InvocationHandler().invoke("app_example_books_BookStore_booksByAuthor",new Object[]{author},new Class[]{java.lang.String.class},future,"RPC");
            resultHandler.handle(Future.handleResult(future.get()));
       }catch (Exception e){
          logger.error("fail invoke for app_example_books_BookStore_booksByAuthor"+ e);
          resultHandler.handle(Future.handleError(e));
       }
    }




 }

public static class App_example_books_BookStore_bookByTitle {

    CallFuture< java.util.Optional<app.example.books.Book>> future = new CallFuture<>();
    public java.util.Optional<app.example.books.Book> get_Result() throws Exception{
            try {

               if(!future.isDone()) {
                  logger.debug("waiting to future to be done");
                  future.await();
               }
                logger.debug("successfully get_Result() ");
                return future.get();
             }catch (Exception e){
                 logger.error("fail get_Result() "+ e);
                 throw new Exception("fail get_Result() "+ e);
             }
        }

    public  App_example_books_BookStore_bookByTitle then(Handler<AsyncResult<java.util.Optional<app.example.books.Book>>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }


    public  App_example_books_BookStore_bookByTitle then(IListener o)throws Exception{
        future.handleResult(future.get());

        return this;
    }

    public  App_example_books_BookStore_bookByTitle then(IListener o,Handler<AsyncResult<java.util.Optional<app.example.books.Book>>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }

    public  App_example_books_BookStore_bookByTitle then(IListener o ,CallFuture<java.util.Optional<app.example.books.Book>> future)throws Exception{
        future.handleResult(future.get());
        return this;
    }





    public App_example_books_BookStore_bookByTitle(java.lang.String title    ){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_bookByTitle");
            new InvocationHandler().invoke("app_example_books_BookStore_bookByTitle",new Object[]{title},new Class[]{java.lang.String.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_bookByTitle"+ e);
        }
    }



    public App_example_books_BookStore_bookByTitle(java.lang.String title,CallFuture<java.util.Optional<app.example.books.Book>> future){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_bookByTitle");
            new InvocationHandler().invoke("app_example_books_BookStore_bookByTitle",new Object[]{title},new Class[]{java.lang.String.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_bookByTitle"+ e);
        }
 }


    public  App_example_books_BookStore_bookByTitle(java.lang.String title,Handler<AsyncResult<java.util.Optional<app.example.books.Book>>> resultHandler  )throws Exception{
        try {
            CallFuture<java.util.Optional<app.example.books.Book>> future = new CallFuture<>();
            logger.debug("invoking proxy for app_example_books_BookStore_bookByTitle");
            new InvocationHandler().invoke("app_example_books_BookStore_bookByTitle",new Object[]{title},new Class[]{java.lang.String.class},future,"RPC");
            resultHandler.handle(Future.handleResult(future.get()));
       }catch (Exception e){
          logger.error("fail invoke for app_example_books_BookStore_bookByTitle"+ e);
          resultHandler.handle(Future.handleError(e));
       }
    }




 }

public static class App_example_books_BookStore_loopOverBooks {

    CallFuture<java.lang.Void> future = new CallFuture<>();
    public void get_Result() throws Exception{
            try {
                 if(!future.isDone()) {
                    logger.debug("waiting to future to be done");
                    future.await();
                 }
                 logger.debug("successfully get_Result() ");
                 future.get();
             }catch (Exception e){
                logger.error("fail get_Result() "+ e);
                throw new Exception("fail get_Result() "+ e);
             }
        }

    public  App_example_books_BookStore_loopOverBooks then(Handler<AsyncResult<java.lang.Void>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }


    public  App_example_books_BookStore_loopOverBooks then(IListener o)throws Exception{
        return this;
    }

    public  App_example_books_BookStore_loopOverBooks then(IListener o,Handler<AsyncResult<java.lang.Void>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }

    public  App_example_books_BookStore_loopOverBooks then(IListener o ,CallFuture<java.lang.Void> future)throws Exception{
        future.handleResult(future.get());
        return this;
    }





    public App_example_books_BookStore_loopOverBooks(    ){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_loopOverBooks");
            new InvocationHandler().invoke("app_example_books_BookStore_loopOverBooks",new Object[]{},new Class[]{},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_loopOverBooks"+ e);
        }
    }



    public App_example_books_BookStore_loopOverBooks(CallFuture<java.lang.Void> future){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_loopOverBooks");
            new InvocationHandler().invoke("app_example_books_BookStore_loopOverBooks",new Object[]{},new Class[]{},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_loopOverBooks"+ e);
        }
 }


    public  App_example_books_BookStore_loopOverBooks(Handler<AsyncResult<java.lang.Void>> resultHandler  )throws Exception{
        try {
            CallFuture<java.lang.Void> future = new CallFuture<>();
            logger.debug("invoking proxy for app_example_books_BookStore_loopOverBooks");
            new InvocationHandler().invoke("app_example_books_BookStore_loopOverBooks",new Object[]{},new Class[]{},future,"RPC");
            resultHandler.handle(Future.handleResult(future.get()));
       }catch (Exception e){
          logger.error("fail invoke for app_example_books_BookStore_loopOverBooks"+ e);
          resultHandler.handle(Future.handleError(e));
       }
    }




 }

public static class App_example_books_BookStore_printBooks {

    CallFuture<java.lang.Void> future = new CallFuture<>();
    public void get_Result() throws Exception{
            try {
                 if(!future.isDone()) {
                    logger.debug("waiting to future to be done");
                    future.await();
                 }
                 logger.debug("successfully get_Result() ");
                 future.get();
             }catch (Exception e){
                logger.error("fail get_Result() "+ e);
                throw new Exception("fail get_Result() "+ e);
             }
        }

    public  App_example_books_BookStore_printBooks then(Handler<AsyncResult<java.lang.Void>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }


    public  App_example_books_BookStore_printBooks then(IListener o)throws Exception{
        return this;
    }

    public  App_example_books_BookStore_printBooks then(IListener o,Handler<AsyncResult<java.lang.Void>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }

    public  App_example_books_BookStore_printBooks then(IListener o ,CallFuture<java.lang.Void> future)throws Exception{
        future.handleResult(future.get());
        return this;
    }





    public App_example_books_BookStore_printBooks(java.util.List<app.example.books.Book> books    ){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_printBooks");
            new InvocationHandler().invoke("app_example_books_BookStore_printBooks",new Object[]{books},new Class[]{java.util.List.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_printBooks"+ e);
        }
    }



    public App_example_books_BookStore_printBooks(java.util.List<app.example.books.Book> books,CallFuture<java.lang.Void> future){
        try {
            logger.debug("invoking proxy for app_example_books_BookStore_printBooks");
            new InvocationHandler().invoke("app_example_books_BookStore_printBooks",new Object[]{books},new Class[]{java.util.List.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_BookStore_printBooks"+ e);
        }
 }


    public  App_example_books_BookStore_printBooks(java.util.List<app.example.books.Book> books,Handler<AsyncResult<java.lang.Void>> resultHandler  )throws Exception{
        try {
            CallFuture<java.lang.Void> future = new CallFuture<>();
            logger.debug("invoking proxy for app_example_books_BookStore_printBooks");
            new InvocationHandler().invoke("app_example_books_BookStore_printBooks",new Object[]{books},new Class[]{java.util.List.class},future,"RPC");
            resultHandler.handle(Future.handleResult(future.get()));
       }catch (Exception e){
          logger.error("fail invoke for app_example_books_BookStore_printBooks"+ e);
          resultHandler.handle(Future.handleError(e));
       }
    }




 }

public static class App_example_books_Book_Book {

    CallFuture< app.example.books.Book> future = new CallFuture<>();
    public app.example.books.Book get_Result() throws Exception{
            try {

               if(!future.isDone()) {
                  logger.debug("waiting to future to be done");
                  future.await();
               }
                logger.debug("successfully get_Result() ");
                return future.get();
             }catch (Exception e){
                 logger.error("fail get_Result() "+ e);
                 throw new Exception("fail get_Result() "+ e);
             }
        }

    public  App_example_books_Book_Book then(Handler<AsyncResult<app.example.books.Book>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }


    public  App_example_books_Book_Book then(IListener o)throws Exception{
        future.handleResult(future.get());

        return this;
    }

    public  App_example_books_Book_Book then(IListener o,Handler<AsyncResult<app.example.books.Book>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }

    public  App_example_books_Book_Book then(IListener o ,CallFuture<app.example.books.Book> future)throws Exception{
        future.handleResult(future.get());
        return this;
    }





    public App_example_books_Book_Book(java.lang.String title, java.lang.String author    ){
        try {
            logger.debug("invoking proxy for app_example_books_Book_Book");
            new InvocationHandler().invoke("app_example_books_Book_Book",new Object[]{title,author},new Class[]{java.lang.String.class,java.lang.String.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_Book_Book"+ e);
        }
    }



    public App_example_books_Book_Book(java.lang.String title, java.lang.String author,CallFuture<app.example.books.Book> future){
        try {
            logger.debug("invoking proxy for app_example_books_Book_Book");
            new InvocationHandler().invoke("app_example_books_Book_Book",new Object[]{title,author},new Class[]{java.lang.String.class,java.lang.String.class},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_Book_Book"+ e);
        }
 }


    public  App_example_books_Book_Book(java.lang.String title, java.lang.String author,Handler<AsyncResult<app.example.books.Book>> resultHandler  )throws Exception{
        try {
            CallFuture<app.example.books.Book> future = new CallFuture<>();
            logger.debug("invoking proxy for app_example_books_Book_Book");
            new InvocationHandler().invoke("app_example_books_Book_Book",new Object[]{title,author},new Class[]{java.lang.String.class,java.lang.String.class},future,"RPC");
            resultHandler.handle(Future.handleResult(future.get()));
       }catch (Exception e){
          logger.error("fail invoke for app_example_books_Book_Book"+ e);
          resultHandler.handle(Future.handleError(e));
       }
    }




 }

public static class App_example_books_Book_hello {

    CallFuture< java.lang.String> future = new CallFuture<>();
    public java.lang.String get_Result() throws Exception{
            try {

               if(!future.isDone()) {
                  logger.debug("waiting to future to be done");
                  future.await();
               }
                logger.debug("successfully get_Result() ");
                return future.get();
             }catch (Exception e){
                 logger.error("fail get_Result() "+ e);
                 throw new Exception("fail get_Result() "+ e);
             }
        }

    public  App_example_books_Book_hello then(Handler<AsyncResult<java.lang.String>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }


    public  App_example_books_Book_hello then(IListener o)throws Exception{
        future.handleResult(future.get());

        return this;
    }

    public  App_example_books_Book_hello then(IListener o,Handler<AsyncResult<java.lang.String>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }

    public  App_example_books_Book_hello then(IListener o ,CallFuture<java.lang.String> future)throws Exception{
        future.handleResult(future.get());
        return this;
    }





    public App_example_books_Book_hello(    ){
        try {
            logger.debug("invoking proxy for app_example_books_Book_hello");
            new InvocationHandler().invoke("app_example_books_Book_hello",new Object[]{},new Class[]{},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_Book_hello"+ e);
        }
    }



    public App_example_books_Book_hello(CallFuture<java.lang.String> future){
        try {
            logger.debug("invoking proxy for app_example_books_Book_hello");
            new InvocationHandler().invoke("app_example_books_Book_hello",new Object[]{},new Class[]{},future,"RPC");
        }catch (Exception e){
             logger.error("fail invoke for app_example_books_Book_hello"+ e);
        }
 }


    public  App_example_books_Book_hello(Handler<AsyncResult<java.lang.String>> resultHandler  )throws Exception{
        try {
            CallFuture<java.lang.String> future = new CallFuture<>();
            logger.debug("invoking proxy for app_example_books_Book_hello");
            new InvocationHandler().invoke("app_example_books_Book_hello",new Object[]{},new Class[]{},future,"RPC");
            resultHandler.handle(Future.handleResult(future.get()));
       }catch (Exception e){
          logger.error("fail invoke for app_example_books_Book_hello"+ e);
          resultHandler.handle(Future.handleError(e));
       }
    }




 }
}