Feature: Get currencies
  As a app user
  I want to verify that the API returns the correct list of currencies
  So that the system is consistent

  Scenario: Retrieve currencies and verify list
    Given the website is running
    When I call the currencies endpoint
    Then the response status code should be 200
    And the returned currencies should match the expected list