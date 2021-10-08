Feature: Book Store
  Scenario: Correct non-zero number of books found by author by list
    Given a list of app.cucumber.books.Book
      | title                                | author      | id |
      | The Devil in the White City          | Erik Larson | 1  |
      | The Lion, the Witch and the Wardrobe | C.S. Lewis  | 2  |
      | In the Garden of Beasts              | Erik Larson | 3  |
    Given add all books in bookList to BookStore
    When I search for books by author Erik Larson
    Then validate step result of Book_.size() == 2

