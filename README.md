# crawler4j-cucumber

This is just an example, how you can integrate a wonderful [crawler4j](https://github.com/yasserg/crawler4j) web crawler into your test project.
For this particular case I took test project based on Cucumber, but it can be done even as a standalone crawler (also is present and can be run).

### How to run crawler using Cucumber step definitions?

To compile project and run Cucumber tests, use the following command:

```
mvn clean integration-test
```

If you did not change core functionality and want to skip unit tests, use the following command:

```
mvn clean integration-test -DskipUTs=true
```

All Cucumber .feature files are stored under crawler4j-cucumber/src/test/resources/features.
You can amend them with new scenarios or new configuration.

### How to run crawler using a standalone runner?

To compile project run:

```
mvn clean compile
```

To use ready multiseed standalone crawler run:


```
mvn exec:java -DrunStandaloneCrawlers=true
```

Currently standalone runner takes 3 parameters:
1. execution flag. If it is false - runner will be terminated
2. controller configuration file name
3. failure report printing flag. If it is true - you will see an additional output in your console, which will duplicate failure report

To amend this data, go to 'standalone-runner' execution of exec-maven-plugin in pom.xml and put your changes here or create a new runner for desired project.

### How to add more seeds and configurations?

Default configuration path is crawler4j-cucumber/src/test/resources/configs.
It contains 2 types of configurations: for one seed and for several seeds.
Create your own configuration nearby and use its name in .feature files or standalone runner setup.

### How can I extend project for my needs?

Currently project supports not so much configuration arguments as crawler4j has. All of them are described in ControllerConfig DTO. 
For adding new property just extend ControllerConfig DTO and OneSeedCrawlController, where this property is used for config initialization.

### Can I ask for help or request additional example/feature?

Sure, 'Issues' and 'Pull requests' sections are always opened for requests :)
