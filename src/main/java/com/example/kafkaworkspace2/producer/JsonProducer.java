package com.example.kafkaworkspace2.producer;

import com.example.kafkaworkspace2.model.Data;
import com.example.kafkaworkspace2.model.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonProducer {
    private final KafkaTemplate<String, Data> kafkaTemplate;

    public void sendMessage(Data data) {
        kafkaTemplate.send(Topic.JS_JSON_TOPIC, String.valueOf(data.getAge()), data);
    }
}
