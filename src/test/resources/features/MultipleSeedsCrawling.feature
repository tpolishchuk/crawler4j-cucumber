@multi-seed
Feature: Multiple seeds crawling

  Scenario: Multiple seeds crawling
    When seed crawler performed crawling with configuration 'multiseed.controller.config.yaml'
    Then the following files should be created
      | tmp/demoqa/reports/demoqa_error_urls.log         |
      | tmp/demoqa/reports/demoqa_working_urls.log       |
      | tmp/herokuapp/reports/herokuapp_error_urls.log   |
      | tmp/herokuapp/reports/herokuapp_working_urls.log |
    And file 'tmp/demoqa/reports/demoqa_working_urls.log' should contain at least 10 URLs
    And file 'tmp/demoqa/reports/demoqa_error_urls.log' should contain at least 2 URLs
    And file 'tmp/herokuapp/reports/herokuapp_working_urls.log' should contain at least 10 URLs
    And file 'tmp/herokuapp/reports/herokuapp_error_urls.log' should contain at least 2 URLs
