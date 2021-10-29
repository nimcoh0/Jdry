Feature: Book Store
  Scenario: Correct non-zero number of books found by author by list
   Given list of Book
      | title                                | author      | id |
      | The Devil in the White City          | Erik Larson | 1  |
      | The Lion, the Witch and the Wardrobe | C.S. Lewis  | 2  |
      | In the Garden of Beasts              | Erik Larson | 3  |
    When Add All Books from  bookList
    When got Books By Author with author 'Erik Larson'
    Then author books size eq 2


