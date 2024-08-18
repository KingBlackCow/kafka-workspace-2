package com.example.kafkaworkspacejava.consumer;

import com.example.kafkaworkspacejava.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.kafkaworkspacejava.model.Topic.JS_JSON_TOPIC;

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
        Message myMessage = objectMapper.readValue(message.value(), Message.class);
        this.printPayloadIfFirstMessage(myMessage);
        acknowledgment.acknowledge();
    }

    private void printPayloadIfFirstMessage(Message message) {
        if (idHistoryMap.putIfAbsent(String.valueOf(message.getId()), 1) == null) {
            log.info("[Main Consumer(" + Thread.currentThread().getId() + ")] Message arrived! - " + message); // Exactly Once 실행되어야 하는 로직으로 가정
        } else {
            log.info("[Main Consumer(" + Thread.currentThread().getId() + ")] Duplicate! (" + message.getId() + ")");
        }
    }
}
