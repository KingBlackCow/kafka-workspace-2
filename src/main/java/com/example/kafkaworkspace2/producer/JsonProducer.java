package com.example.kafkaworkspace2.producer;

import com.example.kafkaworkspace2.model.JsMessage;
import com.example.kafkaworkspace2.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public void sendMessage(JsMessage message) throws JsonProcessingException {
        kafkaTemplate.send(
                Topic.JS_JSON_TOPIC,
                String.valueOf(message.getAge()),
                objectMapper.writeValueAsString(message)
        );
    }
}
