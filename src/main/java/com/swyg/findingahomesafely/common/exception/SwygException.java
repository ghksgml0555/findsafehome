package com.swyg.findingahomesafely.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SwygException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String errorCode;

    private String errorMessage;

    public SwygException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public SwygException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorMessage = errorMessage;
        this.errorCode = "";
    }

    public SwygException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = "";
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
