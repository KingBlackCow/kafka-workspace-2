package com.example.kafkaworkspace2.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.example.kafkaworkspace2.model.Topic.JS_STRING_TOPIC;

@Component
@RequiredArgsConstructor
public class StringProducer {

    @Qualifier("secondKafkaTemplate")
    private final KafkaTemplate<String, String> secondKafkaTemplate;

    public void sendMessageWithKey(String key, String message) {
        secondKafkaTemplate.send(JS_STRING_TOPIC, key, message);
    }
}
