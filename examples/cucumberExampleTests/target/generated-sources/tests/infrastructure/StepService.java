/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package tests.infrastructure;

@org.apache.avro.specific.AvroGenerated
public interface StepService {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"StepService\",\"namespace\":\"tests.infrastructure\",\"version\":\"1.0\",\"types\":[{\"type\":\"external\",\"name\":\"List<app.books.Book>\",\"namespace\":\"java.util\",\"fields\":[]},{\"type\":\"external\",\"name\":\"Book\",\"namespace\":\"app.books\",\"fields\":[]},{\"type\":\"external\",\"name\":\"Collection<app.books.Book>\",\"namespace\":\"java.util\",\"fields\":[]},{\"type\":\"external\",\"name\":\"Optional<app.books.Book>\",\"namespace\":\"java.util\",\"fields\":[]}],\"messages\":{\"app_books_BookCatalog_getBooks\":{\"method\":\"getBooks\",\"transceiver\":\"RPC\",\"namespace\":\"app.books.BookCatalog\",\"description\":\"^I have the following books in the store$\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.books.BookCatalog\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[],\"response\":\"java.util.List<app.books.Book>\"},\"app_books_BookStore_addBook\":{\"method\":\"addBook\",\"transceiver\":\"RPC\",\"namespace\":\"app.books.BookStore\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[{\"name\":\"book\",\"type\":\"app.books.Book\"}],\"response\":\"void\"},\"app_books_BookStore_addAllBooks\":{\"method\":\"addAllBooks\",\"transceiver\":\"RPC\",\"namespace\":\"app.books.BookStore\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[{\"name\":\"books\",\"type\":\"java.util.Collection<app.books.Book>\"}],\"response\":\"void\"},\"app_books_BookStore_booksByAuthor\":{\"method\":\"booksByAuthor\",\"transceiver\":\"RPC\",\"namespace\":\"app.books.BookStore\",\"description\":\"^I search for books by author (.+)$\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[{\"name\":\"author\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"java.util.List<app.books.Book>\"},\"app_books_BookStore_bookByTitle\":{\"method\":\"bookByTitle\",\"transceiver\":\"RPC\",\"namespace\":\"app.books.BookStore\",\"description\":\"^I search for books by title (.+)$\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[{\"name\":\"title\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"java.util.Optional<app.books.Book>\"}}}");
  /**
   */
   java.util.List<app.books.Book> app_books_BookCatalog_getBooks();
  /**
   */
   void app_books_BookStore_addBook(app.books.Book book);
  /**
   */
   void app_books_BookStore_addAllBooks(java.util.Collection<app.books.Book> books);
  /**
   */
   java.util.List<app.books.Book> app_books_BookStore_booksByAuthor(java.lang.String author);
  /**
   */
   java.util.Optional<app.books.Book> app_books_BookStore_bookByTitle(java.lang.String title);

  @SuppressWarnings("all")
  public interface Callback extends StepService {
    public static final org.apache.avro.Protocol PROTOCOL = tests.infrastructure.StepService.PROTOCOL;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_books_BookCatalog_getBooks(org.softauto.serializer.CallFuture<java.util.List<app.books.Book>> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_books_BookStore_addBook(app.books.Book book, org.softauto.serializer.CallFuture<Void> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_books_BookStore_addAllBooks(java.util.Collection<app.books.Book> books, org.softauto.serializer.CallFuture<Void> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_books_BookStore_booksByAuthor(java.lang.String author, org.softauto.serializer.CallFuture<java.util.List<app.books.Book>> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_books_BookStore_bookByTitle(java.lang.String title, org.softauto.serializer.CallFuture<java.util.Optional<app.books.Book>> callback) throws java.io.IOException;
  }
}