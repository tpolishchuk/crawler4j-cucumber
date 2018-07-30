package org.lrp.crawl_controllers;

import org.lrp.dtos.config.ControllerConfig;
import org.lrp.helpers.config.ControllerConfigHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiSeedCrawlController {

    private static final int POOL_THREADS_AMOUNT = 10;
    private List<OneSeedCrawlController> seedCrawlControllers;

    public MultiSeedCrawlController(String configName) {
        seedCrawlControllers = new ArrayList<>();
        initCrawlControllers(configName);
    }

    private void initCrawlControllers(String configName) {
        ControllerConfigHelper controllerConfigHelper = new ControllerConfigHelper();
        ControllerConfig controllerConfig = controllerConfigHelper.getConfig(configName);

        if(controllerConfig.getSeedConfigs() != null && !controllerConfig.getSeedConfigs().isEmpty()){
            for (ControllerConfig seedControllerConfig : controllerConfig.getSeedConfigs()) {
                OneSeedCrawlController seedCrawlController = new OneSeedCrawlController(controllerConfig, seedControllerConfig.getSeedUrl());
                seedCrawlControllers.add(seedCrawlController);
            }
            return;
        }

        OneSeedCrawlController seedCrawlController = new OneSeedCrawlController(controllerConfig, null);
        seedCrawlControllers.add(seedCrawlController);
    }

    public void crawl() {
        ExecutorService executor = Executors.newFixedThreadPool(POOL_THREADS_AMOUNT);

        for(OneSeedCrawlController seedCrawlController : seedCrawlControllers) {
            Runnable task = () -> seedCrawlController.crawl();
            executor.submit(task);
        }

        executor.shutdown();
    }
}
