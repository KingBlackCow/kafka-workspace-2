package com.example.kafkaworkspace2.consumer;

import com.example.kafkaworkspace2.model.JsMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Integer> idHistoryMap = new ConcurrentHashMap<>(); // id에 대해 Exactly Once를 보장하기 위함

    @KafkaListener(
            topics = { JS_JSON_TOPIC },
            groupId = "test-consumer-group",
            concurrency = "1"
    )
    public void listen(ConsumerRecord<String, String> message, Acknowledgment acknowledgment) throws JsonProcessingException {
        JsMessage myMessage = objectMapper.readValue(message.value(), JsMessage.class);
        this.printPayloadIfFirstMessage(myMessage);
        acknowledgment.acknowledge();
    }

    private void printPayloadIfFirstMessage(JsMessage myMessage) {
        if (idHistoryMap.putIfAbsent(String.valueOf(myMessage.getId()), 1) == null) {
            System.out.println("[Main Consumer(" + Thread.currentThread().getId() + ")] Message arrived! - " + myMessage); // Exactly Once 실행되어야 하는 로직으로 가정
        } else {
            System.out.println("[Main Consumer(" + Thread.currentThread().getId() + ")] Duplicate! (" + myMessage.getId() + ")");
        }
    }
}
