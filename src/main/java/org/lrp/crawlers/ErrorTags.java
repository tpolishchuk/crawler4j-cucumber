package org.lrp.crawlers;

public enum ErrorTags {

    BROKEN_URL("[Broken URL] Error on page"),
    UNHANDLED_EXCEPTION("[Unhandled exception] Error on page");

    private String description;

    ErrorTags(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
