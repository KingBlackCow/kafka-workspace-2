package com.example.kafkaworkspacejava.producer;

import com.example.kafkaworkspacejava.common.CustomObjectMapper;
import com.example.kafkaworkspacejava.model.UserCdcMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.example.kafkaworkspacejava.model.Topic.*;

@RequiredArgsConstructor
@Component
public class UserCdcProducer {

    CustomObjectMapper objectMapper = new CustomObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(UserCdcMessage message) throws JsonProcessingException {
        kafkaTemplate.send(JS_CDC_TOPIC, String.valueOf(message.getId()),objectMapper.writeValueAsString(message));
    }
}
