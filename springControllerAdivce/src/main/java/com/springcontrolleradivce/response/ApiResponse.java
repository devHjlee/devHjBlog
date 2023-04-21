package com.springcontrolleradivce.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse {
    private String status;
    private String message;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}