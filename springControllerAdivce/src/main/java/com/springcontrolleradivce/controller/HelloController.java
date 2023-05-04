package com.springcontrolleradivce.controller;

import com.springcontrolleradivce.exception.MyCustomException;
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

    @GetMapping("/v1/board")
    public ResponseEntity<ApiResponse> helloBoard() {
        return new ResponseEntity<>(new ApiResponse("200","Success"), HttpStatus.OK);
    }

    @GetMapping("/v1/calc")
    public ResponseEntity<ApiResponse> helloCalc() {
        helloService.helloCalc();
        return new ResponseEntity<>(new ApiResponse("200","Success"), HttpStatus.OK);
    }

    @GetMapping("/v1/custom")
    public ResponseEntity<ApiResponse> helloCustom() throws MyCustomException{
        throw new MyCustomException("강제 에러발생");
    }
}
