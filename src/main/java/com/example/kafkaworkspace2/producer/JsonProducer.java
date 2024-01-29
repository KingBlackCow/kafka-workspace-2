package com.example.kafkaworkspace2.producer;

import com.example.kafkaworkspace2.model.JsMessage;
import com.example.kafkaworkspace2.model.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonProducer {
    private final KafkaTemplate<String, JsMessage> kafkaTemplate;

    public void sendMessage(JsMessage message) {
        kafkaTemplate.send(Topic.JS_JSON_TOPIC, String.valueOf(message.getAge()), message);
    }
}