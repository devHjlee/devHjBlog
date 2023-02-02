package com.springbootquartz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiException extends RuntimeException{
    private final ErrorCode errorCode;
}
