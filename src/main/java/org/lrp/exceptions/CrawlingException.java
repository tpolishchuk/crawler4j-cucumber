package org.lrp.exceptions;

import org.apache.tika.utils.ExceptionUtils;

public class CrawlingException extends RuntimeException {

    public CrawlingException(Throwable t){
        super("Unable to proceed with crawling. Got an exception:\n" + t.getMessage() +
              "\nFull stack trace:\n" + ExceptionUtils.getStackTrace(t));
    }
}
