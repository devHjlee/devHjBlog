package com.springcontrolleradivce.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String errorMessage;

    public ErrorResponse(ErrorCode errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = status.getReasonPhrase();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}