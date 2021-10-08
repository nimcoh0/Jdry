/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package tests.infrastructure;

@org.apache.avro.specific.AvroGenerated
public interface ListenerService {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"ListenerService\",\"namespace\":\"tests.infrastructure\",\"version\":\"1.0\",\"types\":[{\"type\":\"external\",\"name\":\"Book\",\"namespace\":\"app.books\",\"fields\":[]},{\"type\":\"external\",\"name\":\"Object[]\",\"namespace\":\"java.lang\",\"fields\":[]}],\"messages\":{\"app_books_BookCatalog_addBook_result\":{\"method\":\"addBook_result\",\"transceiver\":\"RPC\",\"namespace\":\"app.books.BookCatalog\",\"request\":[{\"name\":\"book\",\"type\":\"app.books.Book\"}],\"response\":\"null\"},\"app_books_BookCatalog_addBook\":{\"method\":\"addBook\",\"transceiver\":\"RPC\",\"namespace\":\"app.books.BookCatalog\",\"request\":[{\"name\":\"book\",\"type\":\"app.books.Book\"}],\"response\":\"java.lang.Object[]\"}}}");
  /**
   */
   void app_books_BookCatalog_addBook_result(app.books.Book book);
  /**
   */
   java.lang.Object[] app_books_BookCatalog_addBook(app.books.Book book);

  @SuppressWarnings("all")
  public interface Callback extends ListenerService {
    public static final org.apache.avro.Protocol PROTOCOL = tests.infrastructure.ListenerService.PROTOCOL;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_books_BookCatalog_addBook_result(app.books.Book book, org.softauto.serializer.CallFuture<Void> callback) throws java.io.IOException;
    /**
     * @throws java.io.IOException The async call could not be completed.
     */
    void app_books_BookCatalog_addBook(app.books.Book book, org.softauto.serializer.CallFuture<java.lang.Object[]> callback) throws java.io.IOException;
  }
}