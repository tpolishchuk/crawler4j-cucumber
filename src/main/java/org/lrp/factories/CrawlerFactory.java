package org.lrp.factories;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.lrp.crawlers.Crawler;
import org.lrp.dtos.config.ControllerConfig;

public class CrawlerFactory implements CrawlController.WebCrawlerFactory {

    private String seedUrl;
    private ControllerConfig controllerConfig;

    public CrawlerFactory(String seedUrl, ControllerConfig controllerConfig) {
        setSeedUrl(seedUrl);
        setControllerConfig(controllerConfig);
    }

    private void setSeedUrl(String seedUrl) {
        this.seedUrl = seedUrl;
    }

    private void setControllerConfig(ControllerConfig controllerConfig) {
        this.controllerConfig = controllerConfig;
    }

    @Override
    public Crawler newInstance() {
        return new Crawler(seedUrl, controllerConfig);
    }
}
