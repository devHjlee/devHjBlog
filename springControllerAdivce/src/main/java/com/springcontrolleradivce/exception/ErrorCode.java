package com.springcontrolleradivce.exception;

public enum ErrorCode {
    ERROR_001("An unexpected error occurred."),
    ERROR_002("A custom error occurred."),
    // 추가로 필요한 에러코드를 여기에 선언합니다.
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}