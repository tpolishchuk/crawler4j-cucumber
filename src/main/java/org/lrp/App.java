package org.lrp;

import org.lrp.crawl_controllers.MultiSeedCrawlController;

/**
 * TODO: remove
 *
 */
public class App
{
    public static void main( String[] args ) {
        MultiSeedCrawlController crawlController = new MultiSeedCrawlController("multiseed.controller.config.yaml");
        crawlController.crawl();
    }
}
