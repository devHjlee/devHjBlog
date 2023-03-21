package com.springeventpublisher;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class SpringEventPublisherApplicationTests {

    @Test
    void contextLoads() {
        BigDecimal value1 = new BigDecimal("12.23");

// double 타입으로 초기화하는 방법
// 내부적으로 생성자 + 문자열을 사용한다.
        BigDecimal value2 = BigDecimal.valueOf(34.45);

// 아래와 같이 사용하면 안 된다.
// 12.230000000000000426325641456060111522674560546875
        BigDecimal dontDoThis = new BigDecimal(12.23);
        BigDecimal dontDoThis2 = new BigDecimal(12.23);
        System.out.println(value1.add(value2));
    }

}
