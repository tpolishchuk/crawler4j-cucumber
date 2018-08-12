package org.lrp.exceptions;

public class MandatoryParameterException extends RuntimeException {

    public MandatoryParameterException(String parameter){
        super(parameter + " is absent in requested controller configuration");
    }
}
