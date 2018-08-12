package org.lrp.crawl_controllers;

import org.lrp.dtos.config.ControllerConfig;
import org.lrp.helpers.config.ControllerConfigHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiSeedCrawlController {

    private static final int POOL_THREADS_AMOUNT = 10;
    private static final int DEFAULT_CRAWLING_TIMEOUT_MINUTES = 60;

    private List<OneSeedCrawlController> seedCrawlControllers;

    public MultiSeedCrawlController(String configName) {
        seedCrawlControllers = new ArrayList<>();
        initCrawlControllers(configName);
    }

    private void initCrawlControllers(String configName) {
        ControllerConfigHelper controllerConfigHelper = new ControllerConfigHelper();
        ControllerConfig controllerConfig = controllerConfigHelper.getConfig(configName);

        if (controllerConfig.getSeedConfigs() != null && !controllerConfig.getSeedConfigs().isEmpty()) {
            for (ControllerConfig seedControllerConfig : controllerConfig.getSeedConfigs()) {
                OneSeedCrawlController seedCrawlController = new OneSeedCrawlController(controllerConfig, seedControllerConfig.getSeedUrl());
                seedCrawlControllers.add(seedCrawlController);
            }
            return;
        }

        OneSeedCrawlController seedCrawlController = new OneSeedCrawlController(controllerConfig, null);
        seedCrawlControllers.add(seedCrawlController);
    }

    public boolean crawl() {
        return crawl(false);
    }

    public boolean crawl(boolean printFailureReport) {
        ExecutorService executor = Executors.newFixedThreadPool(POOL_THREADS_AMOUNT);

        for (OneSeedCrawlController seedCrawlController : seedCrawlControllers) {
            Runnable task = () -> seedCrawlController.crawl(printFailureReport);
            executor.submit(task);
        }

        executor.shutdown();

        try {
            // Waiting for all Runnables to finish. All seeds have the same value
            executor.awaitTermination(DEFAULT_CRAWLING_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return executor.isShutdown();
    }
}
