package com.com.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/getData")
    public ResponseEntity<Map<String, Object>> getData(@RequestParam String name, @RequestParam int age) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("name", name);
        resMap.put("age", age);

        return new ResponseEntity<Map<String,Object>>(resMap, HttpStatus.OK);
    }
}
