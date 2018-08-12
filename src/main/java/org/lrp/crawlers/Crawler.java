package org.lrp.crawlers;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.lrp.dtos.config.ControllerConfig;
import org.lrp.helpers.common.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.regex.Pattern;

public class Crawler extends WebCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    private ControllerConfig controllerConfig;
    private Page lastProcessedPage;
    private File passedUrlsReport;
    private File failedUrlsReport;
    private String crawlerId;
    private String seedUrl;
    private String seedReportFolder;

    public Crawler(String seedUrl, ControllerConfig controllerConfig) {
        setControllerConfig(controllerConfig);
        setSeedUrl(seedUrl);
        initSeedReportFolder();
        initCrawlerId();
    }

    @Override
    public void onStart() {
        initPassedUrlsReport();
        initFailedUrlsReport();
        LOG.info("\n\nStarted crawler {} for URL: {}\n\n", new Object[]{crawlerId, seedUrl});
    }

    public void setControllerConfig(ControllerConfig controllerConfig) {
        this.controllerConfig = controllerConfig;
    }

    public void setSeedUrl(String seedUrl) {
        this.seedUrl = seedUrl;
    }

    public void initSeedReportFolder() {
        seedReportFolder = seedUrl.replaceAll("[\\W_]", "");
        FileHelper.createDirectoryIfDoesNotExist(getCrawlerReportDirectory() + seedReportFolder);
    }

    private void initCrawlerId() {
        crawlerId = RandomStringUtils.randomNumeric(6);
    }

    private void initPassedUrlsReport() {
        passedUrlsReport = new File(getCrawlerReportDirectory() + seedReportFolder + "/" + crawlerId + ".passed");
        FileHelper.createFile(passedUrlsReport);
    }

    private void initFailedUrlsReport() {
        failedUrlsReport = new File(getCrawlerReportDirectory() + seedReportFolder + "/" + crawlerId + ".failed");
        FileHelper.createFile(failedUrlsReport);
    }

    private String getCrawlerReportDirectory() {
        return controllerConfig.getParameter(seedUrl, "crawler_report_directory", String.class, true);
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        String excludedUrlsPattern = controllerConfig.getParameter(seedUrl, "excluded_urls_pattern", String.class);
        if (StringUtils.isNotBlank(excludedUrlsPattern) && Pattern.compile(excludedUrlsPattern).matcher(href).matches()) {
            return false;
        }

        return url.getURL().startsWith(seedUrl);
    }

    @Override
    public void visit(Page page) {
        lastProcessedPage = page;
        FileHelper.appendRowToFile(passedUrlsReport, page.getWebURL().getURL());
    }

    private String getLastProcessedPageReferrer() {
        if (lastProcessedPage == null || lastProcessedPage.getWebURL().getParentUrl() == null) {
            return seedUrl;
        }
        return lastProcessedPage.getWebURL().getParentUrl();
    }

    @Override
    protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description) {
        String errorRow = String.format("%s: %s. Possibly referred from: %s. StatusCode: %s, %s, %s",
                                        ErrorTags.BROKEN_URL.getDescription(),
                                        urlStr, getLastProcessedPageReferrer(),
                                        statusCode, contentType, description);
        FileHelper.appendRowToFile(failedUrlsReport, errorRow);
    }

    @Override
    protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
        String errorRow = String.format("%s: Page %s exceeded size limit (%d). Possibly referred from: %s",
                                        ErrorTags.BROKEN_URL.getDescription(), urlStr, pageSize,
                                        getLastProcessedPageReferrer());

        FileHelper.appendRowToFile(failedUrlsReport, errorRow);
    }

    @Override
    protected void onUnhandledException(WebURL webUrl, Throwable e) {
        String urlStr = (webUrl == null ? "NULL" : webUrl.getURL());
        String errorRow = String.format("%s: while fetching %s possibly referred from %s: %s",
                                        ErrorTags.UNHANDLED_EXCEPTION.getDescription(), urlStr,
                                        getLastProcessedPageReferrer(), e.getMessage());
        FileHelper.appendRowToFile(failedUrlsReport, errorRow);
    }

    @Override
    protected void onContentFetchError(Page page) {
        String errorRow = String.format("%s: cannot fetch content",
                                        ErrorTags.BROKEN_URL.getDescription(), page.getWebURL().getURL());
        FileHelper.appendRowToFile(failedUrlsReport, errorRow);
    }

    @Override
    protected void onParseError(WebURL webUrl) {
        String errorRow = String.format("%s: cannot parse content",
                                        ErrorTags.BROKEN_URL.getDescription(), webUrl.getURL());
        FileHelper.appendRowToFile(failedUrlsReport, errorRow);
    }
}
