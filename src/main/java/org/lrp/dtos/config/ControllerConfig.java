package org.lrp.dtos.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.lrp.dtos.AbstractDTO;
import org.lrp.exceptions.MandatoryParameterException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ControllerConfig extends AbstractDTO {

    @JsonAlias("crawler_storage_directory")
    private String crawlerStorageDirectory;

    @JsonAlias("crawler_report_directory")
    private String crawlerReportDirectory;

    @JsonAlias("number_of_crawlers")
    private Integer numberOfCrawlers;

    @JsonAlias("politeness_delay")
    private Integer politenessDelay;

    @JsonAlias("seed_url")
    private String seedUrl;

    @JsonAlias("max_depth_of_crawling")
    private Integer maxDepthOfCrawling;

    @JsonAlias("seed_success_report_file")
    private String seedSuccessReportFile;

    @JsonAlias("seed_error_report_file")
    private String seedErrorReportFile;

    @JsonAlias("excluded_urls_pattern")
    private String excludedUrlsPattern;

    @JsonAlias("max_pages_to_fetch")
    private Integer maxPagesToFetch;

    @JsonAlias("seeds")
    private List<ControllerConfig> seedConfigs;

    public String getCrawlerStorageDirectory() {
        return crawlerStorageDirectory;
    }

    public void setCrawlerStorageDirectory(String crawlerStorageDirectory) {
        this.crawlerStorageDirectory = crawlerStorageDirectory;
    }

    public String getCrawlerReportDirectory() {
        return crawlerReportDirectory;
    }

    public void setCrawlerReportDirectory(String crawlerReportDirectory) {
        this.crawlerReportDirectory = crawlerReportDirectory;
    }

    public int getNumberOfCrawlers() {
        return numberOfCrawlers;
    }

    public void setNumberOfCrawlers(int numberOfCrawlers) {
        this.numberOfCrawlers = numberOfCrawlers;
    }

    public int getPolitenessDelay() {
        return politenessDelay;
    }

    public void setPolitenessDelay(int politenessDelay) {
        this.politenessDelay = politenessDelay;
    }

    public String getSeedUrl() {
        return seedUrl;
    }

    public void setSeedUrl(String seedUrl) {
        this.seedUrl = seedUrl;
    }

    public int getMaxDepthOfCrawling() {
        return maxDepthOfCrawling;
    }

    public void setMaxDepthOfCrawling(int maxDepthOfCrawling) {
        this.maxDepthOfCrawling = maxDepthOfCrawling;
    }

    public String getSeedSuccessReportFile() {
        return seedSuccessReportFile;
    }

    public void setSeedSuccessReportFile(String seedSuccessReportFile) {
        this.seedSuccessReportFile = seedSuccessReportFile;
    }

    public String getSeedErrorReportFile() {
        return seedErrorReportFile;
    }

    public void setSeedErrorReportFile(String seedErrorReportFile) {
        this.seedErrorReportFile = seedErrorReportFile;
    }

    public String getExcludedUrlsPattern() {
        return excludedUrlsPattern;
    }

    public void setExcludedUrlsPattern(String excludedUrlsPattern) {
        this.excludedUrlsPattern = excludedUrlsPattern;
    }

    public Integer getMaxPagesToFetch() {
        return maxPagesToFetch;
    }

    public void setMaxPagesToFetch(Integer maxPagesToFetch) {
        this.maxPagesToFetch = maxPagesToFetch;
    }

    public List<ControllerConfig> getSeedConfigs() {
        return seedConfigs;
    }

    public ControllerConfig getSeedConfigBySeedUrl(String seedUrl) {
        if (this.getSeedConfigs() == null || this.getSeedConfigs().isEmpty()) {
            return null;
        }
        return getSeedConfigs().stream()
                               .filter(controllerConfig -> controllerConfig.getSeedUrl().equals(seedUrl))
                               .findFirst()
                               .orElse(null);

    }

    public void setSeedConfigs(List<ControllerConfig> seedConfigs) {
        this.seedConfigs = seedConfigs;
    }

    /**
     * Gets parameter from seed controller configuration
     * If seed controller configuration is absent or has no such parameter
     * Then parent controller config parameter is returned
     */

    public <T> T getParameter(String seedUrl, String parameterFieldOrJsonAlias, Class<T> clazz) {
        ControllerConfig seedControllerConfig = getSeedConfigBySeedUrl(seedUrl);

        if (seedControllerConfig == null) {
            return getParameter(this, parameterFieldOrJsonAlias, clazz);
        }

        if (getParameter(seedControllerConfig, parameterFieldOrJsonAlias, clazz) == null) {
            return getParameter(this, parameterFieldOrJsonAlias, clazz);
        }

        return getParameter(seedControllerConfig, parameterFieldOrJsonAlias, clazz);
    }

    /**
     * Gets parameter from seed controller configuration
     * If seed controller configuration is absent or has no such parameter
     * Then parent controller config parameter is returned
     */

    public <T> T getParameter(String seedUrl, String parameterFieldOrJsonAlias, Class<T> clazz, boolean isMandatory) {
        ControllerConfig seedControllerConfig = getSeedConfigBySeedUrl(seedUrl);
        T result;

        if (seedControllerConfig == null) {
            result = getParameter(this, parameterFieldOrJsonAlias, clazz);
        } else if (getParameter(seedControllerConfig, parameterFieldOrJsonAlias, clazz) == null) {
            result = getParameter(this, parameterFieldOrJsonAlias, clazz);
        } else {
            result = getParameter(seedControllerConfig, parameterFieldOrJsonAlias, clazz);
        }

        if (result == null && isMandatory) {
            throw new MandatoryParameterException(parameterFieldOrJsonAlias);
        }

        return result;
    }

    /**
     * Gets parameter from controller configuration
     * If controller configuration has no such parameter
     * Then null is returned
     *
     * @param controllerConfig          controller configuration
     * @param parameterFieldOrJsonAlias field name or @JsonAlias value
     * @param clazz                     class of a desired result object
     * @return parameter as defined clazz
     */
    public <T> T getParameter(ControllerConfig controllerConfig, String parameterFieldOrJsonAlias, Class<T> clazz) {
        for (Field field : controllerConfig.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            List<String> jsonAliasValues = Arrays.asList(field.getAnnotation(JsonAlias.class).value());

            if (fieldName.equals(parameterFieldOrJsonAlias) || jsonAliasValues.contains(parameterFieldOrJsonAlias)) {
                try {
                    return (T) field.get(controllerConfig);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
