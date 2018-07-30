package org.lrp.helpers.config;

import org.lrp.dtos.config.ControllerConfig;

public class ControllerConfigHelper extends AbstractYAMLConfigHelper {

    private static final String CONFIG_FILE_DIR_PATH = "src/main/resources/";

    public ControllerConfig getConfig(String name){
        return getDTOFromConfig(CONFIG_FILE_DIR_PATH + name, ControllerConfig.class);
    }
}
