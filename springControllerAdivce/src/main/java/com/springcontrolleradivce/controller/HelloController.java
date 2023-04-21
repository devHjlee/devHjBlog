package com.springcontrolleradivce.controller;

import com.springcontrolleradivce.response.ApiResponse;
import com.springcontrolleradivce.service.HelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hello")
public class HelloController {
    private final HelloService helloService;

    @GetMapping("/v1/hello")
    public ResponseEntity<ApiResponse> hello(){
        return new ResponseEntity<>(new ApiResponse("200","Success"), HttpStatus.OK);
    }
}
