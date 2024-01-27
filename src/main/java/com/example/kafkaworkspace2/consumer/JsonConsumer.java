package com.example.kafkaworkspace2.consumer;

import com.example.kafkaworkspace2.model.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.kafkaworkspace2.model.Topic.JS_JSON_TOPIC;

@Component
@Slf4j
public class JsonConsumer {
    @KafkaListener(
            topics = { JS_JSON_TOPIC },
            groupId = "js-json-consumer-group"
    )
    public void accept(ConsumerRecord<String, Data> message) {
        log.info("[json consumer] - " + message.value());
    }
}
