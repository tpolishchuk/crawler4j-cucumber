package org.lrp.exceptions;

public class ConfigNotFoundException extends RuntimeException {

    public ConfigNotFoundException(String configName){
        super(configName + " is not found");
    }
}
