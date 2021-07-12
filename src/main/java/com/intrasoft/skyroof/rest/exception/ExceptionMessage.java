package com.intrasoft.skyroof.rest.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionMessage {

    private final Throwable throwable;

    public ExceptionMessage(Throwable throwable) {
        this.throwable = throwable;
    }

    @JsonProperty("message")
    public String getMessage() {
        return throwable.getMessage();
    }

    @JsonProperty("cause")
    public String getCause() {
        return throwable.getClass().getName();
    }
}