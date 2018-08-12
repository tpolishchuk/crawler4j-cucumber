package org.lrp.standalone_runners;

import org.lrp.crawl_controllers.MultiSeedCrawlController;

public class MultiseedCrawlingStandaloneRunner {

    public static void main(String[] args) {
        boolean shouldBeExecuted = Boolean.parseBoolean(args[0]);

        if (!shouldBeExecuted) {
            return;
        }

        String configName = String.valueOf(args[1]);
        boolean printFailureReport = Boolean.parseBoolean(args[2]);

        MultiSeedCrawlController crawlController = new MultiSeedCrawlController(configName);
        crawlController.crawl(printFailureReport);
    }
}
