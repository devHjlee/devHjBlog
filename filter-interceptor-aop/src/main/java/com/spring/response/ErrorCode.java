package com.spring.response;

public enum ErrorCode {
    ERROR_001("An unexpected error occurred."),
    ERROR_002("AUTH FAIL."),
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