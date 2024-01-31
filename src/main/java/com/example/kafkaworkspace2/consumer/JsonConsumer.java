package com.example.kafkaworkspace2.consumer;

import com.example.kafkaworkspace2.model.JsMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.kafkaworkspace2.model.Topic.JS_JSON_TOPIC;

@Component
@Slf4j
public class JsonConsumer {

    private final Map<String, Integer> idHistoryMap = new ConcurrentHashMap<>();

    @KafkaListener(
            topics = {JS_JSON_TOPIC},
            groupId = "js-json-consumer-group"
    )
    public void accept(ConsumerRecord<String, JsMessage> message, Acknowledgment acknowledgment) {
        log.info("[json consumer] - " + message.value());
        // 수동 커밋 설정
        this.printPayloadIfFirstMessage(message.value());
        acknowledgment.acknowledge();
    }

    private void printPayloadIfFirstMessage(JsMessage jsMessage) {
        if (idHistoryMap.putIfAbsent(String.valueOf(jsMessage.getId()), 1) == null) {
            log.info("[json first consumer] - " + jsMessage);
            idHistoryMap.put(String.valueOf(jsMessage.getId()), 1);
        } else {
            System.out.println("[json consumer] - Duplicate! id: " + jsMessage.getId());
        }
    }
}
