@one-seed @excluded
Feature: One seed crawling

  Scenario: One seed crawling
    When seed crawler performed crawling with configuration 'oneseed.controller.config.yaml'
    Then the following files should be created
      | tmp/reports/demoqa_error_urls.log   |
      | tmp/reports/demoqa_working_urls.log |
    And file 'tmp/reports/demoqa_working_urls.log' should contain at least 10 URLs
    And file 'tmp/reports/demoqa_error_urls.log' should contain at least 2 URLs
