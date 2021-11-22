/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package tests.infrastructure;

@org.apache.avro.specific.AvroGenerated
public interface StepService {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"StepService\",\"namespace\":\"tests.infrastructure\",\"version\":\"1.0\",\"types\":[{\"type\":\"external\",\"name\":\"BookStore\",\"namespace\":\"app.example.books\",\"fields\":[]},{\"type\":\"external\",\"name\":\"Book\",\"namespace\":\"app.example.books\",\"fields\":[]},{\"type\":\"external\",\"name\":\"Collection<app.example.books.Book>\",\"namespace\":\"java.util\",\"fields\":[]},{\"type\":\"external\",\"name\":\"List<app.example.books.Book>\",\"namespace\":\"java.util\",\"fields\":[]},{\"type\":\"external\",\"name\":\"Optional<app.example.books.Book>\",\"namespace\":\"java.util\",\"fields\":[]}],\"messages\":{\"app_example_books_BookStore_addBook\":{\"method\":\"addBook\",\"transceiver\":\"RPC\",\"namespace\":\"app.example.books.BookStore\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.example.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[{\"name\":\"book\",\"type\":\"app.example.books.Book\"}],\"response\":\"app.example.books.BookStore\"},\"app_example_books_BookStore_updateBook\":{\"method\":\"updateBook\",\"transceiver\":\"RPC\",\"namespace\":\"app.example.books.BookStore\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.example.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[{\"name\":\"book\",\"type\":\"app.example.books.Book\"},{\"name\":\"newTitle\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"app.example.books.BookStore\"},\"app_example_books_BookStore_addAllBooks\":{\"method\":\"addAllBooks\",\"transceiver\":\"RPC\",\"namespace\":\"app.example.books.BookStore\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.example.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[{\"name\":\"books\",\"type\":\"java.util.Collection<app.example.books.Book>\"}],\"response\":\"app.example.books.BookStore\"},\"app_example_books_BookStore_booksByAuthor\":{\"method\":\"booksByAuthor\",\"transceiver\":\"RPC\",\"namespace\":\"app.example.books.BookStore\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.example.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[{\"name\":\"author\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"java.util.List<app.example.books.Book>\"},\"app_example_books_BookStore_bookByTitle\":{\"method\":\"bookByTitle\",\"transceiver\":\"RPC\",\"namespace\":\"app.example.books.BookStore\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.example.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[{\"name\":\"title\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"response\":\"java.util.Optional<app.example.books.Book>\"},\"app_example_books_BookStore_loopOverBooks\":{\"method\":\"loopOverBooks\",\"transceiver\":\"RPC\",\"namespace\":\"app.example.books.BookStore\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.example.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[],\"response\":\"void\"},\"app_example_books_BookStore_printBooks\":{\"method\":\"printBooks\",\"transceiver\":\"RPC\",\"namespace\":\"app.example.books.BookStore\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.example.books.BookStore\",\"initialize\":\"INITIALIZE_NO_PARAM\"},\"request\":[],\"response\":\"void\"},\"app_example_books_Book_Book\":{\"method\":\"Book\",\"transceiver\":\"RPC\",\"namespace\":\"app.example.books.Book\",\"type\":\"constructor\",\"class\":{\"fullClassName\":\"app.example.books.Book\",\"initialize\":\"INITIALIZE\"},\"request\":[{\"name\":\"title\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"blabla\"},{\"name\":\"author\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"Nim\"}],\"response\":\"app.example.books.Book\"},\"app_example_books_Book_hello\":{\"method\":\"hello\",\"transceiver\":\"RPC\",\"namespace\":\"app.example.books.Book\",\"type\":\"method\",\"class\":{\"fullClassName\":\"app.example.books.Book\",\"initialize\":\"INITIALIZE\"},\"request\":[],\"response\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}}}");
  /**
   */
   app.example.books.BookStore app_example_books_BookStore_addBook(app.example.books.Book book);
  /**
   */
   app.example.books.BookStore app_example_books_BookStore_updateBook(app.example.books.Book book, java.lang.String newTitle);
  /**
   */
   app.example.books.BookStore app_example_books_BookStore_addAllBooks(java.util.Collection<app.example.books.Book> books);
  /**
   */
   java.util.List<app.example.books.Book> app_example_books_BookStore_booksByAuthor(java.lang.String author);
  /**
   */
   java.util.Optional<app.example.books.Book> app_example_books_BookStore_bookByTitle(java.lang.String title);
  /**
   */
   void app_example_books_BookStore_loopOverBooks();
  /**
   */
   void app_example_books_BookStore_printBooks();
  /**
   */
   app.example.books.Book app_example_books_Book_Book(java.lang.String title, java.lang.String author);
  /**
   */
   java.lang.String app_example_books_Book_hello();

  @SuppressWarnings("all")
  public interface Callback extends StepService {
    public static final org.apache.avro.Protocol PROTOCOL = tests.infrastructure.StepService.PROTOCOL;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_example_books_BookStore_addBook(app.example.books.Book book, org.softauto.serializer.CallFuture<app.example.books.BookStore> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_example_books_BookStore_updateBook(app.example.books.Book book, java.lang.String newTitle, org.softauto.serializer.CallFuture<app.example.books.BookStore> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_example_books_BookStore_addAllBooks(java.util.Collection<app.example.books.Book> books, org.softauto.serializer.CallFuture<app.example.books.BookStore> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_example_books_BookStore_booksByAuthor(java.lang.String author, org.softauto.serializer.CallFuture<java.util.List<app.example.books.Book>> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_example_books_BookStore_bookByTitle(java.lang.String title, org.softauto.serializer.CallFuture<java.util.Optional<app.example.books.Book>> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_example_books_BookStore_loopOverBooks(org.softauto.serializer.CallFuture<Void> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_example_books_BookStore_printBooks(org.softauto.serializer.CallFuture<Void> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_example_books_Book_Book(java.lang.String title, java.lang.String author, org.softauto.serializer.CallFuture<app.example.books.Book> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_example_books_Book_hello(org.softauto.serializer.CallFuture<String> callback) throws java.io.IOException;
  }
}