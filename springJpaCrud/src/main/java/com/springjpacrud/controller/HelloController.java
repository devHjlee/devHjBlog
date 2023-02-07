package com.springjpacrud.controller;

import com.springjpacrud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {
    private final UserService userService;

    @RequestMapping("/hello")
    public ResponseEntity<?> hello(){
        return new ResponseEntity<>(userService.findAll(), HttpStatus.CREATED);
    }
}
