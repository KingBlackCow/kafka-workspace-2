package com.example.kafkaworkspacejava.event;

import com.example.kafkaworkspacejava.model.UserConverter;
import com.example.kafkaworkspacejava.producer.UserCdcProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class UserCdcApplicationEventListener {

    private final UserCdcProducer userCdcProducer;

    /* 미사용! */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void transactionalEventListenerAfterCommit(UserCdcApplicationEvent event) throws JsonProcessingException {
        userCdcProducer.sendMessage(UserConverter.toMessage(event.getId(), event.getUserDTO(), event.getOperationType()));
    }
}
