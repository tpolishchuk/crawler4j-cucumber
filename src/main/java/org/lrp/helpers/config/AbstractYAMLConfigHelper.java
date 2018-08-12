package org.lrp.helpers.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.lrp.dtos.AbstractDTO;
import org.lrp.exceptions.ConfigNotFoundException;

import java.io.File;
import java.io.IOException;

public abstract class AbstractYAMLConfigHelper {

    protected <T extends AbstractDTO> T getDTOFromConfig(String configFilePath, Class<T> clazz){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        T instance = null;

        try {
            instance = mapper.readValue(new File(configFilePath), clazz);
        } catch (IOException e) {
            throw new ConfigNotFoundException(configFilePath);
        }

        return instance;
    }
}
