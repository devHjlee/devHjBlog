package com.springeventpublisher.event;

import com.springeventpublisher.domain.AlarmHistory;
import com.springeventpublisher.repository.AlarmHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.transaction.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmEventListener {
    private final AlarmHistoryRepository alarmHistoryRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void sendSlack(AlarmEvent event) {
        AlarmHistory ah = new AlarmHistory();
        ah.setSendType("Slack");
        alarmHistoryRepository.save(ah);
        log.info(String.format("슬랙 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
        //throw new RuntimeException();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void sendTelegram(AlarmEvent event) {
        AlarmHistory ah = new AlarmHistory();
        ah.setSendType("Telegram");
        alarmHistoryRepository.save(ah);
        log.info(String.format("Telegram 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void sendKakao(AlarmEvent event) {
        AlarmHistory ah = new AlarmHistory();
        ah.setSendType("Kakao");
        alarmHistoryRepository.save(ah);
        log.info(String.format("Kakao 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void sendMail(AlarmEvent event) {
        AlarmHistory ah = new AlarmHistory();
        ah.setSendType("Email");
        alarmHistoryRepository.save(ah);
        log.info(String.format("EMAIL 발송[수신자 : %s][내용 : %s]", event.getUsrId(), event.getMsg()));
    }
}