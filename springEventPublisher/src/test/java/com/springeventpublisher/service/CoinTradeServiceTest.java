package com.springeventpublisher.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class CoinTradeServiceTest {
    @Autowired
    CoinTradeService coinTradeService;
    @Test
    void coinTrade() {
        coinTradeService.coinTrade();
    }
}