Feature: User Registration API
  As a system
  I want to create a new user via API
  So that the user can later log in

  Scenario: Successfully create a new user
    Given I create the register body with random data
    When I send the request with a random username and password
    Then the response status code should be 200