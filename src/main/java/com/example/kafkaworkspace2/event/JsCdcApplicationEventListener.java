package com.example.kafkaworkspace2.event;

import com.example.kafkaworkspace2.model.JsConverter;
import com.example.kafkaworkspace2.producer.JsCdcProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class JsCdcApplicationEventListener {

    private final JsCdcProducer jsCdcProducer;

    /* 미사용! */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void transactionalEventListenerAfterCommit(JsCdcApplicationEvent event) throws JsonProcessingException {
        jsCdcProducer.sendMessage(JsConverter.toMessage(event.getId(), event.getJsDTO(), event.getOperationType()));
    }
}
