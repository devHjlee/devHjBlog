package com.springeventpublisher.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlarmEventListener {
    @EventListener
    public void sendTelegram(AlarmEvent event) throws InterruptedException {
        log.info(String.format("텔레그램 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }

    @EventListener
    public void sendMail(AlarmEvent event) throws InterruptedException {
        log.info(String.format("EMAIL 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }
}