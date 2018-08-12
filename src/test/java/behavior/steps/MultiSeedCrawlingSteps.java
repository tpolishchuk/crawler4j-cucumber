package behavior.steps;

import cucumber.api.java8.En;
import org.lrp.crawl_controllers.MultiSeedCrawlController;

import static org.junit.Assert.assertTrue;

public class MultiSeedCrawlingSteps implements En {

    public MultiSeedCrawlingSteps() {

        When("seed crawler performed crawling with configuration {string}",0, (String string) -> {
            MultiSeedCrawlController crawlController = new MultiSeedCrawlController(string);
            assertTrue("Crawling is not properly finished", crawlController.crawl());
        });
    }
}
