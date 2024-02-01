package com.example.kafkaworkspace2.producer;

import com.example.kafkaworkspace2.common.CustomObjectMapper;
import com.example.kafkaworkspace2.model.JsCdcMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.example.kafkaworkspace2.model.Topic.*;

@RequiredArgsConstructor
@Component
public class JsCdcProducer {

    CustomObjectMapper objectMapper = new CustomObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(JsCdcMessage message) throws JsonProcessingException {
        kafkaTemplate.send(JS_CDC_TOPIC, objectMapper.writeValueAsString(message));
    }
}
