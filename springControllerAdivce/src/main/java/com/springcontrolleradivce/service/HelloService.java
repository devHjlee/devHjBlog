package com.springcontrolleradivce.service;

import com.springcontrolleradivce.exception.CustomExceptionHandler;
import com.springcontrolleradivce.exception.MyCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelloService {

    public void helloBoard() {
      String text = "HELLO";
      System.out.println(text);
    }

    public void helloCalc() {
        int a = 0/0;
    }

}
