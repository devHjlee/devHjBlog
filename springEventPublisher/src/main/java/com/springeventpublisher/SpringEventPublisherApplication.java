package com.springeventpublisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // 비동기 Event를 위한 설정
public class SpringEventPublisherApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEventPublisherApplication.class, args);
    }

}
