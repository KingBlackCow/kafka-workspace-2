package com.example.kafkaworkspacejava.producer;

import com.example.kafkaworkspacejava.model.Message;
import com.example.kafkaworkspacejava.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ObjectMapperProducer {
    ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(Message message) throws JsonProcessingException {
        kafkaTemplate.send(Topic.JS_STRING_TOPIC, String.valueOf(message.getAge()), objectMapper.writeValueAsString(message));
    }
}
