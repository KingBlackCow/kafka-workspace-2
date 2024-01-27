package com.example.kafkaworkspace2.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.kafkaworkspace2.model.Topic.JS_STRING_TOPIC;

@Component
@Slf4j
public class StringConsumer {
    @KafkaListener(
            topics = {JS_STRING_TOPIC},
            groupId = "test-consumer-group",
            containerFactory = "secondKafkaListenerContainerFactory"
    )
    public void accept(ConsumerRecord<String, String> message) {
        log.info("[String Consumer] Message arrived! - " + message.value());
        log.info("[String Consumer] Offset - " + message.offset() + " / Partition - " + message.partition());
    }
}
