package com.springeventpublisher.service;

import com.springeventpublisher.domain.TradeHistory;
import com.springeventpublisher.event.AlarmEvent;
import com.springeventpublisher.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CoinTradeService {
    final private TradeHistoryRepository tradeHistoryRepository;
    final private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void coinTrade(){
        log.info("Coin 자동구매 로직 실행");
        TradeHistory th = new TradeHistory();
        th.setCoin("BTC");
        th.setPrice(10000L);
        tradeHistoryRepository.save(th);
        log.info("Coin 자동구매 로직 종료");
        //Event 발생
        applicationEventPublisher.publishEvent(new AlarmEvent("USER1","BTC구매"));
        log.info("Coin 자동구매 종료");

        throw new RuntimeException();
    }
}
