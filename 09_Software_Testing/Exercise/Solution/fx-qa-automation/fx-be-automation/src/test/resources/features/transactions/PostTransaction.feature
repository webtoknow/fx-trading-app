Feature: Post Transactions
  As a app user
  I want to verify that the API can post a transaction
  So that the system is consistent

  Scenario: Post and retrieve transactions
    Given the website is running
    When I call transactions endpoint
    Then the response status code should be 200
    And the list of transactions should include the previous transaction