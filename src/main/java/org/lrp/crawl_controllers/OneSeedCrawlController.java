package org.lrp.crawl_controllers;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lrp.dtos.config.ControllerConfig;
import org.lrp.exceptions.CrawlingException;
import org.lrp.factories.CrawlerFactory;
import org.lrp.helpers.common.FileHelper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class OneSeedCrawlController {

    private ControllerConfig controllerConfig;
    private CrawlConfig crawlConfig;
    private CrawlController crawlController;
    private PageFetcher pageFetcher;
    private RobotstxtConfig robotsTxtConfig;
    private RobotstxtServer robotsTxtServer;

    private String crawlerReportDirectory;
    private String seedUrl;

    public OneSeedCrawlController(ControllerConfig controllerConfig, String seedUrl) {
        setControllerConfig(controllerConfig);
        setSeedUrl(seedUrl);
        initCrawlConfig();
        initPageFetcher();
        initRobotsTxtConfig();
        initRobotsTxtServer();
        initCrawlController();
        initReportFolder();
    }

    private void setControllerConfig(ControllerConfig controllerConfig) {
        this.controllerConfig = controllerConfig;
    }

    private void setSeedUrl(String seedUrl) {
        if(StringUtils.isBlank(seedUrl)){
            this.seedUrl = controllerConfig.getSeedUrl();
            return;
        }
        this.seedUrl = seedUrl;
    }

    private void initCrawlConfig() {
        crawlConfig = new CrawlConfig();

        String crawlStorageDirectory = controllerConfig.getParameter(seedUrl, "crawler_storage_directory", String.class, true);
        if (StringUtils.isNotBlank(crawlStorageDirectory)) {
            crawlConfig.setCrawlStorageFolder(crawlStorageDirectory);
        }

        Integer politenessDelay = controllerConfig.getParameter(seedUrl, "politeness_delay", Integer.class);
        if (politenessDelay != null) {
            crawlConfig.setPolitenessDelay(politenessDelay);
        }

        Integer maxDepthOfCrawling = controllerConfig.getParameter(seedUrl, "max_depth_of_crawling", Integer.class);
        if (maxDepthOfCrawling != null) {
            crawlConfig.setMaxDepthOfCrawling(maxDepthOfCrawling);
        }

        Integer maxPagesToFetch = controllerConfig.getParameter(seedUrl, "max_pages_to_fetch", Integer.class);
        if (maxPagesToFetch != null) {
            crawlConfig.setMaxPagesToFetch(maxPagesToFetch);
        }
    }

    private void initPageFetcher() {
        pageFetcher = new PageFetcher(crawlConfig);
    }

    private void initRobotsTxtConfig() {
        robotsTxtConfig = new RobotstxtConfig();
    }

    private void initRobotsTxtServer() {
        robotsTxtServer = new RobotstxtServer(robotsTxtConfig, pageFetcher);
    }

    private void initCrawlController() {
        try {
            crawlController = new CrawlController(crawlConfig, pageFetcher, robotsTxtServer);
            crawlController.addSeed(seedUrl);
        } catch (Exception e) {
            throw new CrawlingException(e);
        }
    }

    private void initReportFolder() {
        crawlerReportDirectory = controllerConfig.getParameter(seedUrl, "crawler_report_directory", String.class, true);
        FileHelper.createDirectoryIfDoesNotExist(crawlerReportDirectory);
        FileHelper.cleanDirectoryIfNotEmpty(crawlerReportDirectory);
    }

    public void crawl() {
        Integer numberOfCrawlers = controllerConfig.getParameter(seedUrl, "number_of_crawlers", Integer.class, true);
        CrawlerFactory crawlerFactory = new CrawlerFactory(seedUrl, controllerConfig);
        crawlController.startNonBlocking(crawlerFactory, numberOfCrawlers);
        crawlController.waitUntilFinish();
        mergeSuccessReportsFromCrawlers();
        mergeErrorReportsFromCrawlers();
    }

    private void mergeSuccessReportsFromCrawlers() {
        String[] extensionsToParse = {"passed"};
        String successReportFileName = controllerConfig.getParameter(seedUrl, "seed_success_report_file", String.class, true);
        mergeThreadReportsFromCrawlers(extensionsToParse, successReportFileName);
    }

    private void mergeErrorReportsFromCrawlers() {
        String[] extensionsToParse = {"failed"};
        String errorReportFileName = controllerConfig.getParameter(seedUrl, "seed_error_report_file", String.class, true);
        mergeThreadReportsFromCrawlers(extensionsToParse, errorReportFileName);
    }

    private void mergeThreadReportsFromCrawlers(String[] extensionsToParse, String resultFileName) {
        File urlsList = new File(crawlerReportDirectory + resultFileName);
        String seedReportFolder = seedUrl.replaceAll("[\\W_]", "");
        FileHelper.createFile(urlsList);
        for (File logFile : FileUtils.listFiles(new File(crawlerReportDirectory + seedReportFolder), extensionsToParse, true)) {
            try {
                String content = FileUtils.readFileToString(logFile, Charset.defaultCharset());
                if (StringUtils.isNotBlank(content)) {
                    FileHelper.appendStringToFile(urlsList, content);
                }
            } catch (IOException e) {
                throw new IllegalStateException(String.format("Unable to merge crawler report into %s\n%s",
                                                              urlsList.getAbsolutePath(), e.getMessage()));
            }

            FileHelper.deleteFile(logFile);
        }
    }
}
